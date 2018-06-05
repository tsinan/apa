<#macro dialog id title hasSubmit=true>
  <div class="modal fade" id="${id}">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
          <h4 class="modal-title">${title}</h4>
        </div>
        <form id="${id}Form">
        <div class="modal-body">
		  <#nested>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
          <#if hasSubmit >
          <button type="submit" class="btn btn-primary">保存</button>
          </#if>
        </div>
		</form>
      </div>
    </div>
  </div>
</#macro>

<#macro confirmDialog id>
  <div class="modal fade" id="${id}">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
          <h4 class="modal-title">确认提醒</h4>
        </div>
        <form id="${id}Form">
        <div class="modal-body">
		  <#nested>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          <button type="submit" class="btn btn-primary">确认</button>
        </div>
		</form>
      </div>
    </div>
  </div>
</#macro>
