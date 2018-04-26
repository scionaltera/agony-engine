<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign styles = [ "/css/index.css" ]>
<#include "header.inc.ftl">
<body>
<div class="container-fluid">
    <#assign nobuffer = true>
    <#include "title.inc.ftl">

    <#if name??>
    <div class="row">
        <div class="col text-right">
            <a role="button" class="btn btn-secondary" href="<@spring.url '/account'/>">
                <i class="fa fa-address-book"></i> Account
            </a>
            <form id="new-player-form" class="form-horizontal" style="display: inline-block;" method="post" action="<@spring.url '/logout'/>">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button role="button" class="btn btn-primary">
                    <i class="fa fa-sign-out-alt"></i> Logout
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
                <i class="fa fa-sign-in-alt"></i> Login
            </a>
        </div>
    </div>
    </#if>

    <div class="row justify-content-start">
        <div class="col-5 offset-1">
            <h1>Introduction</h1>
            <p>The Agony Engine is a modern, customizable engine for web based MUDs. It is MIT licensed and designed to be modified and expanded. You can use it as a platform for building your own multi-player text based games.</p>
            <p>Most MUD server software in use today dates back a decade or more. The Agony Engine aims to provide a modern, free, secure alternative.</p>
            <ul>
                <li>Runs in modern browsers without plugins or software installs.</li>
                <li>HTTPS encryption to ensure safe communication with the server.</li>
                <li>BCrypt encryption for passwords stored in the database.</li>
                <li>Runs in a Docker container for easy testing and deployment.</li>
                <li>Written in Java using Spring Boot allowing for rapid development, powerful debugging tools and a massive amount of community know-how.</li>
            </ul>
            <p>For information about getting your own copy of The Agony Engine, please see our <a href="https://github.com/scionaltera/agony-engine">project on GitHub</a>.</p>
        </div>

        <div class="col-5">
            <h1>What is a MUD?</h1>
            <p><em>MUDs are multi-player text based games, like MMORPGs without the fancy graphics.</em> You play a character in a rich, detailed world that the game describes to you through text. You interact with the game by typing commands to walk around, talk, fight, craft, cast spells and more.</p>
            <p>The first MUDs gained popularity in the 80s and 90s when telnet was a popular protocol and text based games like <a href="https://en.wikipedia.org/wiki/Zork">Zork</a> were commonplace. Much time has passed but MUDs are still around. On many of them, the code remains very similar to what it looked like 20 years ago. The Agony Engine is an attempt to bring modern technology to this retro form of gaming, and to make it easy for people to start up their own MUDs.</p>
        </div>
    </div>

    <#include "links.inc.ftl">
</div>

<#include "scripts.inc.ftl">
</body>
</html>
