  <table class="table">

    <tbody>
	  <#list rawDomainCountList as rawDomainCount>
      <tr>
      	<td>${(rawDomainCount.registrationDate?date('yyyy-MM-dd'))!''}</td>
        <td>${rawDomainCount.dataCount}</td>
      </tr>
	  </#list>      
    </tbody>
  </table>