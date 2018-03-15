<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="en">
<#assign styles = [ "/css/index.css" ]>
<#include "header.inc.ftl">
<body>
<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col text-center">
            <span class="heading">The Agony Engine</span>
        </div>
    </div>

    <div class="row justify-content-start">
        <div class="col offset-1">
            <h1>A Modern MUD</h1>
            <p>The Agony Engine is a modern, customizable engine for web based MUDs.</p>
        </div>
    </div>
</div>

<#include "scripts.inc.ftl">
</body>
</html>