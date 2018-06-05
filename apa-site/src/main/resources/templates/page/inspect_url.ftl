<#import "common/pagelib.ftl" as pageLib>
<#import "common/layout.ftl" as defaultLayout>
<#import "common/pager.ftl" as defaultPager>
<#import "common/dialog.ftl" as defaultDialog>

<@defaultLayout.layout>
  
  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
  	  <form id="searchForm" class="form-inline">
  	    <input id="startTimeForSearch" name="startTime" type="text" class="form-control form-datetime" placeholder="请选择起始时间">
  	    <input id="endTimeForSearch" name="endTime" type="text" class="form-control form-datetime" placeholder="请选择结束时间">
  	  	<select id="inspectLevelForSearch" name="inspectLevel" class="form-control" onchange="quickSearch();">
		  <option value="">巡检级别</option>
		  <option value="0">暂停巡检</option>
		  <option value="1">例行巡检</option>
		</select>
		<select id="inspectStatusForSearch" name="inspectStatus" class="form-control" onchange="quickSearch();">
		  <option value="">巡检状态</option>
		  <option value="0">初始状态</option>
		  <option value="1">在线</option>
		  <option value="2">内容变更</option>
		  <option value="3">离线</option>
		</select>
		<select id="categoryForSearch" name="category" class="form-control" onchange="quickSearch();">
		  <option value="">录入方式</option>
		  <option value="0">自动检测</option>
		  <option value="1">手工录入</option>
		</select>
  	    <select id="brandIdForSearch" name="brandId" class="form-control" onchange="quickSearch();" style="width:80px">
		  <option value="">品牌</option>
		  <#list brandList as brand>
		  <option value="${brand.id}">${brand.name}</option>
		  </#list>
		</select>
  	    <input type="text" id="urlLikeForSearch" name="urlLike" class="form-control" placeholder="请输入需要查询的URL">
        <button class="btn" id="submitSearch" type="button">查询</button>
        <button class="btn" id="resetSearch" type="button">重置</button>
        <button class="btn" type="button" data-toggle="modal" data-target="#createInspectUrlModal" >添加</button>
        <button class="btn" id="export" type="button">导出</button>
      </form>
    </div>
  </div>
  
  <#-- 添加巡检URL对话框 -->
  <@defaultDialog.dialog id="createInspectUrlModal" title="添加巡检URL">
    <div class="row">
	    <div class="form-group col-md-8">
	      <label for="url">URL</label>
	      <input type="text" class="form-control" id="url" placeholder="例如：http://www.domain.com/page.html">
	    </div>
	    <div class="form-group col-md-4">
	      <label for="url">内容关键字</label>
	      <input type="text" class="form-control" id="inspectKeyword" placeholder="不填时系统自动提取">
	    </div>
	</div>
    <div class="row">
	    <div class="form-group col-md-4">
	      <label for="brandId">品牌</label>
	      <select id="brandId" class="form-control">
			  <#list brandList as brand>
			  <option value="${brand.id}">${brand.name}</option>
			  </#list>
		  </select>
	    </div>
	    <div class="form-group col-md-4">
	      <label for="inspectLevel">巡检级别</label>
	 	  <select id="inspectLevel" class="form-control">
			  <option value="0">暂停巡检</option>
			  <option value="1">例行巡检</option>
			</select>
	    </div>
	    <div class="form-group col-md-4">
	      <label for="incidentNo">工单号</label>
	      <input type="text" class="form-control" id="incidentNo" placeholder="">
	    </div>
    </div>
    <div class="row">
	    <div class="form-group col-md-4">
	      <label for="whoisRegName">域名注册商</label>
	      <input type="text" class="form-control" id="whoisRegName" placeholder="例如：persion">
	    </div>
	    <div class="form-group col-md-4">
	      <label for="whoisRegEmail">域名注册人Email</label>
	      <input type="text" class="form-control" id="whoisRegEmail" placeholder="例如：persion@server.com">
	    </div>
	    <div class="form-group col-md-4">
	      <label for="whoisWebhost">托管主机</label>
	      <input type="text" class="form-control" id="whoisWebhost" placeholder="例如：aliyun">
	    </div>
    </div>
    
  </@defaultDialog.dialog>
  
  <div class="table-responsive">
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="60"></th>
        <th data-width="140">品牌</th>
      	<th data-width="200">URL</th>
      	<th class="flex-col">IP</th>
      	<th class="flex-col">注册商名称</th>
      	<th class="flex-col">注册人Email</th>
        <th class="flex-col">最新状态</th>
        <th class="flex-col">在线时长</th>
        <th class="flex-col">录入方式</th>
        <th data-width="140" data-type="date">录入时间</th>
        <th data-width="80">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list inspectUrlPage.content as inspectUrl>
      <#if inspectUrl.inspectStatus == 1 || inspectUrl.inspectStatus == 2>
      <tr data-id="${inspectUrl.id}" class="success">
      <#elseif inspectUrl.inspectStatus == 3>
      <tr data-id="${inspectUrl.id}" class="warning">
      <#else>
      <tr data-id="${inspectUrl.id}">
      </#if>
        <td>${inspectUrl_index+1}</td>
      	<td style="word-break:break-all;">${inspectUrl.brandName!'' }</td>
      	<td style="word-break:break-all;">
      		<a href="javascript:;" onclick="window.open('${inspectUrl.url}','_blank');">${inspectUrl.url}</a>
      		<span class="label label-badge label-primary data-clipboard-span" data-clipboard-text="${inspectUrl.url}">复制</span>
      	</td>
      	<td style="word-break:break-all;">${inspectUrl.ipAddress!''}</td>
      	<td style="word-break:break-all;">${inspectUrl.whoisRegName!''}</td>
      	<td style="word-break:break-all;">${inspectUrl.whoisRegEmail!''}</td>
      	<td style="word-break:break-all;">
      		<#if inspectUrl.inspectStatus == 0>
      		初始状态
      		<#elseif inspectUrl.inspectStatus == 1>
      		在线
      		<#elseif inspectUrl.inspectStatus == 2>
      		内容改变
      		<#elseif inspectUrl.inspectStatus == 3>
      		离线
      		</#if>
      	</td>
      	<td style="word-break:break-all;">
      		<#if inspectUrl.activeDuration lt 60>
      		${inspectUrl.activeDuration }秒
      		<#elseif inspectUrl.activeDuration lt 3600>
      		${inspectUrl.activeDuration/60 }分
      		<#elseif inspectUrl.activeDuration lt 86400>
      		${inspectUrl.activeDuration/3600 }小时
      		<#else>
      		${inspectUrl.activeDuration/86400 }天
      		</#if>
      	</td>
      	<td style="word-break:break-all;">
      		<#if inspectUrl.category == 0>
      		自动检测
      		<#elseif inspectUrl.category == 1>
      		人工录入
      		</#if>
      	</td>
      	<td style="word-break:break-all;">
      		${inspectUrl.createTime}
      	</td>
      	<td>
      	  <a href="javascript:;" onclick="viewData('${inspectUrl.id}',event)">
      	  	<span class="label label-badge label-success">最近巡检</span>
      	  </a>
      	  <a href="javascript:;" onclick="window.open('${pageLib.ctx}/webpage/apa/inspect-trace?inspectUrlId=${inspectUrl.id}','_blank')">
      	  	<span class="label label-badge label-primary">巡检跟踪</span>
      	  </a>
      	  <a href="javascript:;" onclick="editData('${inspectUrl.id}',event)">
      	  	<span class="label label-badge label-info">编辑</span>
      	  </a>
      	  <a href="javascript:;" onclick="deleteData('${inspectUrl.id}');">
      	  	<span class="label label-badge label-danger">删除</span>
      	  </a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  
  <@defaultPager.pager pageable=inspectUrlPage url=pageLib.ctx+'/webpage/apa/inspect-url?startTime='+startTime+'&endTime='+endTime+'&inspectLevel='+inspectLevel+'&inspectStatus='+inspectStatus+'&category='+category+'&brandId='+brandId+'&urlLike='+urlLike></@defaultPager.pager>
  </div>
  
  <#-- 编辑巡检Url对话框 -->
  <@defaultDialog.dialog id="editInspectUrlModal" title="编辑巡检URL">
    <div class="row">
	    <div class="form-group col-md-8">
	      <label for="url">URL</label>
	      <input type="text" class="form-control" id="url" placeholder="例如：http://www.domain.com/page.html">
	    </div>
	    <div class="form-group col-md-4">
	      <label for="url">内容关键字</label>
	      <input type="text" class="form-control" id="inspectKeyword" placeholder="例如："平安银行" AND ("CVV2" OR "密码")">
	    </div>
	</div>
    <div class="row">
	    <div class="form-group col-md-4">
	      <label for="brandId">品牌</label>
	      <select id="brandId" class="form-control">
			  <#list brandList as brand>
			  <option value="${brand.id}">${brand.name}</option>
			  </#list>
		  </select>
	    </div>
	    <div class="form-group col-md-4">
	      <label for="inspectLevel">巡检级别</label>
	 	  <select id="inspectLevel" class="form-control">
			  <option value="0">暂停巡检</option>
			  <option value="1">例行巡检</option>
			</select>
	    </div>
	    <div class="form-group col-md-4">
	      <label for="incidentNo">工单号</label>
	      <input type="text" class="form-control" id="incidentNo" placeholder="">
	    </div>
    </div>
    <div class="row">
	    <div class="form-group col-md-4">
	      <label for="whoisRegName">域名注册商</label>
	      <input type="text" class="form-control" id="whoisRegName" placeholder="例如：persion">
	    </div>
	    <div class="form-group col-md-4">
	      <label for="whoisRegEmail">域名注册人Email</label>
	      <input type="text" class="form-control" id="whoisRegEmail" placeholder="例如：persion@server.com">
	    </div>
	    <div class="form-group col-md-4">
	      <label for="whoisWebhost">托管主机</label>
	      <input type="text" class="form-control" id="whoisWebhost" placeholder="例如：aliyun">
	    </div>
    </div>
    
  </@defaultDialog.dialog>
  
  <#-- 查看最近巡检对话框 -->
  <@defaultDialog.dialog id="viewInspectUrlModal" title="查看最近巡检" hasSubmit=false>
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
  <@defaultDialog.confirmDialog id="deleteInspectUrlModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = 'startTime=${startTime}&endTime=${endTime}&inspectLevel=${inspectLevel}&inspectStatus=${inspectStatus}&category=${category}&brandId=${brandId }&urlLike=${urlLike }';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${inspectUrlPage.number+1};
 
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
    	fixedLeftWidth: 400, 
    	fixedRightWidth: 220
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
	var inspectLevelForS = extractAndDecodeParamValue("inspectLevel");
    if(inspectLevelForS != null){
    	$("#searchForm #inspectLevelForSearch").val(inspectLevelForS);
	}else{
		inspectLevelForS = '';		
	}
	var inspectStatusForS = extractAndDecodeParamValue("inspectStatus");
    if(inspectStatusForS != null){
    	$("#searchForm #inspectStatusForSearch").val(inspectStatusForS);
	}else{
		inspectStatusForS = '';		
	}
	var categoryForS = extractAndDecodeParamValue("category");
    if(categoryForS != null){
    	$("#searchForm #categoryForSearch").val(categoryForS);
	}else{
		categoryForS = '';		
	}
	var brandIdForS = extractAndDecodeParamValue("brandId");
    if(brandIdForS != null){
    	$("#searchForm #brandIdForSearch").val(brandIdForS);
	}else{
		brandIdForS = '';		
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
		window.location.href="${pageLib.ctx}/webpage/apa/inspect-url";
	});
	
	//导出
	$("#export").click(function(){
		var startTime = $("#searchForm #startTimeForSearch").val();
		var endTime = $("#searchForm #endTimeForSearch").val();
		var inspectLevel = $("#searchForm #inspectLevelForSearch").val();
		var inspectStatus = $("#searchForm #inspectStatusForSearch").val();
		var category = $("#searchForm #categoryForSearch").val();
		var brandId = $("#searchForm #brandIdForSearch").val();
		var urlLike = $("#searchForm #urlLikeForSearch").val();
		window.location.href="${pageLib.ctx}/webpage/apa/inspect-url/export?startTime="+startTime
				+"&endTime="+endTime+"&inspectLevel="+inspectLevel+"&inspectStatus="+inspectStatus+"&category="+category
				+"&brandId="+brandId+"&urlLike="+urlLike;
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
	
	// 添加
    $("#createInspectUrlModalForm").on("submit",function(){
    	var category = 1;
        var url = $('#createInspectUrlModalForm #url').val();
        var inspectKeyword = $('#createInspectUrlModalForm #inspectKeyword').val();
        var brandId = $('#createInspectUrlModalForm #brandId').val();
        var incidentNo = $('#createInspectUrlModalForm #incidentNo').val();
        var inspectLevel = $('#createInspectUrlModalForm #inspectLevel').val();
        var whoisRegName = $('#createInspectUrlModalForm #whoisRegName').val();
        var whoisRegEmail = $('#createInspectUrlModalForm #whoisRegEmail').val();
        var whoisWebhost = $('#createInspectUrlModalForm #whoisWebhost').val();
        
        if(url.length == 0 ){
        	new $.zui.Messager('URL、内容关键字为必填项', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
        	return false;
        }
        
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/inspect-urls', 
            data: {
            	'category' : category,
                'url': url,
                'inspectKeyword': inspectKeyword,
                'brandId':brandId,
                'incidentNo': incidentNo,
                'inspectLevel': inspectLevel,
                'whoisRegName': whoisRegName,
                'whoisRegEmail': whoisRegEmail,
                'whoisWebhost': whoisWebhost
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/inspect-url?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function quickSearch(){
   	$("#searchForm").submit();
  }
  
  function editData(id,e){
    $.ajax({
		type: 'GET',
		async: false,
		url: '${pageLib.ctx}/webapi/apa/v1/inspect-urls/'+id,
		cache: false,
		success: function(data) {
			$('#editInspectUrlModal #url').val(data.url);
			$('#editInspectUrlModal #inspectKeyword').val(data.inspectKeyword);
		    $('#editInspectUrlModal #brandId').val(data.brandId);
		    $('#editInspectUrlModal #incidentNo').val(data.incidentNo);
		    $('#editInspectUrlModal #inspectLevel').val(data.inspectLevel);
		    $('#editInspectUrlModal #whoisRegName').val(data.whoisRegName);
		    $('#editInspectUrlModal #whoisRegEmail').val(data.whoisRegEmail);
		    $('#editInspectUrlModal #whoisWebhost').val(data.whoisWebhost);
        }
	});
		
    // 修改
    $("#editInspectUrlModal").on("submit",function(){
        var url = $('#editInspectUrlModal #url').val();
        var inspectKeyword = $('#editInspectUrlModalForm #inspectKeyword').val();
        var brandId = $('#editInspectUrlModal #brandId').val();
        var incidentNo = $('#editInspectUrlModal #incidentNo').val();
        var inspectLevel = $('#editInspectUrlModal #inspectLevel').val();
        var whoisRegName = $('#editInspectUrlModal #whoisRegName').val();
        var whoisRegEmail = $('#editInspectUrlModal #whoisRegEmail').val();
        var whoisWebhost = $('#editInspectUrlModal #whoisWebhost').val();
        
        if(url.length == 0 ){
        	new $.zui.Messager('URL、内容关键字为必填项', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
        	return false;
        }
        
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/inspect-urls/'+id, 
            data: {
                'url': url,
                'inspectKeyword': inspectKeyword,
                'brandId':brandId,
                'incidentNo': incidentNo,
                'inspectLevel': inspectLevel,
                'whoisRegName': whoisRegName,
                'whoisRegEmail': whoisRegEmail,
                'whoisWebhost': whoisWebhost
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/inspect-url?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editInspectUrlModal').modal();
  }
  
  function viewData(id,e){
    $.ajax({
		type: 'GET',
		async: false,
		url: '${pageLib.ctx}/webapi/apa/v1/inspect-urls/'+id,
		cache: false,
		success: function(data) {
			$('#viewInspectUrlModal #url').html(data.url);
			$('#viewInspectUrlModal #inspectKeyword').html(data.inspectKeyword+'/'+data.inspectKeywordScore);


		    $('#viewInspectUrlModal #ipAddress').html(data.ipAddress);			
			var inspectLevel = '暂停巡检';
			if(data.inspectLevel==1){
			  inspectLevel = '例行巡检';
			}
		    $('#viewInspectUrlModal #inspectLevel').html(inspectLevel);
			
			var inspectStatus = '初始状态';
			if(data.inspectStatus==1){
			  inspectStatus = '在线';
			}else if(data.inspectStatus==2){
			  inspectStatus = '内容改变';
			}else if(data.inspectStatus==3){
			  inspectStatus = '离线';
			}
		    $('#viewInspectUrlModal #inspectStatus').html(inspectStatus);
		    $('#viewInspectUrlModal #inspectMessage').html(data.inspectMessage);
		    
		    $('#viewInspectUrlModal #inspectTime').html(dateToStringYYYY_MM_DD_HH_MM_SS(data.inspectTime));
		    $('#viewInspectUrlModal #inspectNextTime').html(dateToStringYYYY_MM_DD_HH_MM_SS(data.inspectNextTime));
		    
		    var activeDuration = '0';
		    if(data.activeDuration<60){
		    	activeDuration = data.activeDuration+"秒";
		    }else if(data.activeDuration<3600){
		    	activeDuration = (data.activeDuration/60).toFixed(3)+"分";
		    }else if(data.activeDuration<86400){
		    	activeDuration = (data.activeDuration/3600).toFixed(3)+"小时";
		    }else{
		    	activeDuration = (data.activeDuration/86400).toFixed(3)+"天";
		    }
		    $('#viewInspectUrlModal #activeDuration').html(activeDuration);
		    $('#viewInspectUrlModal #inspectTimes').html(data.inspectTimes+"次");
		    
		    $('#viewInspectUrlModal').modal();
        }
	});
  }
  
  function deleteData(id){
    $("#deleteInspectUrlModalForm").on("submit",function(){
  	  var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/inspect-urls/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/inspect-url?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteInspectUrlModal').modal();
  }
  </script>
</@defaultLayout.layout>