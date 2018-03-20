<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign styles = [ "/css/index.css" ]>
<#include "header.inc.ftl">
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col text-center">
            <span class="heading">The Agony Engine</span>
        </div>
    </div>

    <#if name??>
    <div class="row">
        <div class="col text-right">
            <form id="new-player-form" class="form-horizontal" method="post" action="<@spring.url '/logout'/>">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button role="button" class="btn btn-primary">
                    <i class="fa fa-sign-out"></i> Logout
                </button>
            </form>
        </div>
    </div>
    <#else>
    <div class="row">
        <div class="col text-right">
            <a role="button" class="btn btn-secondary" href="<@spring.url '/login/new'/>">
                <i class="fa fa-plus"></i> Register
            </a>
            <a role="button" class="btn btn-primary" href="<@spring.url '/login'/>">
                <i class="fa fa-sign-in"></i> Login
            </a>
        </div>
    </div>
    </#if>

    <div class="row justify-content-start">
        <div class="col offset-1">
            <h1>A Modern MUD</h1>
            <p>The Agony Engine is a modern, customizable engine for web based MUDs.</p>
            <p><a href="<@spring.url '/play'/>">Play!</a></p>
            <p>Authenticated user: ${name!"N/A"}</p>
        </div>
    </div>
</div>

<#include "scripts.inc.ftl">
</body>
</html>