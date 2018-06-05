  <table class="table">

    <tbody>
	  <#list recentFoundList as recentFound>
      <tr>
        <td width="60%" style="word-break:break-all;">${recentFound.url}</td>
        <td>
        <#if recentFound.verify = '0'>
        未确认
        <#else>
        已确认
        </#if>
        </td>
        <td>${recentFound.createTime}</td>
      </tr>
	  </#list>      
    </tbody>
  </table>