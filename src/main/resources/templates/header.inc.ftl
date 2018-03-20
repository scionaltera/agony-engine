<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<#if title??>
    <title>${title} - The Agony Engine</title>
<#else>
    <title>The Agony Engine</title>
</#if>
    <meta name="description" content="The Agony Engine is a modern, customizable engine for web based MUDs."/>
    <meta name="viewport" content="width=device-width"/>
    <link rel="canonical" href="https://agonyengine.com${path!}"/>
    <link rel="stylesheet" type="text/css"
          href="<@spring.url 'https://fonts.googleapis.com/css?family=Inconsolata|Rock+Salt'/>">
    <link rel="stylesheet" type="text/css" href="<@spring.url '/webjars/bootstrap/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/webjars/font-awesome/css/font-awesome.min.css'/>"/>
<#if styles??>
    <#list styles as style>
    <link rel="stylesheet" type="text/css" href="<@spring.url '${style}'/>"/>
    </#list>
</#if>
</head>
