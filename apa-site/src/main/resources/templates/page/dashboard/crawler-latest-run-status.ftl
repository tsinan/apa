  <table class="table">

    <tbody>
      <tr>
        <td width="50%">起止时间</td>
        <td>${runStatusMap["minCreateTime"]!''} - ${runStatusMap["maxCreateTime"]!''} </td>
      </tr>
      
      <tr>
        <td>外链/非本站外链/可信站点外链总数量</td>
        <td>${(runStatusMap["pageOutlinkNum"]!'0')?number?string(",###")} / 
        	${(runStatusMap["pageOutsitelinkNum"]!'0')?number?string(",###")} / 
        	${(runStatusMap["pageTrustlinkNum"]!'0')?number?string(",###")}</td>
      </tr>
      <tr>
        <td>爬取尝试/成功HTML/非HTML总数量</td>
        <td>${(runStatusMap["pageShouldVisitNum"]!'0')?number?string(",###")} / 
        	${(runStatusMap["pageVisitNum"]!'0')?number?string(",###")} / 
        	${(runStatusMap["nohtmlVisitNum"]!'0')?number?string(",###")}</td>
      </tr>
      <tr>
        <td>爬取成功HTML/非HTML总数据量</td>
        <td>${(runStatusMap["pageVisitSize"]!'0')?number?string(",###")} / 
        	${(runStatusMap["nohtmlVisitSize"]!'0')?number?string(",###")}</td>
      </tr>
	       
    </tbody>
  </table>