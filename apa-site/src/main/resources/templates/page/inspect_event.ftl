<#import "common/pagelib.ftl" as pageLib>
<#import "common/layout.ftl" as defaultLayout>
<#import "common/pager.ftl" as defaultPager>
<#import "common/dialog.ftl" as defaultDialog>

<@defaultLayout.layout>
  
  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
  	  <form id="searchForm" class="form-inline">
  	    <select id="progressForSearch" name="progress" class="form-control" onchange="quickSearch();">
		  <option value="">处理进展</option>
		  <option value="0">未处理</option>
		  <option value="1">已处理</option>
		  <option value="2">无需处理</option>
		</select>
  	    <input type="text" id="urlLikeForSearch" name="urlLike" class="form-control" placeholder="请输入需要查询的URL">
        <button class="btn" id="submitSearch" type="button">查询</button>
        <button class="btn" id="resetSearch" type="button">重置</button>
      </form>
    </div>
  </div>
  
  <div class="table-responsive">
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="60"></th>
        <th data-width="80">类型</th>
      	<th data-width="200">URL</th>
      	<th class="flex-col">巡检状态</th>
      	<th class="flex-col">巡检信息</th>
        <th class="flex-col">巡检关键字</th>
        <th data-width="140" data-type="date">前次巡检时间</th>
        <th data-width="140" data-type="date">巡检时间</th>
        <th data-width="80">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list inspectEventPage.content as inspectEvent>
      <#if inspectEvent.category == 0>
      <tr data-id="${inspectEvent.id}" class="danger">
      <#elseif inspectEvent.category == 1>
      <tr data-id="${inspectEvent.id}" class="warning">
      <#else>
      <tr data-id="${inspectEvent.id}">
      </#if>
        <td>${inspectEvent_index+1}</td>
      	<td style="word-break:break-all;">
      		 <#if inspectEvent.category == 0>
      		 状态切换
      		 <#elseif inspectEvent.category == 1>
      		 内容变化
      		 </#if>
      	</td>
      	<td style="word-break:break-all;">
      		<a href="javascript:;" onclick="window.open('${inspectEvent.url}','_blank');">${inspectEvent.url}</a>
      		<span class="label label-badge label-primary data-clipboard-span" data-clipboard-text="${inspectEvent.url}">复制</span>
      	</td>
      	<td style="word-break:break-all;">
      		<#if inspectEvent.statusFrom = 0>
      		  初始
      		<#elseif inspectEvent.statusFrom = 1>
      		  在线
      		<#elseif inspectEvent.statusFrom = 2>
      		  内容变化
      		<#elseif inspectEvent.statusFrom = 3>
      		  离线
      		</#if>
      		-> 
      		<#if inspectEvent.statusTo = 0>
      		  初始
      		<#elseif inspectEvent.statusTo = 1>
      		  在线
      		<#elseif inspectEvent.statusTo = 2>
      		  内容变化
      		<#elseif inspectEvent.statusTo = 3>
      		  离线
      		</#if>
      	</td>
      	<td style="word-break:break-all;">
      		${inspectEvent.messageFrom!''} -> ${inspectEvent.messageTo!''}
      	</td>
      	<td style="word-break:break-all;">
      		${inspectEvent.keywordFrom!''}=${inspectEvent.keywordScoreFrom!''} 
      		-> 
      		${inspectEvent.keywordTo!''}=${inspectEvent.keywordScoreTo!''}
      	</td>
      	<td style="word-break:break-all;">
      		${inspectEvent.timeFrom!''}
      	</td>
      	<td style="word-break:break-all;">
      		${inspectEvent.timeTo!''}
      	</td>
      	<td>
      	  <a href="javascript:;" onclick="window.open('${pageLib.ctx}/webpage/apa/inspect-trace?inspectUrlId=${inspectEvent.urlId}','_blank')">
      	  	<span class="label label-badge label-primary">巡检跟踪</span>
      	  </a>
      	  <#if inspectEvent.progress = 0 >
      	  <a href="javascript:;" onclick="changeProgress('${inspectEvent.id}',1)">
      	  	<span class="label label-badge label-success">标记处理</span>
      	  </a>
      	  <a href="javascript:;" onclick="changeProgress('${inspectEvent.id}',2)">
      	  	<span class="label label-badge label-gray">无需处理</span>
      	  </a>
      	  <#elseif inspectEvent.progress = 1 || inspectEvent.progress =2 >
      	  <a href="javascript:;" onclick="changeProgress('${inspectEvent.id}',0);">
      	  	<span class="label label-badge label-info">重置</span>
      	  </a>
      	  <a href="javascript:;" onclick="deleteData('${inspectEvent.id}');">
      	  	<span class="label label-badge label-danger">删除</span>
      	  </a>
      	  </#if>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  
  <@defaultPager.pager pageable=inspectEventPage url=pageLib.ctx+'/webpage/apa/inspect-event?progress='+progress+'&urlLike='+urlLike></@defaultPager.pager>
  </div>
  
  <#-- 查看巡检日志对话框 -->
  <@defaultDialog.dialog id="viewInspectTraceModal" title="查看巡检状况" hasSubmit=false>
   <table class="table">
    <tbody>
      <tr>
      	<td width="20%">URL:</td>
      	<td width="30%" style="word-break:break-all;"><span id="url"></span></td>
      	<td width="20%">关键字/得分:</td>
      	<td width="30%"><span id="inspectKeyword"></span></td>
      </tr>
      <tr>
      	<td>IP地址:</td>
      	<td><span id="ipAddress"></span></td>
      	<td>巡检级别:</td>
      	<td><span id="inspectLevel"></span></td>
      </tr>
      <tr>
      	<td>在线状态:</td>
      	<td><span id="inspectStatus"></span></td>
      	<td>巡检信息:</td>
      	<td><span id="inspectMessage"></span></td>
      </tr>
      <tr>
      	<td>巡检时间:</td>
      	<td><span id="inspectTime"></span></td>
      	<td>下次巡检时间:</td>
      	<td><span id="inspectNextTime"></span></td>
      </tr>
      <tr>
      	<td>在线时长:</td>
      	<td><span id="activeDuration"></span></td>
      	<td>巡检次数:</td>
      	<td><span id="inspectTimes"></span></td>
      </tr>
    </tbody>
  </table>
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deleteInspectEventModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = 'progress=${progress}&urlLike=${urlLike }';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${inspectEventPage.number+1};
 
  $(function(){
    $('table.datatable').datatable({
    	checkable: false, 
    	sortable: true, 
    	fixedLeftWidth: 340, 
    	fixedRightWidth: 360
    });
    
    // 提取查询参数
    var progressForS = extractAndDecodeParamValue("progress");
    if(progressForS != null){
    	$("#searchForm #progressForSearch").val(progressForS);
	}else{
		progressForS = '';		
	}
    var urlLikeForS = extractAndDecodeParamValue("urlLike");
    if(urlLikeForS != null){
    	$("#searchForm #urlLikeForSearch").val(urlLikeForS);
	}else{
		urlLikeForS = '';		
	}
    
	//查找
	$("#submitSearch").click(function(e){
		$("#searchForm").submit();
  	});
  	
	//重置查询条件
	$("#resetSearch").click(function(){
		window.location.href="${pageLib.ctx}/webpage/apa/inspect-event";
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
	
  });
  
  function quickSearch(){
   	$("#searchForm").submit();
  }
  
  function changeProgress(id,progress){
	var data = "progress="+progress;
    $.ajax({
        type: 'PUT',
        url: '${pageLib.ctx}/webapi/apa/v1/inspect-events/'+id+'/progress',
        data: data,
        success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/inspect-event?"+currentQueryParams;
   		}
	});
  }
  
  function deleteData(id){
    $("#deleteInspectEventModalForm").on("submit",function(){
  	  var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/inspect-events/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/inspect-event?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteInspectEventModal').modal();
  }
  </script>
</@defaultLayout.layout>