<#import "common/pagelib.ftl" as pageLib>
<#import "common/layout.ftl" as defaultLayout>
<#import "common/pager.ftl" as defaultPager>

<@defaultLayout.layout>

  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
  	  <form id="searchForm" class="form-inline">
  	    <input id="startTimeForSearch" name="startTime" type="text" class="form-control form-datetime" placeholder="请选择起始时间">
  	    <input id="endTimeForSearch" name="endTime" type="text" class="form-control form-datetime" placeholder="请选择结束时间">
  	  	<input type="text" id="domainLikeForSearch" name="domainLike" class="form-control" placeholder="请输入需要查询的域名">
        <button class="btn" id="submitSearch" type="button">查询</button>
        <button class="btn" id="resetSearch" type="button">重置</button>
        <button class="btn" id="export" type="button">导出</button>
      </form>
    </div>
  </div>
  
  <div class="table-responsive">
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="60"></th>
        <th data-width="300">域名</th>
        <th class="flex-col" data-type="date">注册时间</th>
        <th data-width="140" data-type="date">导入时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list rawDomainPage.content as rawDomain>
      <tr>
        <td>${rawDomain_index+1}</td>
      	<td style="word-break:break-all;">
      		${rawDomain.domainName}
      		<span class="label label-badge label-primary data-clipboard-span" data-clipboard-text="${rawDomain.domainName}">复制</span>
      	</td>
      	<td>${(rawDomain.registrationDate?string('yyyy-MM-dd'))!''}</td>
      	<td>${rawDomain.createTime!''}</td>
      	<td>
      	  <#if rawDomain.isSensitive == 0>
      	  <a href="javascript:;" onclick="pushToSensitive('${rawDomain.domainName}');">
      	  	<span class="label label-badge label-primary">添加到敏感域名</span>
      	  </a>
      	  <#else>
      	  已添加为敏感域名
      	  </#if>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  
  <@defaultPager.pager pageable=rawDomainPage url=pageLib.ctx+'/webpage/apa/newly-domain?startTime='+startTime+'&endTime='+endTime+'&domainLike='+domainLike></@defaultPager.pager>
  </div>
  <script>
  var curQueryParamsWithoutPage = 'startTime=${startTime}&endTime=${endTime}&domainLike=${domainLike}';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${rawDomainPage.number+1};
 
  $(function(){
    // 选择时间和日期
	$("#startTimeForSearch").datetimepicker({
	    weekStart: 1,
	    todayBtn:  1,
	    autoclose: 1,
	    todayHighlight: 1,
	    startView: 2,
	    forceParse: 0,
	    showMeridian: 1,
	    format: "yyyy-mm-dd hh:ii"
	});
	$("#endTimeForSearch").datetimepicker({
	    weekStart: 1,
	    todayBtn:  1,
	    autoclose: 1,
	    todayHighlight: 1,
	    startView: 2,
	    forceParse: 0,
	    showMeridian: 1,
	    format: "yyyy-mm-dd hh:ii"
	});
	
    $('table.datatable').datatable({
    	checkable: false, 
    	sortable: true, 
    	fixedLeftWidth: 360, 
    	fixedRightWidth: 280
    });
    
    // 提取查询参数
    var startTimeForS = extractAndDecodeParamValue("startTime");
    if(startTimeForS != null){
    	startTimeForS = startTimeForS.replace("+"," ");
    	$("#searchForm #startTimeForSearch").val(startTimeForS);
    	$('#startTimeForSearch').datetimepicker('update');
	}else{
		startTimeForS = '';		
	}
	var endTimeForS = extractAndDecodeParamValue("endTime");
    if(endTimeForS != null){
    	endTimeForS = endTimeForS.replace("+"," ");
    	$("#searchForm #endTimeForSearch").val(endTimeForS);
    	$('#endTimeForSearch').datetimepicker('update');
	}else{
		endTimeForS = '';		
	}
    var domainLikeForS = extractAndDecodeParamValue("domainLike");
    if(domainLikeForS != null){
    	$("#searchForm #domainLikeForSearch").val(domainLikeForS);
	}else{
		domainLikeForS = '';		
	}
    
	//查找
	$("#submitSearch").click(function(e){
		$("#searchForm").submit();
  	});
  	
	//重置查询条件
	$("#resetSearch").click(function(){
		window.location.href="${pageLib.ctx}/webpage/apa/newly-domain";
	});
	
	//导出
	$("#export").click(function(){
		var startTime = $("#searchForm #startTimeForSearch").val();
		var endTime = $("#searchForm #endTimeForSearch").val();
		var domainLike = $("#searchForm #domainLikeForSearch").val();
		window.location.href="${pageLib.ctx}/webpage/apa/newly-domain/export?startTime="+startTime
				+"&endTime="+endTime+"&domainLike="+domainLike;
	});
	
	// 初始化复制
	var clipboard = new Clipboard('.data-clipboard-span');
	clipboard.on('success',function(e){
		new $.zui.Messager('域名已复制到粘贴板', {
                	type : 'success',
    				close: false,
    				time : 3000
				}).show();
	});
  });
  
  function pushToSensitive(domainName){
  	var category = 2;

  	$.ajax({
          type: 'post', 
          url: '${pageLib.ctx}/webapi/apa/v1/sensitive-domains', 
          data: {
          	'category' : category,
            'domainNames': domainName
          },
          success: function(data) { 
            window.location.href="${pageLib.ctx}/webpage/apa/newly-domain?"+currentQueryParams;
          }
     });
     return false; 
  }

  </script>
</@defaultLayout.layout>