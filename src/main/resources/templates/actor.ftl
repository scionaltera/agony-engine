<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign title="Character">
<#assign path="/actor/new">
<#assign styles = [ "/css/index.css" ]>
<#include "header.inc.ftl">
<body>
<div class="container-fluid">
    <#include "title.inc.ftl">

    <div class="row">
        <div class="col-md"></div>
        <div class="col">
            <form id="new-actor-form" class="form-horizontal" method="post" action="<@spring.url '/actor/new'/>">
                <div class="form-group">
                    <div class="col">
                        <label for="given-name">Given Name</label>
                        <input type="text" class="form-control" name="givenName" id="given-name" value="${givenName!""}"
                               placeholder="Given Name" autofocus>
                        <small id="given-name-help" class="form-text text-muted">Also known as "first name" in Western cultures.</small>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col">
                        <label for="pronoun">Pronouns</label>
                        <select class="form-control" id="pronoun" name="pronoun">
                            <#list pronouns as pronoun>
                                <#if selectedPronoun?? && selectedPronoun == pronoun.subject>
                                    <option value="${pronoun.subject}" selected>${pronoun.subject}/${pronoun.object}</option>
                                <#else>
                                    <option value="${pronoun.subject}">${pronoun.subject}/${pronoun.object}</option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                </div>

                <#if errorText??>
                <div class="form-group">
                    <div class="col">
                        <span class="text-danger">${errorText}</span>
                    </div>
                </div>
                </#if>

                <div class="form-group">
                    <div class="col">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <a role="button" class="btn btn-danger" href="<@spring.url '/account'/>">
                            <i class="fa fa-times"></i> Cancel
                        </a>
                        <button role="button" class="btn btn-primary">
                            <i class="fa fa-plus"></i> Create
                        </button>
                    </div>
                </div>
            </form>
        </div>
        <div class="col-md"></div>
    </div>

    <#include "links.inc.ftl">
</div>
<#include "scripts.inc.ftl">
</body>
</html>
