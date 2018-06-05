<#import "common/pagelib.ftl" as pageLib>
<#import "common/layout.ftl" as defaultLayout>
<#import "common/pager.ftl" as defaultPager>

<@defaultLayout.layout>
  
  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
  	  <form id="searchForm" class="form-inline">
  	    <select id="tagIdForSearch" name="tagId" class="form-control" onchange="quickSearch();">
		  <option value="">查询标签</option>
		  <#list tagList as tag>
		  <option value="${tag.id}">${tag.name}</option>
		  </#list>
		</select>
  	    <input type="text" id="urlLikeForSearch" name="urlLike" class="form-control" placeholder="请输入需要查询的URL">
        <button class="btn" id="submitSearch" type="button">查询</button>
        <button class="btn" id="resetSearch" type="button">重置</button>
        <button class="btn" id="batchConfirm" type="button">批量确认</button>
        <button class="btn" id="batchMistake" type="button">批量确认误判</button>
      </form>
    </div>
  </div>
  
  <div class="table-responsive">
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="60"></th>
      	<th data-width="80">标签</th>
        <th data-width="220">规则</th>
        <th data-width="60" data-type="number">评分</th>
        <th class="flex-col">可疑URL</th>
        <th data-width="80">确认状态</th>
        <th data-width="140" data-type="date">发现时间</th>
        <th data-width="80">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list suspectUrlPage.content as suspectUrl>
      <tr data-id="${suspectUrl.id}">
        <td>${suspectUrl_index+1}</td>
      	<td>${suspectUrl.tagName!'' }</td>
      	<td>${suspectUrl.tagRule}</td>
      	<td>${suspectUrl.tagRuleScore}</td>
      	<td style="word-break:break-all;">
      		${suspectUrl.url}
      		<span class="label label-badge label-primary data-clipboard-span" data-clipboard-text="${suspectUrl.url}">复制</span>
      	</td>
      	<td>
      		<#if suspectUrl.verify == 0 >
		    <span class="text-info">未确认</span>
		    <#elseif suspectUrl.verify == 1>
		    <span class="text-danger">钓鱼网站</span>
		    <#elseif suspectUrl.verify == 2>
		    <span class="text-success">误判</span>
		    </#if>
      	</td>
      	<td>${suspectUrl.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="window.open('${pageLib.ctx}/webpage/apa/suspect-url/${suspectUrl.id}/confirm','_blank')">
      	  	<span class="label label-badge label-primary">人工确认</span>
      	  </a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  
  <@defaultPager.pager pageable=suspectUrlPage url=pageLib.ctx+'/webpage/apa/suspect-url?tagId='+tagId+'&urlLike='+urlLike></@defaultPager.pager>
  </div>
  <script>
  
  $(function(){
    $('table.datatable').datatable({
    	checkable: true, 
    	sortable: true, 
    	fixedLeftWidth: 440, 
    	fixedRightWidth: 300
    });
    
    // 提取查询参数
    var urlLikeForS = extractAndDecodeParamValue("urlLike");
    if(urlLikeForS != null){
    	$("#searchForm #urlLikeForSearch").val(urlLikeForS);
	}else{
		urlLikeForS = '';		
	}
	var tagIdForS = extractAndDecodeParamValue("tagId");
    if(tagIdForS != null){
    	$("#searchForm #tagIdForSearch").val(tagIdForS);
	}else{
		tagIdForS = '';		
	}
    
	//查找
	$("#submitSearch").click(function(e){
		$("#searchForm").submit();
  	});
  	
	//重置查询条件
	$("#resetSearch").click(function(){
		window.location.href="${pageLib.ctx}/webpage/apa/suspect-url";
	});
	
	//批量确认
	$("#batchConfirm").click(function(){
		var myDatatable = $('table.datatable').data('zui.datatable');
		if(myDatatable.checks == null){
		  new $.zui.Messager('请选择需要批量确认的可疑URL', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
		  return;
		}
		var checkedIds = myDatatable.checks.checks;
		for(var idx in checkedIds){
		  window.open('${pageLib.ctx}/webpage/apa/suspect-url/'+checkedIds[idx]+'/confirm','_blank');
		}
	});
	//批量确认误判
	$("#batchMistake").click(function(){
		var myDatatable = $('table.datatable').data('zui.datatable');
		if(myDatatable.checks == null){
		  new $.zui.Messager('请选择需要批量设置为误判的可疑URL', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
		  return;
		}
		var checkedIds = myDatatable.checks.checks;
		var suspectUrlIds = [];
		for(var idx in checkedIds){
		  suspectUrlIds.push(checkedIds[idx]);
		}
		
		$.ajax({
	        type: 'put', 
	        url: '${pageLib.ctx}/webapi/apa/v1/suspect-urls/batch-verify', 
	        data: {
	        	'suspectUrlIds': suspectUrlIds.toString(),
	        	'verify': 2
	        },
	        success: function(data) { 
	            window.location.href="${pageLib.ctx}/webpage/apa/suspect-url?tagId=${tagId}&urlLike=${urlLike}";
	        }
    	});
		
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
  </script>
</@defaultLayout.layout>