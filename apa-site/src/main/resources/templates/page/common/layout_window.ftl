<#macro layout>
<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>APA Console</title>
	<#include "resource.ftl">
	<style>
	#wrap-w {
	  padding:0 19px 20px 19px;
	  background:#e5e5e5;
	  margin-top: 19px;
	}
	#wrap-w .outer {
	  position:relative;
	  text-align:left;
	  background:#fff;
	  box-shadow:0 1px 5px rgba(0,0,0,0.1);
	  padding:20px;
	  min-height:600px;
	}
	#wrap-w .outer .row-table {
      display: table;
      width: 100%;
	}
	.col-main, .col-side {
      display: table-cell;
      vertical-align: top;
	}
	.col-side {
      width: 350px;
      max-width: 350px;
      padding-left: 10px;
	}
	.tab-content {
      border: 1px solid #ddd;
      border-top: 0;
      padding: 10px 10px;
	}
	
	</style>
  </head>
  <body style="background:#e5e5e5; " >
    
    
    <div id="wrap-w">
    <div class="outer">
    <#nested>
    </div>
    </div>
    
  </body>
</html>
</#macro>