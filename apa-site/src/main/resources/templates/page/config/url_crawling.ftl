<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>

<@defaultLayout.layout>

  <#-- 功能bar -->
  <div id="featurebar">
  	<div class="actions">
      <button class="btn" type="button" data-toggle="modal" data-target="#createTrustDomainModal" >添加</button>
    </div>
  </div>
  
  <#-- 添加可信域名对话框 -->
  <@defaultDialog.dialog id="createTrustDomainModal" title="添加可信域名">
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
	      <div class="panel-actions" style="position: absolute;">
		    <a href="javascript:;" onclick="editUrlTemplate(event);" title="编辑"><i class="icon-pencil"></i></a>
		  </div>
	      <span class="panel-title">内容获取URL构造模板</span>
	    </div>
	    <div class="panel-body">
	      ${urlTemplate.content!''}
	    </div>
	    </div>
	  </div>
	  
	  <div style="position: relative;">
	    <div class="panel">
	    <div class="panel-heading" style="font-weight:bold">
	      <span class="panel-title">URL抓取次数排行</span>
	    </div>
	    <div class="panel-body">
	      <table class="table">
		    <tbody>
		      <#list urlVisitMetric as urlVisit>
		      <#if urlVisit_index gt 2 >
   				<#break>
 			  </#if>
		      <tr>
		        <td width="90%" style="word-break:break-all;">${urlVisit.value!''}</td>
		        <td>${urlVisit.score!0}</td>
		      </tr>
		      </#list>
		    </tbody>
		  </table>
	    </div>
	    </div>
  	  </div>
  	  
  	  <div style="position: relative;">
	    <div class="panel">
	    <div class="panel-heading" style="font-weight:bold">
	      <span class="panel-title">URL抓取数据量排行</span>
	    </div>
	    <div class="panel-body">
	      <table class="table">
		    <tbody>
		      <#list urlVisitSizeMetric as urlVisitSize>
		      <#if urlVisitSize_index gt 2 >
   				<#break>
 			  </#if>
		      <tr>
		        <td width="90%" style="word-break:break-all;">${urlVisitSize.value!''}</td>
		        <td>${urlVisitSize.score!0?string(',###')}</td>
		      </tr>
		      </#list>
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
      <#list trustDomainPage.content as trustDomain>
      <tr>
        <td>${trustDomain.domainName}</td>
      	<td>${trustDomain.name}</td>
      	<td>${trustDomain.description}</td>
      	<td>${trustDomain.createTime}</td>
      	<td>
      	  <a href="javascript:;" onclick="editData('${trustDomain.id}','${trustDomain.status}',event)">编辑</a>
      	  <#if trustDomain.status = 0>
      	  <a href="javascript:;" onclick="changeStatus('${trustDomain.id}','1');">停用</a>
      	  <#else>
      	  <a href="javascript:;" onclick="changeStatus('${trustDomain.id}','0');">启用</a>
      	  </#if>
      	  <a href="javascript:;" onclick="deleteData('${trustDomain.id}');">删除</a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  <@defaultPager.pager pageable=trustDomainPage url=pageLib.ctx+'/webpage/apa/url-crawling'></@defaultPager.pager>
  </div>
  
  <#-- 编辑URL模板对话框 -->
  <@defaultDialog.dialog id="editUrlTemplateModal" title="编辑内容获取URL构造模板">
    <div class="form-group">
      <label for="content">内容</label>
      <textarea class="form-control" id="content" rows="3" placeholder="每条URL构造模板占用一行，注释行以#开头"></textarea>
    </div>
  </@defaultDialog.dialog>
  
  <#-- 编辑可信域名对话框 -->
  <@defaultDialog.dialog id="editTrustDomainModal" title="编辑可信域名">
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
  <@defaultDialog.confirmDialog id="deleteTrustDomainModal">
        <p>是否确认删除该数据？</p>
  </@defaultDialog.confirmDialog>  
  
  <script>
  var curQueryParamsWithoutPage = '1=1';
  var currentQueryParams = curQueryParamsWithoutPage + "&page.page="+${trustDomainPage.number+1};
    
  $(function(){
    
    $('table.datatable').datatable({checkable: false, sortable: true});
    
    // 添加
    $("#createTrustDomainModalForm").on("submit",function(){
    	var domainName = $('#createTrustDomainModalForm #domainName').val();
        var name = $('#createTrustDomainModalForm #name').val();
        var status = '0';
        if($('#createTrustDomainModalForm #status')[0].checked != true){
        	status = '1';
        }
        var description = $('#createTrustDomainModalForm #description').val();
  		$(this).ajaxSubmit({
            type: 'post', 
            url: '${pageLib.ctx}/webapi/apa/v1/trust-domains', 
            data: {
            	'domainName': domainName,
                'name': name,
                'status':status,
                'description': description
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/url-crawling?"+currentQueryParams;
            }
        });
        return false; 
	});
	
  });
  
  function editUrlTemplate(e){
    var content=$(e.target).parents(".panel-heading").next().html().trim();
    $('#editUrlTemplateModalForm #content').val(content);
    
     // 修改
    $("#editUrlTemplateModalForm").on("submit",function(){
        
        var content = $('#editUrlTemplateModalForm #content').val();
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/clue-url-templates', 
            data: {
                'content': content
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/url-crawling?"+currentQueryParams;
            }
        });
        return false; 
	});
    
    $('#editUrlTemplateModal').modal();
  }
  
  function editData(id,status,e){
      
    $('#editTrustDomainModalForm #status')[0].checked = false;
    if(status == '0'){
    	$('#editTrustDomainModalForm #status')[0].checked = true;
    }
    var domainName=$(e.target).closest("td").prev().prev().prev().prev().html();
    var name=$(e.target).closest("td").prev().prev().prev().html();
    var description=$(e.target).closest("td").prev().prev().html();
    $('#editTrustDomainModalForm #domainName').val(domainName);
    $('#editTrustDomainModalForm #name').val(name);
    $('#editTrustDomainModalForm #description').val(description);
	
    // 修改
    $("#editTrustDomainModalForm").on("submit",function(){
    	var domainName= $('#editTrustDomainModalForm #domainName').val();
        var name = $('#editTrustDomainModalForm #name').val();
        var status = '0';
        if($('#editTrustDomainModalForm #status')[0].checked != true){
        	status = '1';
        }
        var description = $('#editTrustDomainModalForm #description').val();
  		$(this).ajaxSubmit({
            type: 'put', 
            url: '${pageLib.ctx}/webapi/apa/v1/trust-domains/'+id, 
            data: {
                'domainName': domainName,
                'name': name,
                'status':status,
                'description': description
            },
            success: function(data) { 
                window.location.href="${pageLib.ctx}/webpage/apa/url-crawling?"+currentQueryParams;
            }
        });
        return false; 
	});
	
	$('#editTrustDomainModal').modal();
  }
  
  function changeStatus(id,status){
	var data = "status="+status;
    $.ajax({
        type: 'PUT',
        url: '${pageLib.ctx}/webapi/apa/v1/trust-domains/'+id+'/status',
        data: data,
        success: function(msg){
			window.location.href="${pageLib.ctx}/webpage/apa/url-crawling?"+currentQueryParams;
   		}
	});
  }
  
  function deleteData(id){
    $("#deleteTrustDomainModalForm").on("submit",function(){
  	  var data = "";
      $.ajax({
          type: 'DELETE',
          url: '${pageLib.ctx}/webapi/apa/v1/trust-domains/'+id,
          data: data,
          success: function(msg){
			  window.location.href="${pageLib.ctx}/webpage/apa/url-crawling?"+currentQueryParams;
   		  }
      });
    });
    $('#deleteTrustDomainModal').modal();
  }
  </script>
 
</@defaultLayout.layout>