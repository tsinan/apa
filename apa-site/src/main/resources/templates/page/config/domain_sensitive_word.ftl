<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>
 
<#-- 调用布局指令 -->
<@defaultLayout.layout>
  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
      <button class="btn" type="button" data-toggle="modal" data-target="#createSensitiveWordModal" >添加</button>
    </div>
  </div>
  
  <#-- 添加对话框 -->
  <@defaultDialog.dialog id="createSensitiveWordModal" title="添加域名敏感词">
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
      <label for="word">敏感词</label>
      <textarea class="form-control" id="word" rows="3" placeholder="每个敏感词占用一行"></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 数据表格 -->
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="300">名称</th>
        <th>敏感词</th>
        <th data-width="140" data-type="date">添加时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list domainSensitiveWordPage.content as sensitiveWord>
      <tr>
      	<td>${sensitiveWord.name}</td>
      	<td><pre>${sensitiveWord.word}</pre></td>
      	<td>${sensitiveWord.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="editData('${sensitiveWord.id}','${sensitiveWord.status}',event)">编辑</a>
      	  <#if sensitiveWord.status = 0>
      	  <a href="javascript:;" onclick="changeStatus('${sensitiveWord.id}','1');">停用</a>
      	  <#else>
      	  <a href="javascript:;" onclick="changeStatus('${sensitiveWord.id}','0');">启用</a>
      	  </#if>
      	  <a href="javascript:;" onclick="deleteData('${sensitiveWord.id}');">删除</a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  <@defaultPager.pager pageable=domainSensitiveWordPage url=pageLib.ctx+'/webpage/apa/domain-sensitive-word'></@defaultPager.pager>
  
  <#-- 编辑对话框 -->
  <@defaultDialog.dialog id="editSensitiveWordModal" title="编辑域名敏感词">
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
      <label for="word">敏感词</label>
      <textarea class="form-control" id="word" rows="3" placeholder="每个敏感词占用一行"></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deleteSensitiveWordModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = '1=1';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${domainSensitiveWordPage.number+1};
    
  $(function(){
    
    $('table.datatable').datatable({checkable: false, sortable: true});
    
    // 添加
    $("#createSensitiveWordModalForm").on("submit",function(){
        var name = $('#createSensitiveWordModalForm #name').val();
        var status = '0';
        if($('#createSensitiveWordModalForm #status')[0].checked != true){
        	status = '1';
        }
        var word = $('#createSensitiveWordModalForm #word').val();
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/domain-sensitive-words', 
            data: {
                'name': name,
                'status':status,
                'word': word
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/domain-sensitive-word?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function editData(id,status,e){
      
    $('#editSensitiveWordModalForm #status')[0].checked = false;
    if(status == '0'){
    	$('#editSensitiveWordModalForm #status')[0].checked = true;
    }
    var name=$(e.target).closest("td").prev().prev().prev().html();
    var word=$(e.target).closest("td").prev().prev().children().html();
    $('#editSensitiveWordModalForm #name').val(name);
    $('#editSensitiveWordModalForm #word').val(word);
	
    // 修改
    $("#editSensitiveWordModalForm").on("submit",function(){
        var name = $('#editSensitiveWordModalForm #name').val();
        var status = '0';
        if($('#editSensitiveWordModalForm #status')[0].checked != true){
        	status = '1';
        }
        var word = $('#editSensitiveWordModalForm #word').val();
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/domain-sensitive-words/'+id, 
            data: {
                'name': name,
                'status':status,
                'word': word
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/domain-sensitive-word?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editSensitiveWordModal').modal();
  }
  
  function changeStatus(id,status){
	var data = "status="+status;
    $.ajax({
        type: 'PUT',
        url: '${pageLib.ctx}/webapi/apa/v1/domain-sensitive-words/'+id+'/status',
        data: data,
        success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/domain-sensitive-word?"+currentQueryParams;
   		}
	});
  }
  
  function deleteData(id){
  
    $("#deleteSensitiveWordModalForm").on("submit",function(){
      var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/domain-sensitive-words/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/domain-sensitive-word?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteSensitiveWordModal').modal();
  }
  </script>
  
</@defaultLayout.layout>