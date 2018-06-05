<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>
 
<#-- 调用布局指令 -->
<@defaultLayout.layout>
  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
      <button class="btn" type="button" data-toggle="modal" data-target="#createThreatActorModal" >添加</button>
    </div>
  </div>
  
  <#-- 添加对话框 -->
  <@defaultDialog.dialog id="createThreatActorModal" title="添加Threat Actor">
    <div class="form-group">
      <label for="ipAddress">主机IP地址</label>
      <input type="text" class="form-control" id="ipAddress" placeholder="例如：10.0.0.1">
    </div>
    <div class="form-group">
      <label for="registrantEmail">注册Email</label>
      <input type="text" class="form-control" id="registrantEmail" placeholder="例如：admin@host.com">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="description">描述信息</label>
      <textarea class="form-control" id="description" rows="3" placeholder="描述信息"></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 数据表格 -->
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="300">主机IP地址</th>
        <th data-width="300">注册Email</th>
        <th>描述信息</th>
        <th data-width="140" data-type="date">添加时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list threatActorPage.content as threatActor>
      <tr>
      	<td>${threatActor.ipAddress}</td>
      	<td>${threatActor.registrantEmail}</td>
      	<td>${threatActor.description}</td>
      	<td>${threatActor.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="editData('${threatActor.id}','${threatActor.status}',event)">编辑</a>
      	  <#if threatActor.status = 0>
      	  <a href="javascript:;" onclick="changeStatus('${threatActor.id}','1');">停用</a>
      	  <#else>
      	  <a href="javascript:;" onclick="changeStatus('${threatActor.id}','0');">启用</a>
      	  </#if>
      	  <a href="javascript:;" onclick="deleteData('${threatActor.id}');">删除</a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  <@defaultPager.pager pageable=threatActorPage url=pageLib.ctx+'/webpage/apa/domain-threat-actor'></@defaultPager.pager>
  
  <#-- 编辑对话框 -->
  <@defaultDialog.dialog id="editThreatActorModal" title="编辑Threat Actor">
    <div class="form-group">
      <label for="ipAddress">主机IP地址</label>
      <input type="text" class="form-control" id="ipAddress" placeholder="例如：10.0.0.1">
    </div>
    <div class="form-group">
      <label for="registrantEmail">注册Email</label>
      <input type="text" class="form-control" id="registrantEmail" placeholder="例如：admin@host.com">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="description">描述信息</label>
      <textarea class="form-control" id="description" rows="3" placeholder="描述信息"></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deleteThreatActorModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = '1=1';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${threatActorPage.number+1};
    
  $(function(){
    
    $('table.datatable').datatable({checkable: false, sortable: true});
    
    // 添加
    $("#createThreatActorModalForm").on("submit",function(){
        var ipAddress = $('#createThreatActorModalForm #ipAddress').val();
        var registrantEmail = $('#createThreatActorModalForm #registrantEmail').val();
        
        if(ipAddress.length == 0 && registrantEmail.length ==0){
        	new $.zui.Messager('主机IP地址或注册Email至少填写一项', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
        	return false;
        }
        
        var description = $('#createThreatActorModalForm #description').val();
        var status = '0';
        if($('#createThreatActorModalForm #status')[0].checked != true){
        	status = '1';
        }
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/domain-threat-actors', 
            data: {
                'ipAddress': ipAddress,
                'registrantEmail': registrantEmail,
                'description': description,
                'status':status
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/domain-threat-actor?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function editData(id,status,e){
      
    $('#editThreatActorModalForm #status')[0].checked = false;
    if(status == '0'){
    	$('#editThreatActorModalForm #status')[0].checked = true;
    }
    var ipAddress=$(e.target).closest("td").prev().prev().prev().prev().html();
    var registrantEmail=$(e.target).closest("td").prev().prev().prev().html();
    var description=$(e.target).closest("td").prev().prev().html();
    $('#editThreatActorModalForm #ipAddress').val(ipAddress);
    $('#editThreatActorModalForm #registrantEmail').val(registrantEmail);
    $('#editThreatActorModalForm #description').val(description);
	
    // 修改
    $("#editThreatActorModalForm").on("submit",function(){
        var ipAddress = $('#editThreatActorModalForm #ipAddress').val();
        var registrantEmail = $('#editThreatActorModalForm #registrantEmail').val();
        
        if(ipAddress.length == 0 && registrantEmail.length ==0){
        	new $.zui.Messager('主机IP地址或注册Email至少填写一项', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
        	return false;
        }
        
        var description = $('#editThreatActorModalForm #description').val();
        var status = '0';
        if($('#editThreatActorModalForm #status')[0].checked != true){
        	status = '1';
        }
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/domain-threat-actors/'+id, 
            data: {
                'ipAddress': ipAddress,
                'registrantEmail': registrantEmail,
                'description': description,
                'status': status
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/domain-threat-actor?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editThreatActorModal').modal();
  }
  
  function changeStatus(id,status){
	var data = "status="+status;
    $.ajax({
        type: 'PUT',
        url: '${pageLib.ctx}/webapi/apa/v1/domain-threat-actors/'+id+'/status',
        data: data,
        success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/domain-threat-actor?"+currentQueryParams;
   		}
	});
  }
  
  function deleteData(id){
  
    $("#deleteThreatActorModalForm").on("submit",function(){
      var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/domain-threat-actors/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/domain-threat-actor?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteThreatActorModal').modal();
  }
  </script>
  
</@defaultLayout.layout>