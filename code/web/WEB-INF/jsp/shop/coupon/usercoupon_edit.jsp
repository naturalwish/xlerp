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
					
					<form action="usercoupon/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="USERCOUPON_ID" id="USERCOUPON_ID" value="${pd.USERCOUPON_ID}"/>
						<input type="hidden" name="USER_ID" id="USER_ID" value="${pd.USER_ID}"/>
						<input type="hidden" name="COUPON_ID" id="COUPON_ID" value="${pd.COUPON_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">优惠卷:</td>
								<td><input type="text" name="COUPON_NAME" id="COUPON_NAME" value="${pd.COUPON_NAME}" maxlength="100" placeholder="这里选择优惠卷" title="优惠卷" style="width:98%;" onclick="choiceCoupon();"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								<td>
									<select name="COUPON_STATE" title="优惠卷状态" style="width:98%;">
									<option value="0"
											<c:if test="${pd.COUPON_STATE == '0' }">selected</c:if> >正常
									</option>
									<option value="1"
											<c:if test="${pd.COUPON_STATE == '1' }">selected</c:if> >失效
									</option>
								</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">领用时间:</td>
								<td><input class="span10 date-picker" name="APPLY_TIME" id="APPLY_TIME" value="${pd.APPLY_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="领用时间" title="领用时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">使用时间:</td>
								<td><input class="span10 date-picker" name="USE_TIME" id="USE_TIME" value="${pd.USE_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="使用时间" title="使用时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="MEMO" id="MEMO" value="${pd.MEMO}" maxlength="255" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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

		//选择优惠券
		function choiceCoupon() {
			top.jzts();
			var diag = new top.Dialog();
			diag.Drag = true;
			diag.Title = "选择优惠券";
			diag.URL = '<%=basePath%>coupon/goBox.do';
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
						$("#COUPON_ID").val(tbodyWin.$("#selectStr").val());
						$("#COUPON_NAME").val(tbodyObj.rows[key].cells[2].innerHTML);
						return false;
					}
				});
				diag.close();
			};
			diag.show();
		}

		//保存
		function save(){
			if($("#COUPON_ID").val()==""){
				$("#COUPON_ID").tips({
					side:3,
		            msg:'请选择优惠卷',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COUPON_ID").focus();
			return false;
			}
			if($("#COUPON_STATE").val()==""){
				$("#COUPON_STATE").tips({
					side:3,
		            msg:'请输入优惠卷状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COUPON_STATE").focus();
			return false;
			}
			if($("#APPLY_TIME").val()==""){
				$("#APPLY_TIME").tips({
					side:3,
		            msg:'请输入领用时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#APPLY_TIME").focus();
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