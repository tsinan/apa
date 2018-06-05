	<link rel="stylesheet" href="//cdn.bootcss.com/zui/1.7.0/css/zui.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/zui/1.7.0/lib/datatable/zui.datatable.min.css">
	<link rel="stylesheet" href="//cdn.bootcss.com/zui/1.7.0/lib/dashboard/zui.dashboard.min.css">
	<link rel="stylesheet" href="//cdn.bootcss.com/zui/1.7.0/lib/datetimepicker/datetimepicker.min.css">
    <script src="//cdn.bootcss.com/zui/1.7.0/lib/jquery/jquery.js"></script>
    <script src="//cdn.bootcss.com/jquery.form/4.2.2/jquery.form.min.js"></script>
	<script src="//cdn.bootcss.com/zui/1.7.0/js/zui.min.js"></script>
	<script src="//cdn.bootcss.com/zui/1.7.0/lib/datatable/zui.datatable.min.js"></script>
	<script src="//cdn.bootcss.com/zui/1.7.0/lib/dashboard/zui.dashboard.min.js"></script>
	<script src="//cdn.bootcss.com/clipboard.js/1.7.1/clipboard.min.js"></script>
	<script src="//cdn.bootcss.com/zui/1.7.0/lib/datetimepicker/datetimepicker.min.js"></script>
	<style>
	
	#header {
	  padding: 12px 20px 0;
	  background:#e5e5e5;
	}
	#modulemenu {
	  background:#e5e5e5;
	  margin:0 -20px;
	  padding:0 20px;
	}
	#wrap {
	  padding:0 19px 20px 19px;
	  background:#e5e5e5;
	  margin-top:-20px;
	}
	#wrap .outer {
	  position:relative;
	  text-align:left;
	  background:#fff;
	  box-shadow:0 1px 5px rgba(0,0,0,0.1);
	  padding:20px;
	  min-height:768px;
	}
	#featurebar {
	  margin:-20px -20px 20px;
	  padding:8px 10px 7px;
	  font-size:14px;
	  background:#f8fafe;
	  border-bottom:1px solid #ddd;
	  line-height:30px;
	  min-height:46px;
	}
	#featurebar .actions {
	  float:right;
	  margin-top:-2px;
	  margin-right:10px;
	}
	#wrap .outer .side {
	  width: 292px;
	  position: absolute;
	  left: 20px;
	}
	#wrap .outer .main{
	  margin-left: 312px;
	  margin-right: 0px;
	}
	#wrap .outer .main .table{
	  border: 1px solid #ddd;
	}
	.panel-actions {
      position: absolute;
      top: 10px;
      right: 10px;
      height: 30px;
	}
	.panel-actions > a {
      color: #666;
	}
	.pager > li > span > select {
      position: relative;
      float: left;
      margin-right: 5px;
      background-color: #fff;
      border: 1px solid #ddd;
	}
	.pager > li:last-child > select {
  	  border-top-right-radius: 4px;
  	  border-bottom-right-radius: 4px;
  	}
  	.pager > li > span > input {
      position: relative;
      float: left;
      width: 40px;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 11px;
      margin: -1px 10px;
      text-align: center;
	}
  	.data-clipboard-span {
      padding: 5px 8px;
      font-size: 12px;
      color: #767676;
      cursor: pointer;
      background-color: #fff;
      border: 1px solid #e1e1e8;
      border-radius: 0 4px 0 4px;
  	}
	</style>
	<script>
	  	//清除jquery的ajax的缓存(设置ajax不调用浏览器缓存.)
		$.ajaxSetup({
			cache:false,
			error:function(jqXHR, textStatus, errorThrown){
				new $.zui.Messager('发生了内部错误，请联系管理员', {
                	type : 'danger',
    				close: false,
    				time : 3000
				}).show();
			}
		});
		
		function extractAndDecodeParamValue(name) { 
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
			var r = decodeURI(window.location.search).substr(1).match(reg); 
			if (r != null) 
				return decodeURIComponent(r[2]);
			return null; 
		}
		
		function dateToStringYYYY_MM_DD_HH_MM_SS(val){
			if(val == null){
				return '';
			}
			var date = new Date(val);
			return date.getFullYear()+' '+(date.getMonth()+1)+'月'+date.getDate()+'日'+' '+date.getHours()+":"+date.getMinutes()+':'+date.getSeconds();
		}
	</script>
