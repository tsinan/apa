<#import "../common/pagelib.ftl" as pageLib>
<#import "../common/layout.ftl" as defaultLayout>
<#import "../common/pager.ftl" as defaultPager>
<#import "../common/dialog.ftl" as defaultDialog>
 
<#-- 调用布局指令 -->
<@defaultLayout.layout>
  <table class="table datatable">
    <thead>
      <tr>
        <th data-width="60"></th>
        <th data-width="70">phishId</th>
        <th data-width="70">仿冒目标</th>
        <th class="flex-col">URL</th>
        <th data-width="120" data-type="date">提交时间</th>
        <th data-width="120" data-type="date">确认时间</th>
        <th data-width="120" data-type="date">同步时间</th>
        <th data-width="120">操作</th>
      </tr>
    </thead>
    <tbody>
      <#list phishtankUrlPage.content as phishtankUrl>
      <tr>
        <td>${phishtankUrl_index+1}</td>
      	<td>${phishtankUrl.phishId}</td>
      	<td>${phishtankUrl.target}</td>
      	<td style="word-break:break-all;">${phishtankUrl.url}</td>
      	<td>${phishtankUrl.submissionTime}</td>
      	<td>${phishtankUrl.verificationTime}</td>
      	<td>${phishtankUrl.createTime}</td>
      	<td >
      	  <a href="javascript:;" onclick="window.open('${phishtankUrl.url}','_blank')"><span class="label label-badge label-danger">打开原地址</span></a> 
      	  <a href="javascript:;" onclick="window.open('${phishtankUrl.phishDetailUrl}','_blank')"><span class="label label-badge label-info">打开确认信息</span></a>
      	</td>
      </tr>
      </#list>
    </tbody>
  </table>
  
  <@defaultPager.pager pageable=phishtankUrlPage url=pageLib.ctx+'/webpage/apa/phishtank'></@defaultPager.pager>

  <script>
  
  $(function(){
    $('table.datatable').datatable({
    	checkable: false, 
    	sortable: true, 
    	fixedLeftWidth: 200, 
    	fixedRightWidth: 480
    	});
  });
  </script>
</@defaultLayout.layout>