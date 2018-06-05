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
  	  	<input type="text" id="domainLikeForSearch" name="domainLike" class="form-control" placeholder="请输入需要查询的域名">
        <button class="btn" id="submitSearch" type="button">查询</button>
        <button class="btn" id="resetSearch" type="button">重置</button>
        <button class="btn" type="button" data-toggle="modal" data-target="#createSensitiveDomainModal" >添加</button>
        <button class="btn" id="export" type="button">导出</button>
      </form>
    </div>
  </div>
  
  <#-- 添加敏感域名对话框 -->
  <@defaultDialog.dialog id="createSensitiveDomainModal" title="添加敏感域名">
    <div class="row">
	    <div class="form-group col-md-8">
	      <label for="domainNames">域名</label>
	      <textarea class="form-control" id="domainNames" rows="3" placeholder="例如：domain.com, domain2.com"></textarea>
	    </div>
	</div>
  </@defaultDialog.dialog>
  
  
  <div class="table-responsive">
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="60"></th>
        <th data-width="300">域名</th>
        <th class="flex-col" data-type="date">注册时间</th>
        <th class="flex-col" data-width="300">详细信息</th>
        <th class="flex-col">录入方式</th>
        <th data-width="140" data-type="date">录入时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list sensitiveDomainPage.content as sensitiveDomain>
      <tr>
        <td>${sensitiveDomain_index+1}</td>
      	<td style="word-break:break-all;">
      		${sensitiveDomain.domainName}
      		<span class="label label-badge label-primary data-clipboard-span" data-clipboard-text="${sensitiveDomain.domainName}">复制</span>
      	</td>
      	<td>${(sensitiveDomain.registrationDate?string('yyyy-MM-dd'))!''}</td>
        <td>${sensitiveDomain.sensitiveWordName}/${sensitiveDomain.sensitiveWord}/${sensitiveDomain.lcsLength}</td>
        <td><#if sensitiveDomain.category == 0 >
        	机器识别
        	<#elseif sensitiveDomain.category == 1>
        	人工录入
        	<#elseif sensitiveDomain.category == 2>
        	原始域名添加
        	<#elseif sensitiveDomain.category == 3>
        	IP反查
        	<#elseif sensitiveDomain.category == 4>
        	EMAIL反查
        	<#else>
        	未知
        	</#if>
        </td>
      	<td>${sensitiveDomain.createTime!''}</td>
      	<td>
      	  <#if sensitiveDomain.category == 1>
      	  <a href="javascript:;" onclick="editData('${sensitiveDomain.id}',event)">
      	  	<span class="label label-badge label-info">编辑</span>
      	  </a>
      	  </#if>
      	  <a href="javascript:;" onclick="deleteData('${sensitiveDomain.id}');">
      	  	<span class="label label-badge label-danger">删除</span>
      	  </a>
        </td>
      </tr>
      </#list>
    </tbody>
  </table>
  
  <@defaultPager.pager pageable=sensitiveDomainPage url=pageLib.ctx+'/webpage/apa/sensitive-domain?startTime='+startTime+'&endTime='+endTime+'&domainLike='+domainLike></@defaultPager.pager>
  </div>
  
  <#-- 编辑敏感域名对话框 -->
  <@defaultDialog.dialog id="editSensitiveDomainModal" title="编辑敏感域名">
     <div class="row">
	    <div class="form-group col-md-8">
	      <label for="domainName">域名</label>
	      <input type="text" class="form-control" id="domainName" placeholder="例如：domain.com">
	    </div>
	</div>
   
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deleteSensitiveDomainModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = 'startTime=${startTime}&endTime=${endTime}&domainLike=${domainLike}';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${sensitiveDomainPage.number+1};
 
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
		window.location.href="${pageLib.ctx}/webpage/apa/sensitive-domain";
	});
	
	//导出
	$("#export").click(function(){
		var startTime = $("#searchForm #startTimeForSearch").val();
		var endTime = $("#searchForm #endTimeForSearch").val();
		var domainLike = $("#searchForm #domainLikeForSearch").val();
		window.location.href="${pageLib.ctx}/webpage/apa/sensitive-domain/export?startTime="+startTime
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
	
	// 添加
    $("#createSensitiveDomainModalForm").on("submit",function(){
    	var category = 1;
        var domainNames = $('#createSensitiveDomainModalForm #domainNames').val();
        
        if(domainNames.length == 0 ){
        	new $.zui.Messager('域名为必填项', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
        	return false;
        }
        
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/sensitive-domains', 
            data: {
            	'category' : category,
                'domainNames': domainNames
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/sensitive-domain?"+currentQueryParams;
            }
        });
        return false; 
	});
  });
  
  function editData(id,e){
    $.ajax({
		type: 'GET',
		async: false,
		url: '${pageLib.ctx}/webapi/apa/v1/sensitive-domains/'+id,
		cache: false,
		success: function(data) {
			$('#editSensitiveDomainModalForm #domainName').val(data.domainName);
        }
	});
		
    // 修改
    $("#editSensitiveDomainModalForm").on("submit",function(){
        var domainName = $('#editSensitiveDomainModalForm #domainName').val();
        
        if(domainName.length == 0 ){
        	new $.zui.Messager('域名为必填项', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
        	return false;
        }
        
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/sensitive-domains/'+id, 
            data: {
                'domainName': domainName
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/sensitive-domain?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editSensitiveDomainModal').modal();
  }
  
  function deleteData(id){
    $("#deleteSensitiveDomainModalForm").on("submit",function(){
  	  var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/sensitive-domains/'+id,
          data: data,
          success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/sensitive-domain?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteSensitiveDomainModal').modal();
  }

  </script>
</@defaultLayout.layout>