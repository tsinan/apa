  <table class="table">

    <tbody>
	  <#list runStatusList as runStatus>
      <tr>
        <td>
        <#if runStatus.dataName = 'latest_sync_time'>
        最近同步成功时间
        <#elseif runStatus.dataName = 'latest_submission_time'>
        最近发现时间
        <#elseif runStatus.dataName = 'phishtank_url_count'>
        收集phishing url数量
        </#if>
        </td>
        <td>${runStatus.dataValue?default('')}</td>
      </tr>
	  </#list>      
    </tbody>
  </table>