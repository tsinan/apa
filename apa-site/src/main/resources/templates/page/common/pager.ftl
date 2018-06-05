<#macro pager pageable url>
  <#-- 基本分页参数 -->
  <#assign total=pageable.totalElements>
  <#assign pageSize=pageable.size>
  <#assign pageNumber=pageable.number+1>
    
  <#-- 计算总页数 -->
  <#if total % pageSize = 0>
  <#assign totalPage=total/pageSize>
  <#else>
  <#assign totalPage=(total-total%pageSize)/pageSize+1>
  </#if>
  
  <#-- 计算当前显示的起止页号 -->
  <#if pageNumber gte 3>
    <#assign firstIndex = pageNumber-2>
    <#else>
    <#assign firstIndex = 1>
    </#if>
    <#if totalPage gte pageNumber+2>
    <#assign endIndex = pageNumber+2>
    <#else>
    <#assign endIndex = totalPage>
  </#if>
  
  <#-- 分页地址 -->
  <#if url?contains("?")>
  <#assign pageParam="&page.page">
  <#else>
  <#assign pageParam="?page.page">
  </#if>
  
  <#-- 分页条 -->
  <ul class="pager" style="margin-top:-10px">
    <#-- 显示首页和上一页 -->
    <#if pageNumber gt 1>
    <li class="first"><a href="${url}${pageParam}=1&page.size=${pageSize}">首页</a></li>
    <#else>
    <li class="first disabled"><a href="">首页</a></li>
    </#if>
    
  	<#if pageNumber gt 1 >
    <li class="previous"><a href="${url}${pageParam}=${pageNumber-1}&page.size=${pageSize}">上一页</a></li>
    <#if firstIndex gt 3>
    <li><a href="${url}${pageParam}=${firstIndex-3}&page.size=${pageSize}">...</a></li>
    </#if>
    <#else>
    <li class="previous disabled"><a href="">上一页</a></li>
    </#if>
    
    <#-- 显示当前页号 -->
    <#list firstIndex..endIndex as i>
    <#if i = pageNumber>
    <li class="active"><a href="${url}${pageParam}=${i}&page.size=${pageSize}">${i}</a></li>
    <#elseif i gt 0>
    <li><a href="${url}${pageParam}=${i}&page.size=${pageSize}">${i}</a></li>
    </#if>
    </#list>
    
    <#-- 显示下一页和尾页 -->
    <#if pageNumber lt totalPage>
    <#if endIndex+3 lt totalPage>
    <li><a href="${url}${pageParam}=${endIndex+3}&page.size=${pageSize}">...</a></li>
    </#if>
    <li class="next"><a href="${url}${pageParam}=${pageNumber+1}&page.size=${pageSize}">下一页</a></li>
    <#else>
    <li class="next disabled"><a href="">下一页</a></li>
    </#if>
    
    <#if pageNumber lt totalPage>
    <li class="last"><a href="${url}${pageParam}=${totalPage}&page.size=${pageSize}">末页</a></li>
    <#else>
    <li class="last disabled"><a href="">末页</a></li>
    </#if>
    
    <li>
      <span><input type="text" value="${pageNumber}" onchange="window.location.href='${url}${pageParam}='+$(this).val()+'&page.size=${pageSize}'" />
      <a href="javascript:;" onclick="window.location.href='${url}${pageParam}='+$(this).prev().val()+'&page.size=${pageSize}'" style="text-decoration: none;">Go</a>
      </span>
    </li>
    
    <#if total gt 0>
    <li>
      <span>
      <select onchange="window.location.href='${url}${pageParam}=${pageNumber}&page.size='+$(this).val()">
      	<option value="30" <#if pageSize = 30>selected="selected"</#if>>30条/页</option>
        <option value="50" <#if pageSize = 50>selected="selected"</#if>>50条/页</option>
        <option value="100" <#if pageSize = 100>selected="selected"</#if>>100条/页</option>
        <option value="500" <#if pageSize = 500>selected="selected"</#if>>500条/页</option>
        <option value="1000" <#if pageSize = 1000>selected="selected"</#if>>1000条/页</option>
      </select>
      共${totalPage}页（${total}条记录）</span>
    </li>
    </#if>
  </ul>

</#macro>