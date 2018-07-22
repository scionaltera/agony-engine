package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.interpret.ArgumentBinding;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.interpret.Verb;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.VerbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class InvokerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvokerService.class);
    private static final int STANDARD_CMD_ARG_COUNT = 2;

    private ApplicationContext applicationContext;
    private VerbRepository verbRepository;

    @Inject
    public InvokerService(
        ApplicationContext applicationContext,
        VerbRepository verbRepository) {

        this.applicationContext = applicationContext;
        this.verbRepository = verbRepository;
    }

    @Transactional
    public void invoke(Actor actor, GameOutput output, UserInput rawInput, List<String> tokens) {
        String verbToken = tokens.get(0);
        Verb verb = verbRepository.findFirstByNameIgnoreCaseStartingWith(
            Sort.by(Sort.Direction.ASC, "priority", "name"),
            verbToken)
            .orElseThrow(() -> new IllegalArgumentException("Unknown verb: " + verbToken));

        Object verbBean = applicationContext.getBean(verb.getBean());

        List<Method> methods = Arrays.stream(ReflectionUtils.getUniqueDeclaredMethods(verbBean.getClass()))
            .filter(m -> "invoke".equals(m.getName()))
            .filter(m -> (verb.isQuoting() && tokens.size() > 1) || STANDARD_CMD_ARG_COUNT + (tokens.size() - 1) == m.getParameterCount())
            .collect(Collectors.toList());

        for (Method method : methods) {
            List<ArgumentBinding> arguments = new ArrayList<>();

            if (tokens.size() == 1) { // no args
                ReflectionUtils.invokeMethod(method, verbBean, actor, output);
                return;
            } else if (verb.isQuoting()) { // verb automatically quotes the rest of the input (e.g. SAY)
                ReflectionUtils.invokeMethod(method, verbBean, actor, output, new QuotedString(UserInput.removeFirstWord(rawInput.getInput())));
                return;
            } else { // verb has arguments after it that we need to bind objects to
                boolean isBindingSuccessful = true;

                for (int i = STANDARD_CMD_ARG_COUNT; i < method.getParameterCount(); i++) {
                    ArgumentBinding binding = (ArgumentBinding) applicationContext.getBean(method.getParameterTypes()[i]);

                    if (binding.bind(actor, tokens.get(i - STANDARD_CMD_ARG_COUNT + 1))) {
                        arguments.add(binding);
                    } else {
                        isBindingSuccessful = false;
                    }
                }

                if (isBindingSuccessful) {
                    ReflectionUtils.invokeMethod(
                        method,
                        verbBean,
                        Stream.concat(
                            Arrays.stream(new Object[]{actor, output}),
                            arguments.stream())
                            .toArray());

                    return;
                }
            }
        }

        output.append("Unable to bind arguments.");
    }
}
