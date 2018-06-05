<header id="header">
<nav id="mainmenu" class="navbar navbar-inverse" role="navigation">
  <div class="container-fluid">
    <!-- 导航头部 -->
    <div class="navbar-header">
      <!-- 移动设备上的导航切换按钮 -->
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-apa">
        <span class="sr-only"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <!-- 品牌名称或logo -->
      <a class="navbar-brand" href="${pageLib.ctx}/">APA</a>
    </div>
    <!-- 导航项目 -->
    <div class="collapse navbar-collapse navbar-collapse-apa">
      <!-- 一般导航项目 -->
      <ul class="nav navbar-nav">
      	
      	<#list Request["current_menu"] as menu>
      	  <#if menu.children?size <= 1>
      	    <#if menu.selected>      	
        	<li class="active">
            <#else>
            <li>        
      	    </#if>
      	    <a href="${pageLib.ctx}${menu.url}">${menu.text}</a></li>
      	  <#else>
            <!-- 导航中的下拉菜单 -->
            <#if menu.selected>
	        <li class="active" class="dropdown">
	        <#else>
	        <li class="dropdown">
	        </#if>
	          <a href="" class="dropdown-toggle" data-toggle="dropdown">${menu.text} <b class="caret"></b></a>
	          <ul class="dropdown-menu" role="menu">
	          	<#list menu.children as child>
	            <li><a href="${pageLib.ctx}${child.url}">${child.text}</a></li>
	            </#list>
	          </ul>
	        </li>
      	  </#if>
        </#list>
        
      </ul>
    </div><!-- END .navbar-collapse -->
  </div>
</nav>
<nav id="modulemenu">
</nav>
</header>
