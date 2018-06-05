  <table class="table">

    <tbody>
	  <#list runStatusList as runStatus>
      <tr>
        <td>
        <#if runStatus.progress = '0'>
        等待爬取的URL数量
        <#elseif runStatus.progress = '1'>
        爬取失败的URL数量
        <#elseif runStatus.progress = '2'>
        爬取成功的网页URL数量
        <#elseif runStatus.progress = '3'>
        爬取成功但内容不是HTML的URL数量
        <#elseif runStatus.progress = '4'>
        放弃爬取的URL数量
        <#elseif runStatus.progress = '5'>
        爬取异常失败的URL数量
        </#if>
        </td>
        <td>${runStatus.dataCount}</td>
      </tr>
	  </#list>      
    </tbody>
  </table>