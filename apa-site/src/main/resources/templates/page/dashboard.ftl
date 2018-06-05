<#import "common/pagelib.ftl" as pageLib>
<#import "common/layout.ftl" as defaultLayout>
<#import "common/pager.ftl" as defaultPager>

<@defaultLayout.layout>
  
  <div id="dashboard" class="dashboard dashboard-draggable" data-height="260">
  <section class="row">
    <div class="col-md-4 col-sm-6">
      <div class="panel" data-id="11" data-url="${pageLib.ctx}/webpage/apa/dashboard/data-count">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">数据处理状态一览</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    
    <div class="col-md-4 col-sm-6">
      <div class="panel" data-id="12" data-url="${pageLib.ctx}/webpage/apa/dashboard/crawler-run-status">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">爬虫运行状态</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    
    <div class="col-md-4 col-sm-6">
      <div class="panel" data-id="13" data-url="${pageLib.ctx}/webpage/apa/dashboard/raw-domain-count">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">注册域名统计</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    
    
    <div class="col-sm-6">
      <div class="panel" data-id="21" data-url="${pageLib.ctx}/webpage/apa/dashboard/crawler-latest-run-status?day=yesterday">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">昨日爬虫工作状态</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    
    <div class="col-sm-6">
      <div class="panel" data-id="21" data-url="${pageLib.ctx}/webpage/apa/dashboard/crawler-latest-run-status?day=today">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">今日爬虫工作状态</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    
    <#--
    <div class="col-sm-6">
      <div class="panel" data-id="22" data-url="${pageLib.ctx}/webpage/apa/dashboard/recent-found-suspect-url">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">最新发现的可疑URL</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    
    <div class="col-sm-6">
      <div class="panel" data-id="29" data-url="${pageLib.ctx}/webpage/apa/dashboard/recent-found-suspect-domain">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">最新发现的可疑域名</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    
    <div class="col-md-4 col-sm-6">
      <div class="panel" data-id="19" data-url="${pageLib.ctx}/webpage/apa/dashboard/phishtank-run-status">
        <div class="panel-heading">
          <i class="icon icon-list"></i>
          <span class="title">phishtank运行状态</span>
          <div class="panel-actions">
            <button type="button" class="btn refresh-panel" data-toggle="tooltip" title="重新获取内容"><i class="icon-refresh"></i></button>
          </div>
        </div>
        <div class="panel-body">
          正在加载内容...
        </div>
      </div>
    </div>
    -->
  </section>
  </div>
  
  <script>
  
  $(function(){
    $('#dashboard').dashboard({draggable: true});
  });
  </script>
</@defaultLayout.layout>