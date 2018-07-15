package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class InvokerService {
    private ApplicationContext applicationContext;

    @Inject
    public InvokerService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Transactional
    public void invoke(String verbBeanName, Actor actor, GameOutput output, Object ... arguments) {
        Object verbBean = applicationContext.getBean(verbBeanName);
        Method verbMethod = ReflectionUtils.findMethod(verbBean.getClass(), "invoke",
            Stream.concat(
                Arrays.stream(new Class<?>[] { Actor.class, GameOutput.class }),
                Arrays.stream(arguments).map(Object::getClass)
            ).toArray(Class<?>[]::new));

        if (arguments.length == 0) {
            ReflectionUtils.invokeMethod(verbMethod, verbBean, actor, output);
        } else {
            ReflectionUtils.invokeMethod(verbMethod, verbBean,
                Stream.concat(
                    Arrays.stream(new Object[] { actor, output }),
                    Arrays.stream(arguments)
                ).toArray());
        }
    }
}
