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
					
					<form action="coupongoods/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="COUPONGOODS_ID" id="COUPONGOODS_ID" value="${pd.COUPONGOODS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">优惠卷:</td>
								<td><input type="text" name="COUPON_ID" id="COUPON_ID" value="${pd.COUPON_ID}" maxlength="100" placeholder="这里输入优惠卷" title="优惠卷" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品ID:</td>
								<td><input type="text" name="GOODS_ID" id="GOODS_ID" value="${pd.GOODS_ID}" maxlength="100" placeholder="这里输入商品ID" title="商品ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">优惠价:</td>
								<td><input type="number" name="GOODS_PRICE" id="GOODS_PRICE" value="${pd.GOODS_PRICE}" maxlength="32" placeholder="这里输入优惠价" title="优惠价" style="width:98%;"/></td>
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
		//保存
		function save(){
			if($("#COUPON_ID").val()==""){
				$("#COUPON_ID").tips({
					side:3,
		            msg:'请输入优惠卷',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COUPON_ID").focus();
			return false;
			}
			if($("#GOODS_ID").val()==""){
				$("#GOODS_ID").tips({
					side:3,
		            msg:'请输入商品ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_ID").focus();
			return false;
			}
			if($("#GOODS_PRICE").val()==""){
				$("#GOODS_PRICE").tips({
					side:3,
		            msg:'请输入优惠价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_PRICE").focus();
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