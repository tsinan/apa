  <table class="table">

    <tbody>
	  <#list dataCountList as dataCount>
      <tr>
      	<td>
      	<#if dataCount.tableName = 'ld_raw_domain'>
        已录入注册域名数量
        <#elseif dataCount.tableName = 'sd_suspect_domain'>
        域名规则匹配命中的域名数量
        <#elseif dataCount.tableName = 'su_clue_url'>
        爬虫已抓取+正在抓取的URL总数量
        <#elseif dataCount.tableName = 'su_suspect_url'>
        网页内容匹配命中的URL数量
        </#if>
        </td>
        <td>${dataCount.dataCount}</td>
      </tr>
	  </#list>      
    </tbody>
  </table>