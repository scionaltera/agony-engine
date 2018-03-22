<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign title="Login">
<#assign path="/login">
<#assign styles = [ "/css/index.css" ]>
<#include "header.inc.ftl">
<body>
<div class="container-fluid">
    <div class="row buffer">
        <div class="col text-center">
            <span class="heading">The Agony Engine</span>
        </div>
    </div>

    <div class="row">
        <div class="col-10 offset-1">
            <h1>Privacy Policy</h1>
            <p>The Agony Engine may collect and store Personally Identifiable Information (PII) including but not limited to IP addresses, hostnames and browser characteristics. PII collected is never shared with any third parties except as required by law.</p>
            <p>During the course of playing The Agony Engine you may interact with other players or site administrators. We do not regulate, cannot control and do not record these interactions. If you choose to divulge PII during your interactions with other players or site administrators that is solely your choice and you alone bear any consequences arising from it.</p>
            <p>The Agony Engine requires the use of browser cookies to function properly. By using the site you acknowledge and accept that cookies may be generated in your browser.</p>
            <p>Any cookies that The Agony Engine generates are strictly for one of the following purposes:</p>
            <ul>
                <li>Authentication: Allows you to register an account and log into the site.</li>
                <li>Session Identity: Allows the site to distinguish your browser from other users' browsers, keeping your session private and separate from others.</li>
                <li>Analytics: Allows the site operators to measure traffic to the site and improve your experience.</li>
            </ul>
            <p>We do not have any advertising on this site. Your PII will not be provided to any advertisers.</p>
        </div>
    </div>

    <#include "links.inc.ftl">
</div>

<#include "scripts.inc.ftl">
</body>
</html>
