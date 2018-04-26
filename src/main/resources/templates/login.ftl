<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign title="Login">
<#assign path="/login">
<#assign styles = [ "/css/index.css" ]>
<#include "header.inc.ftl">
<body>
<div class="container-fluid">
    <#include "title.inc.ftl">

    <div class="row">
        <div class="col-md"></div>
        <div class="col">
            <form id="new-player-form" class="form-horizontal" method="post" action="<@spring.url '/login'/>">
                <div class="form-group">
                    <div class="col">
                        <label for="username">Username</label>
                        <#if username??>
                        <input type="text" class="form-control" name="username" id="username" value="${username!""}" placeholder="Username">
                        <#else>
                        <input type="text" class="form-control" name="username" id="username" value="${username!""}" placeholder="Username" autofocus>
                        </#if>

                    </div>
                </div>

                <div class="form-group">
                    <div class="col">
                        <label for="password">Password</label>
                        <#if username??>
                        <input type="password" class="form-control" name="password" id="password" autofocus>
                        <#else>
                        <input type="password" class="form-control" name="password" id="password">
                        </#if>

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
                        <a role="button" class="btn btn-danger" href="<@spring.url '/'/>">
                            <i class="fa fa-times"></i> Cancel
                        </a>
                        <button role="button" class="btn btn-primary">
                            <i class="fa fa-sign-in-alt"></i> Login
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
