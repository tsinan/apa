<#import "common/pagelib.ftl" as pageLib>
<#import "common/layout_window.ftl" as windowLayout>
<#import "common/pager.ftl" as defaultPager>
<#import "common/dialog.ftl" as defaultDialog>

<@windowLayout.layout>
  
  <div class="row-table">
    <div class="col-main">
      <div class="main">
        <iframe src="${suspectUrl.url}" width="100%" height="500px" 
        	 frameborder=0 style="border: 1px solid #ddd;">
        </iframe>
      </div>
    </div>
    <div class="col-side">
      <div class="main-side">
	      <ul class="nav nav-tabs">
		    <li class="active"><a data-tab href="#tabUrlInfo">嫌疑URL信息</a></li>
		    <li><a data-tab href="#tabMoreInfo">更多信息</a></li>
		  </ul>
		  <div class="tab-content">
		    <div class="tab-pane active" id="tabUrlInfo">
		      <table class="table table-borderless">
				<tr>
				  <th width="80px">URL</th>
				  <td style="word-break:break-all;">
				    ${suspectUrl.url}
				    <span class="label label-badge label-primary data-clipboard-span" data-clipboard-text="${suspectUrl.url}">复制</span>
				  </td>
				</tr>
				<tr>
				  <th>确认状态</th>
				  <td>
				    <#if suspectUrl.verify == 0 >
				    未确认
				    <#elseif suspectUrl.verify == 1>
				    已确认
				    <#elseif suspectUrl.verify == 2>
				    误判
				    </#if>
				  </td>
				</tr>
				<tr>
				  <th>发现日期</th>
				  <td>${suspectUrl.createTime}</td>
				</tr>
				<tr>
				  <th>内容识别标签</th>
				  <td>${suspectUrl.tagName}</td>
				</tr>
				<tr>
				  <th>内容识别规则</th>
				  <td>${suspectUrl.tagRule}</td>
				</tr>
				<tr>
				  <th>内容识别评分</th>
				  <td>${suspectUrl.tagRuleScore}</td>
				</tr>
				<tr>
				  <td colspan="2">
				    <div class="btn-group">
				      <#if suspectUrl.verify == 0>
					  <button class="btn btn-danger" onclick="changeSuspectUrlVerify(1);">确认并监控</button>
					  <button class="btn btn-success" onclick="changeSuspectUrlVerify(2);">误判</button>
					  <button class="btn btn-warning" onclick="window.open('${suspectUrl.url}','_blank');">打开原网址</button>
					  <#else>
					  <button class="btn btn-warning" onclick="window.open('${suspectUrl.url}','_blank');">打开原网址</button>
					  </#if>
					</div>
				  </td>
				</tr>
			  </table>
		    </div>
		    <div class="tab-pane" id="tabMoreInfo">
		      <p>更多信息。</p>
		    </div>
		  </div>
	  </div>
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
  
  <script>
  
  $(function(){
	// 初始化复制
	var clipboard = new Clipboard('.data-clipboard-span');
	clipboard.on('success',function(e){
		new $.zui.Messager('可疑URL已复制到粘贴板', {
                	type : 'success',
    				close: false,
    				time : 3000
				}).show();
	});
	
	// 添加巡检URL
    $("#createInspectUrlModalForm").on("submit",function(){
    	var category = 0;
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
            	var data = "verify=1";
			    $.ajax({
			        type: 'put', 
			        url: '${pageLib.ctx}/webapi/apa/v1/suspect-urls/${suspectUrl.id}/verify', 
			        data: data,
			        success: function(data) { 
		        		new $.zui.Messager('确认监控成功，3秒后自动关闭', {
		                	type : 'success',
		    				close: false,
		    				time : 3000
						}).show();
		            	
		            	setTimeout(function() {
			                window.opener = null;
			            	window.open('','_self');
			            	window.close();
			            	return;
						}, 3000);
			        }
			    }); 
            
            }
        });
        return false; 
	});
  });
  
  function changeSuspectUrlVerify(verify){
    if(verify == 1){
		$('#createInspectUrlModal #url').val('${suspectUrl.url}');
		$('#createInspectUrlModal #inspectKeyword').val('${suspectUrl.tagRule}');
		
		$('#createInspectUrlModal').on('hidden.zui.modal',function(){
			window.location.href="${pageLib.ctx}/webpage/apa/suspect-url/${suspectUrl.id}/confirm";
		});
		$('#createInspectUrlModal').modal('show', 'fit');
		return;
	}else if(verify == 2){
    	var data = "verify=2";
	    $.ajax({
	        type: 'put', 
	        url: '${pageLib.ctx}/webapi/apa/v1/suspect-urls/${suspectUrl.id}/verify', 
	        data: data,
	        success: function(data) { 
            	window.opener = null;
            	window.open('','_self');
            	window.close();
            	return;
	        }
	    });
    }
  
  }
  
  </script>
</@windowLayout.layout>