<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="seckillgoods/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="SECKILLGOODS_ID" id="SECKILLGOODS_ID" value="${pd.SECKILLGOODS_ID}"/>
						<input type="hidden" name="SECKILL_ID" id="SECKILL_ID" value="${pd.SECKILL_ID}"/>
						<input type="hidden" name="GOODS_ID" id="GOODS_ID" value="${pd.GOODS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">秒杀商品:</td>
								<td><input type="text" name="GOODS_NAME" id="GOODS_NAME" value="${pd.GOODS_NAME}" maxlength="100" placeholder="这里选择秒杀商品" title="秒杀商品" style="width:98%;" onclick="choiceGoods();"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品数量:</td>
								<td><input type="number" name="GOODS_NUM" id="GOODS_NUM" value="${pd.GOODS_NUM}" maxlength="32" placeholder="这里输入商品数量" title="商品数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
					</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());

		//选择秒杀商品
		function choiceGoods() {
			top.jzts();
			var diag = new top.Dialog();
			diag.Drag = true;
			diag.Title = "选择秒杀商品";
			diag.URL = '<%=basePath%>goods/goBox.do';
			diag.Width = 1000;
			diag.Height = 650;
			diag.Modal = true;				//有无遮罩窗口
			diag.ShowMaxButton = true;	//最大化按钮
			diag.ShowMinButton = true;		//最小化按钮
			diag.CancelEvent = function () { //关闭事件
				var tbodyWin = diag.innerFrame.contentWindow;
				var tbodyObj = tbodyWin.document.getElementById('simple-table');
				$(tbodyWin.$("table :checkbox")).each(function (key, value) {
					if (tbodyWin.$(value).prop('checked')) {
						$("#GOODS_ID").val(tbodyWin.$("#selectStr").val());
						$("#GOODS_NAME").val(tbodyObj.rows[key].cells[4].innerHTML);
						return false;
					}
				});
				diag.close();
			};
			diag.show();
		}

		//保存
		function save(){
			if($("#SECKILL_ID").val()==""){
				$("#SECKILL_ID").tips({
					side:3,
		            msg:'请输入秒杀活动',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SECKILL_ID").focus();
			return false;
			}
			if($("#GOODS_ID").val()==""){
				$("#GOODS_ID").tips({
					side:3,
		            msg:'请选择秒杀商品',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_ID").focus();
			return false;
			}
			if($("#GOODS_NUM").val()==""){
				$("#GOODS_NUM").tips({
					side:3,
		            msg:'请输入商品数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_NUM").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>