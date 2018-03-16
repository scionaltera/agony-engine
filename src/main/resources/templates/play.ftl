<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign title="Play">
<#assign path="/play">
<#assign styles = [ "/css/color.css", "/css/play.css" ]>
<#include "header.inc.ftl">
<body>
<div>
    <div id="output-box">
        <ul id="output-list">
            <noscript>
                <li style="color: #ff0000">It seems your browser doesn't support Javascript! Websocket relies on
                    Javascript being enabled. Please enable Javascript and reload this page!</li>
            </noscript>
        </ul>
    </div>
    <div id="input-box">
        <form id="user-input-form">
            <input type="text" id="user-input" autocomplete="off" autofocus />
        </form>
    </div>
</div>

<#include "scripts.inc.ftl">
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/webstomp-client@1.2.0/dist/webstomp.min.js"></script>
<script type="text/javascript" src="<@spring.url '/js/client.js'/>"></script>
</body>
</html>