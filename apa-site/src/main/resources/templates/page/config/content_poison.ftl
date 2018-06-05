<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>

<@defaultLayout.layout>

  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
      <button class="btn" type="button" data-toggle="modal" data-target="#createPoisonDomainModal" >添加</button>
    </div>
  </div>
  
  <#-- 添加毒丸域名对话框 -->
  <@defaultDialog.dialog id="createPoisonDomainModal" title="添加毒丸域名">
    <div class="form-group">
      <label for="domainName">域名</label>
      <input type="text" class="form-control" id="domainName" placeholder="例如：tencent.com">
    </div>
    <div class="form-group">
      <label for="name">网站名称</label>
      <input type="text" class="form-control" id="name" placeholder="例如：腾讯">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="description">描述信息</label>
      <textarea class="form-control" id="description" rows="3" placeholder="域名描述"></textarea>
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
        <th data-width="140">域名</th>
        <th>网站名称</th>
        <th>描述</th>
        <th data-width="140" data-type="date">添加时间</th>
        <th data-width="140">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list poisonDomainPage.content as poisonDomain>
      <tr>
        <td>${poisonDomain.domainName}</td>
      	<td>${poisonDomain.name}</td>
      	<td>${poisonDomain.description}</td>
      	<td>${poisonDomain.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="editData('${poisonDomain.id}','${poisonDomain.status}',event)">编辑</a>
      	  <#if poisonDomain.status = 0>
      	  <a href="javascript:;" onclick="changeStatus('${poisonDomain.id}','1');">停用</a>
      	  <#else>
      	  <a href="javascript:;" onclick="changeStatus('${poisonDomain.id}','0');">启用</a>
      	  </#if>
      	  <a href="javascript:;" onclick="deleteData('${poisonDomain.id}');">删除</a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  <@defaultPager.pager pageable=poisonDomainPage url=pageLib.ctx+'/webpage/apa/content-poison'></@defaultPager.pager>
  </div>
  
  <#-- 编辑毒丸域名对话框 -->
  <@defaultDialog.dialog id="editPoisonDomainModal" title="编辑毒丸域名">
    <div class="form-group">
      <label for="domainName">域名</label>
      <input type="text" class="form-control" id="domainName" placeholder="例如：tencent.com">
    </div>
    <div class="form-group">
      <label for="name">网站名称</label>
      <input type="text" class="form-control" id="name" placeholder="例如：腾讯">
    </div>
    <div class="form-group">
      <div class="checkbox">
        <label><input type="checkbox" id="status" checked="checked"> 启用</label>
      </div>
    </div>
    <div class="form-group">
      <label for="description">描述</label>
      <textarea class="form-control" id="description" rows="3" placeholder="域名描述"></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 删除前确认对话框 -->
  <@defaultDialog.confirmDialog id="deletePoisonDomainModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog> 
  
  <script>
  var curQueryParamsWithoutPage = '1=1';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${poisonDomainPage.number+1};
    
  $(function(){
    
    $('table.datatable').datatable({checkable: false, sortable: true});
    
    // 添加
    $("#createPoisonDomainModalForm").on("submit",function(){
    	var domainName = $('#createPoisonDomainModalForm #domainName').val();
        var name = $('#createPoisonDomainModalForm #name').val();
        var status = '0';
        if($('#createPoisonDomainModalForm #status')[0].checked != true){
        	status = '1';
        }
        var description = $('#createPoisonDomainModalForm #description').val();
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/poison-domains', 
            data: {
            	'domainName': domainName,
                'name': name,
                'status':status,
                'description': description
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/content-poison?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function editData(id,status,e){
      
    $('#editPoisonDomainModalForm #status')[0].checked = false;
    if(status == '0'){
    	$('#editPoisonDomainModalForm #status')[0].checked = true;
    }
    var domainName=$(e.target).closest("td").prev().prev().prev().prev().html();
    var name=$(e.target).closest("td").prev().prev().prev().html();
    var description=$(e.target).closest("td").prev().prev().html();
    $('#editPoisonDomainModalForm #domainName').val(domainName);
    $('#editPoisonDomainModalForm #name').val(name);
    $('#editPoisonDomainModalForm #description').val(description);
	
    // 修改
    $("#editPoisonDomainModalForm").on("submit",function(){
    	var domainName= $('#editPoisonDomainModalForm #domainName').val();
        var name = $('#editPoisonDomainModalForm #name').val();
        var status = '0';
        if($('#editPoisonDomainModalForm #status')[0].checked != true){
        	status = '1';
        }
        var description = $('#editPoisonDomainModalForm #description').val();
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/poison-domains/'+id, 
            data: {
                'domainName': domainName,
                'name': name,
                'status':status,
                'description': description
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/content-poison?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editPoisonDomainModal').modal();
  }
  
  function changeStatus(id,status){
	var data = "status="+status;
    $.ajax({
        type: 'PUT',
        url: '${pageLib.ctx}/webapi/apa/v1/poison-domains/'+id+'/status',
        data: data,
        success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/content-poison?"+currentQueryParams;
   		}
	});
  }
  
  function deleteData(id){
    $("#deletePoisonDomainModalForm").on("submit",function(){
  	  var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/poison-domains/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/content-poison?"+currentQueryParams;
   		  }
      });
    });
    $('#deletePoisonDomainModal').modal();
  }
  </script>
 
</@defaultLayout.layout>