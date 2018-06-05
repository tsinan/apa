<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>
 
<#-- 调用布局指令 -->
<@defaultLayout.layout>
  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
      <button class="btn" type="button" data-toggle="modal" data-target="#createRegularModal" >添加</button>
    </div>
  </div>
  
  <#-- 添加对话框 -->
  <@defaultDialog.dialog id="createRegularModal" title="添加域名匹配正则表达式">
    <div class="form-group">
      <label for="name">名称</label>
      <input type="text" class="form-control" id="name" placeholder="名称">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="expression">表达式</label>
      <input type="text" class="form-control" id="expression" placeholder="请输入有效的正则表达式，注意：双反斜线=转义">
    </div>
  </@defaultDialog.dialog>
  
  <#-- 数据表格 -->
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="300">表达式名称</th>
        <th>规则</th>
        <th data-width="140" data-type="date">添加时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list domainRegularPage.content as regular>
      <tr>
      	<td>${regular.name}</td>
      	<td><pre>${regular.expression}</pre></td>
      	<td>${regular.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="editData('${regular.id}','${regular.status}',event)">编辑</a>
      	  <#if regular.status = 0>
      	  <a href="javascript:;" onclick="changeStatus('${regular.id}','1');">停用</a>
      	  <#else>
      	  <a href="javascript:;" onclick="changeStatus('${regular.id}','0');">启用</a>
      	  </#if>
      	  <a href="javascript:;" onclick="deleteData('${regular.id}');">删除</a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  <@defaultPager.pager pageable=domainRegularPage url=pageLib.ctx+'/webpage/apa/domain-regular'></@defaultPager.pager>
  
  <#-- 编辑对话框 -->
  <@defaultDialog.dialog id="editRegularModal" title="编辑域名匹配正则表达式">
    <div class="form-group">
      <label for="name">名称</label>
      <input type="text" class="form-control" id="name" placeholder="名称">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="expression">表达式</label>
      <input type="text" class="form-control" id="expression" placeholder="请输入有效的正则表达式，注意：双反斜线=转义">
    </div>
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deleteRegularModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = '1=1';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${domainRegularPage.number+1};
    
  $(function(){
    
    $('table.datatable').datatable({checkable: false, sortable: true});
    
    // 添加
    $("#createRegularModalForm").on("submit",function(){
        var name = $('#createRegularModalForm #name').val();
        var status = '0';
        if($('#createRegularModalForm #status')[0].checked != true){
        	status = '1';
        }
        var expression = $('#createRegularModalForm #expression').val();
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/domain-regulars', 
            data: {
                'name': name,
                'status':status,
                'expression': expression
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/domain-regular?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function editData(id,status,e){
      
    $('#editRegularModalForm #status')[0].checked = false;
    if(status == '0'){
    	$('#editRegularModalForm #status')[0].checked = true;
    }
    var name=$(e.target).closest("td").prev().prev().prev().html();
    var expression=$(e.target).closest("td").prev().prev().children().html();
    $('#editRegularModalForm #name').val(name);
    $('#editRegularModalForm #expression').val(expression);
	
    // 修改
    $("#editRegularModalForm").on("submit",function(){
        var name = $('#editRegularModalForm #name').val();
        var status = '0';
        if($('#editRegularModalForm #status')[0].checked != true){
        	status = '1';
        }
        var expression = $('#editRegularModalForm #expression').val();
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/domain-regulars/'+id, 
            data: {
                'name': name,
                'status':status,
                'expression': expression
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/domain-regular?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editRegularModal').modal();
  }
  
  function changeStatus(id,status){
	var data = "status="+status;
    $.ajax({
        type: 'PUT',
        url: '${pageLib.ctx}/webapi/apa/v1/domain-regulars/'+id+'/status',
        data: data,
        success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/domain-regular?"+currentQueryParams;
   		}
	});
  }
  
  function deleteData(id){
  
    $("#deleteRegularModalForm").on("submit",function(){
      var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/domain-regulars/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/domain-regular?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteRegularModal').modal();
  }
  </script>
  
</@defaultLayout.layout>