<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign title="Account">
<#assign path="/account">
<#assign styles = [ "/css/index.css" ]>
<#include "header.inc.ftl">
<body>
<div class="container-fluid">
    <#include "title.inc.ftl">

    <div class="row">
        <div class="col text-right">
            <a role="button" class="btn btn-secondary" href="<@spring.url '/actor/new'/>">
                <i class="fa fa-plus"></i> Create
            </a>
            <form id="new-player-form" class="form-horizontal" style="display: inline-block;" method="post" action="<@spring.url '/logout'/>">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button role="button" class="btn btn-primary">
                    <i class="fa fa-sign-out-alt"></i> Logout
                </button>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-md"></div>
        <div class="col">
            <#if actors?size == 0>
            <div>No characters created yet.</div>
            <#else>
            <table class="table table-sm table-dark table-hover">
                <thead>
                <tr><th scope="col">Given Name</th><th scope="col">Actions</th></tr>
                </thead>
                <tbody>
                <#list actors as actor>
                <tr>
                    <td>${actor.name}</td>
                    <td>
                        <a role="button" class="btn btn-primary" href="<@spring.url '/play/${actor.id}'/>">
                            <i class="fa fa-play"></i> Play
                        </a>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
            </#if>
        </div>
        <div class="col-md"></div>
    </div>

    <#include "links.inc.ftl">
</div>
<#include "scripts.inc.ftl">
</body>
</html>
