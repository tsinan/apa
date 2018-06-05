<#import "common/pagelib.ftl" as pageLib>
<#import "common/layout_window.ftl" as windowLayout>
<#import "common/pager.ftl" as defaultPager>
<#import "common/dialog.ftl" as defaultDialog>

<@windowLayout.layout>
  
  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
  	  <form id="inspectUrlForm" class="form-inline">
  	  	<#if inspectUrl.inspectLevel = 0>
  	  	<button class="btn" id="routineBtn" type="button">例行巡检</button>
  	  	<#elseif inspectUrl.inspectLevel = 1>
        <button class="btn" id="suspendBtn" type="button">暂停巡检</button>
        </#if>
        <button class="btn" id="closeBtn" type="button">关闭本页</button>
      </form>
    </div>
  </div>
  
  <div class="table-responsive">
  	  <table class="table datatable">
	    <thead>
	      <tr>
	        <th data-width="60"></th>
	      	<th data-width="200">URL</th>
	        <th data-width="80">IP</th>
	      	<th class="flex-col">巡检状态</th>
	      	<th class="flex-col">巡检信息</th>
	        <th class="flex-col">巡检关键字/得分</th>
	        <th class="flex-col">巡检次数</th>
	        <th class="flex-col">在线时长</th>
	        <th data-width="140" data-type="date">巡检时间</th>
	        <th data-width="140" data-type="date">下次巡检时间</th>
	        <th data-width="80" data-type="date">操作</th>
	      </tr>
	    </thead>
	    <tbody>
	      <#list inspectTracePage.content as inspectTrace>
	      <tr data-id="${inspectTrace.id}">
	        <td>${inspectTrace_index+1}</td>
	      	<td style="word-break:break-all;">
	      		<a href="javascript:;" onclick="window.open('${inspectTrace.url}','_blank');">${inspectTrace.url}</a>
	      		<span class="label label-badge label-primary data-clipboard-span" data-clipboard-text="${inspectTrace.url}">复制</span>
	      	</td>
	      	<td style="word-break:break-all;">${inspectTrace.ipAddress!''}</td>
	      	<td style="word-break:break-all;">
	      		<#if inspectTrace.inspectStatus = 0>
	      		  初始
	      		<#elseif inspectTrace.inspectStatus = 1>
	      		  在线
	      		<#elseif inspectTrace.inspectStatus = 2>
	      		  内容变化
	      		<#elseif inspectTrace.inspectStatus = 3>
	      		  离线
	      		</#if>
	      	</td>
	      	<td style="word-break:break-all;">
	      		${inspectTrace.inspectMessage!''}
	      	</td>
	      	<td style="word-break:break-all;">
	      		${inspectTrace.inspectKeyword!''}=${inspectTrace.inspectKeywordScore!''} 
	      	</td>
	      	<td style="word-break:break-all;">
	      		${inspectTrace.inspectTimes!''}
	      	</td>
	      	<td style="word-break:break-all;">
	      		<#if inspectTrace.activeDuration lt 60>
	      		${inspectTrace.activeDuration }秒
	      		<#elseif inspectTrace.activeDuration lt 3600>
	      		${inspectTrace.activeDuration/60 }分
	      		<#elseif inspectTrace.activeDuration lt 86400>
	      		${inspectTrace.activeDuration/3600 }小时
	      		<#else>
	      		${inspectTrace.activeDuration/86400 }天
	      		</#if>
	      	</td>
	      	<td style="word-break:break-all;">
	      		${inspectTrace.inspectTime!''}
	      	</td>
	      	<td style="word-break:break-all;">
	      		${inspectTrace.inspectNextTime!''}
	      	</td>
	      	<td>
	      		<a href="javascript:;" onclick="viewData('${inspectTrace.id}')">
	      			<span class="label label-badge label-success">网页内容</span>
	      		</a>
	      	</td>
	      </tr>
	      </#list>
	    </tbody>
	  </table>
	  <@defaultPager.pager pageable=inspectTracePage url=pageLib.ctx+'/webpage/apa/inspect-trace?inspectUrlId='+inspectUrlId></@defaultPager.pager>
  </div>
    
    
  <#-- 查看网页内容对话框 -->
  <@defaultDialog.dialog id="viewInspectTraceModal" title="查看网页内容" hasSubmit=false>
   <table class="table">
    <tbody>
      <tr>
      	<td width="20%">httpStatusCode:</td>
      	<td width="80%" style="word-break:break-all;"><span id="httpStatusCode"></span></td>
      </tr>
      <tr>
      	<td>httpHeaders:</td>
      	<td><span id="httpHeaders"></span></td>
      </tr>
      <tr>
      	<td>textTokens:</td>
      	<td><span id="textTokens"></span></td>
      </tr>
      <tr>
      	<td>textOther:</td>
      	<td><span id="textOther"></span></td>
      </tr>
    </tbody>
  </table>
  </@defaultDialog.dialog>
  
  
  <script>
  var curQueryParamsWithoutPage = 'inspectUrlId=${inspectUrlId }';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${inspectTracePage.number+1};
 
  $(function(){
  
    $('table.datatable').datatable({
    	checkable: false, 
    	sortable: true, 
    	fixedLeftWidth: 340, 
    	fixedRightWidth: 360
    });
    
	// 初始化复制
	var clipboard = new Clipboard('.data-clipboard-span');
	clipboard.on('success',function(e){
		new $.zui.Messager('可疑URL已复制到粘贴板', {
                	type : 'success',
    				close: false,
    				time : 3000
				}).show();
	});
	
	// 设置为例行巡检
	$("#routineBtn").click(function(e){
		var data = "inspectLevel=1";
	    $.ajax({
	        type: 'PUT',
	        url: '${pageLib.ctx}/webapi/apa/v1/inspect-urls/${inspectUrlId}/inspect-level',
	        data: data,
	        success: function(msg){
				window.location.href="${pageLib.ctx}/webpage/apa/inspect-trace?"+currentQueryParams;
	   		}
		});
  	});
  	
  	// 设置为暂停巡检
	$("#suspendBtn").click(function(e){
		var data = "inspectLevel=0";
	    $.ajax({
	        type: 'PUT',
	        url: '${pageLib.ctx}/webapi/apa/v1/inspect-urls/${inspectUrlId}/inspect-level',
	        data: data,
	        success: function(msg){
				window.location.href="${pageLib.ctx}/webpage/apa/inspect-trace?"+currentQueryParams;
	   		}
		});
  	});
  	
  	// 关闭本页
  	$("#closeBtn").click(function(e){
  		window.opener = null;
        window.open('','_self');
        window.close();
        return;
    });

  });
  
  function viewData(id){
    $.ajax({
		type: 'GET',
		async: false,
		url: '${pageLib.ctx}/webapi/apa/v1/inspect-traces/'+id,
		cache: false,
		success: function(data) {
			$('#viewInspectTraceModal #httpStatusCode').html(data.httpStatusCode);
			$('#viewInspectTraceModal #httpHeaders').html(data.httpHeaders);
			$('#viewInspectTraceModal #textTokens').html(data.textTokens);
			$('#viewInspectTraceModal #textOther').html(data.textOther);
		    $('#viewInspectTraceModal').modal();
        }
	});
  }
  
  </script>
</@windowLayout.layout>