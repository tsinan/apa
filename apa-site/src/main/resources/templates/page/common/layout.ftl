<#macro layout>
<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>APA Console</title>
	<#include "resource.ftl">
  </head>
  <body style="background:#e5e5e5; " >
    <#include "header.ftl">
    
    <div id="wrap">
    <div class="outer">
    <#nested>
    </div>
    </div>
    
    <#include "footer.ftl">
  </body>
</html>
</#macro>