<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>

<@defaultLayout.layout>

  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
      <button class="btn" type="button" data-toggle="modal" data-target="#createBrandModal" >添加</button>
    </div>
  </div>
  
  <#-- 添加客户品牌对话框 -->
  <@defaultDialog.dialog id="createBrandModal" title="添加客户品牌">
    <div class="form-group">
      <label for="name">名称</label>
      <input type="text" class="form-control" id="name" placeholder="品牌名称">
    </div>
    <div class="form-group">
       	<select id="category" class="form-control">
			<option value="0">客户</option>
			<option value="1">非客户</option>
			<option value="2">潜在客户</option>
		</select>
    </div>
    <div class="form-group">
      <label for="description">描述</label>
      <textarea class="form-control" id="description" rows="3" placeholder=""></textarea>
    </div>
  </@defaultDialog.dialog>

  <div class="side ">
    <div class="row">
      <div style="position: relative;">
	    <div class="panel">
	    <div class="panel-heading" style="font-weight:bold">
	      <span class="panel-title">巡检频率</span>
	    </div>
	    <div class="panel-body">
	       <table class="table">
		    <tbody>
		      <tr>
		        <td>客户品牌active URL每五分钟检查一次</td>
		      </tr>
		      <tr>
		        <td>客户品牌inactive URL每X分钟检查一次</td>
		      </tr>
		      <tr>
		        <td>非客户品牌active URL每Y天检查一次</td>
		      </tr>
		      <tr>
		        <td>非客户品牌inactive URL每周检查一次</td>
		      </tr>
		    </tbody>
		  </table>
	    </div>
	    </div>
	  </div> 
    </div>
  </div>

  <div class="main">
  <#-- 数据表格 -->
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="200">品牌名称</th>
        <th data-width="80">是否客户</th>
        <th>描述</th>
        <th data-width="140" data-type="date">添加时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list brandPage.content as brand>
      <tr>
      	<td>${brand.name}</td>
      	<td>
      		<#if brand.category == 0 >
      		客户
      		<#elseif brand.category == 1>
      		非客户
      		<#elseif brand.category == 2 >
      		潜在客户
      		</#if>
      	</td>
      	<td>${brand.description}</td>
      	<td>${brand.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="editData('${brand.id}',event)">编辑</a>
      	  <a href="javascript:;" onclick="deleteData('${brand.id}');">删除</a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  <@defaultPager.pager pageable=brandPage url=pageLib.ctx+'/webpage/apa/brand'></@defaultPager.pager>
  </div>
  
  <#-- 编辑客户品牌对话框 -->
  <@defaultDialog.dialog id="editBrandModal" title="编辑客户品牌">
    <div class="form-group">
      <label for="name">名称</label>
      <input type="text" class="form-control" id="name" placeholder="品牌名称">
    </div>
    <div class="form-group">
       	<select id="category" class="form-control">
			<option value="0">客户</option>
			<option value="1">非客户</option>
			<option value="2">潜在客户</option>
		</select>
    </div>
    <div class="form-group">
      <label for="description">描述</label>
      <textarea class="form-control" id="description" rows="3" placeholder=""></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deleteBrandModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = '1=1';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${brandPage.number+1};
    
  $(function(){
    
    $('table.datatable').datatable({checkable: false, sortable: true});
    
    // 添加
    $("#createBrandModalForm").on("submit",function(){
        var name = $('#createBrandModalForm #name').val();
        var category = $('#createBrandModalForm #category').val();
        var description = $('#createBrandModalForm #description').val();
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/brands', 
            data: {
                'name': name,
                'category':category,
                'description': description
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/brand?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function editData(id,e){
  	
  	$.ajax({
		type: 'GET',
		async: false,
		url: '${pageLib.ctx}/webapi/apa/v1/brands/'+id,
		cache: false,
		success: function(data) {
			$('#editBrandModalForm #name').val(data.name);
    		$('#editBrandModalForm #category').val(data.category);
    		$('#editBrandModalForm #description').val(data.description);
        }
	});
	
	
    // 修改
    $("#editBrandModalForm").on("submit",function(){
        var name = $('#editBrandModalForm #name').val();
        var category = $('#editBrandModalForm #category').val();
        var description = $('#editBrandModalForm #description').val();
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/brands/'+id, 
            data: {
                'name': name,
                'category':category,
                'description': description
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/brand?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editBrandModal').modal();
  }
  
  function deleteData(id){
    $("#deleteBrandModalForm").on("submit",function(){
  	  var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/brands/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/brand?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteBrandModal').modal();
  }
  </script>
 
</@defaultLayout.layout>