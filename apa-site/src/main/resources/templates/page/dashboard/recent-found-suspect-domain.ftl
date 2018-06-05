  <table class="table">

    <tbody>
	  <#list recentFoundList as recentFound>
      <tr>
        <td>${recentFound.domainName}</td>
        <td>${recentFound.createTime}</td>
      </tr>
	  </#list>      
    </tbody>
  </table>