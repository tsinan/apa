<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>

<@defaultLayout.layout>

  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
      <button class="btn" type="button" data-toggle="modal" data-target="#createTagModal" >添加</button>
    </div>
  </div>
  
  <#-- 添加标签对话框 -->
  <@defaultDialog.dialog id="createTagModal" title="添加标签以及识别规则">
    <div class="form-group">
      <label for="name">名称</label>
      <input type="text" class="form-control" id="name" placeholder="标签名称/客户名称">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="rules">规则</label>
      <textarea class="form-control" id="rules" rows="3" placeholder="每条规则占用一行，规则支持lucene查询语法，多条规则之间是OR的关系"></textarea>
    </div>
  </@defaultDialog.dialog>

  <div class="side ">
    <div class="row">
      <div style="position: relative;">
	    <div class="panel">
	    <div class="panel-heading" style="font-weight:bold">
	      <span class="panel-title">内容过滤统计</span>
	    </div>
	    <div class="panel-body">
	       <table class="table">
		    <tbody>
		      <tr>
		        <td width="35%">毒丸外链</td>
		        <td>[毒] ${(metric['PoisonDomainMatchStrategy_Hit']!'0')?number?string(",###")} / 
		        	${(metric['PoisonDomainMatchStrategy_Pass']!'0')?number?string(",###")}</td>
		      </tr>
		      <tr>
		        <td>外链数量超限</td>
		        <td>[页] ${(metric['OutlinkNumMatchStrategy_Outlink']!'0')?number?string(",###")} / 
		        	[图] ${(metric['OutlinkNumMatchStrategy_Image']!'0')?number?string(",###")} / 
		        	${(metric['OutlinkNumMatchStrategy_Pass']!'0')?number?string(",###")}</td>
		      </tr>
		    </tbody>
		  </table>
	    </div>
	    </div>
	  </div>
	  
	  <div style="position: relative;">
	    <div class="panel">
	    <div class="panel-heading" style="font-weight:bold">
	      <span class="panel-title">内容过滤统计</span>
	    </div>
	    <div class="panel-body">
	      <table class="table">
		    <tbody>
		      <tr>
		        <td width="35%">内容非汉字</td>
		        <td>[H] ${(metric['TextMatchStrategy_MustHaveChinese_Hit']!'0')?number?string(",###")}</td>
		      </tr>
		      <tr>
		        <td>关键字命中</td>
		        <td>[H] ${(metric['TextMatchStrategy_Tag_Hit']!'0')?number?string(",###")}</td>
		      </tr>
		      <tr>
		        <td>内容未命中</td>
		        <td>[M] ${(metric['TextMatchStrategy_Miss']!'0')?number?string(",###")}</td>
		      </tr>
		      <tr>
		        <td>规则命中</td>
		        <td>[H] ${(metric['TextMatchStrategy_TagRule_Hit']!'0')?number?string(",###")} / 
		        	[M] ${(metric['TextMatchStrategy_TagRule_Miss']!'0')?number?string(",###")}</td>
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
        <th data-width="140">标签名称</th>
        <th>规则</th>
        <th data-width="140" data-type="date">添加时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list tagPage.content as tag>
      <tr>
      	<td>${tag.name}</td>
      	<td><pre>${tag.rules}</pre></td>
      	<td>${tag.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="editData('${tag.id}','${tag.status}',event)">编辑</a>
      	  <#if tag.status = 0>
      	  <a href="javascript:;" onclick="changeStatus('${tag.id}','1');">停用</a>
      	  <#else>
      	  <a href="javascript:;" onclick="changeStatus('${tag.id}','0');">启用</a>
      	  </#if>
      	  <a href="javascript:;" onclick="deleteData('${tag.id}');">删除</a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  <@defaultPager.pager pageable=tagPage url=pageLib.ctx+'/webpage/apa/content-keyword'></@defaultPager.pager>
  </div>
  
  <#-- 编辑标签对话框 -->
  <@defaultDialog.dialog id="editTagModal" title="编辑标签标签以及识别规则">
    <div class="form-group">
      <label for="name">名称</label>
      <input type="text" class="form-control" id="name" placeholder="标签名称/客户名称">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="rules">规则</label>
      <textarea class="form-control" id="rules" rows="3" placeholder="每条规则占用一行，规则支持lucene查询语法，多条规则之间是OR的关系"></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deleteTagModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = '1=1';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${tagPage.number+1};
    
  $(function(){
    
    $('table.datatable').datatable({checkable: false, sortable: true});
    
    // 添加
    $("#createTagModalForm").on("submit",function(){
        var name = $('#createTagModalForm #name').val();
        var status = '0';
        if($('#createTagModalForm #status')[0].checked != true){
        	status = '1';
        }
        var rules = $('#createTagModalForm #rules').val();
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/tags', 
            data: {
                'name': name,
                'status':status,
                'rules': rules
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/content-keyword?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function editData(id,status,e){
      
    $('#editTagModalForm #status')[0].checked = false;
    if(status == '0'){
    	$('#editTagModalForm #status')[0].checked = true;
    }
    var name=$(e.target).closest("td").prev().prev().prev().html();
    var rules=$(e.target).closest("td").prev().prev().children().html();
    $('#editTagModalForm #name').val(name);
    $('#editTagModalForm #rules').val(rules);
	
    // 修改
    $("#editTagModalForm").on("submit",function(){
        var name = $('#editTagModalForm #name').val();
        var status = '0';
        if($('#editTagModalForm #status')[0].checked != true){
        	status = '1';
        }
        var rules = $('#editTagModalForm #rules').val();
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/tags/'+id, 
            data: {
                'name': name,
                'status':status,
                'rules': rules
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/content-keyword?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editTagModal').modal();
  }
  
  function changeStatus(id,status){
	var data = "status="+status;
    $.ajax({
        type: 'PUT',
        url: '${pageLib.ctx}/webapi/apa/v1/tags/'+id+'/status',
        data: data,
        success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/content-keyword?"+currentQueryParams;
   		}
	});
  }
  
  function deleteData(id){
    $("#deleteTagModalForm").on("submit",function(){
  	  var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/tags/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/content-keyword?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteTagModal').modal();
  }
  </script>
 
</@defaultLayout.layout>