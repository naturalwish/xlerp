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
					
					<form action="attribute/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ATTRIBUTE_ID" id="ATTRIBUTE_ID" value="${pd.ATTRIBUTE_ID}"/>
						<input type="hidden" name="GOODS_ID" id="ORDER_ID" value="${pd.GOODS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">属性:</td>
								<td><input type="text" name="ATTRIBUTE" id="ATTRIBUTE" value="${pd.ATTRIBUTE}" maxlength="255" placeholder="这里输入属性" title="属性" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">数量:</td>
								<td><input type="number" name="ATTRIBUTENUM" id="ATTRIBUTENUM" value="${pd.ATTRIBUTENUM}" maxlength="32" placeholder="这里输入数量" title="数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">单价:</td>
								<td><input type="number" name="ATTRIBUTEPRICE" id="ATTRIBUTEPRICE" value="${pd.ATTRIBUTEPRICE}" maxlength="32" placeholder="这里输入单价" title="单价" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">市场价格:</td>
								<td><input type="number" name="MARKETPRICE" id="MARKETPRICE" value="${pd.MARKETPRICE}" maxlength="32" placeholder="这里输入市场价格" title="市场价格" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">秒杀价格:</td>
								<td><input type="number" name="SECKILLPRICE" id="SECKILLPRICE" value="${pd.SECKILLPRICE}" maxlength="32" placeholder="这里输入秒杀价格" title="秒杀价格" style="width:98%;"/></td>
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
			if($("#GOODS_ID").val()==""){
				$("#GOODS_ID").tips({
					side:3,
		            msg:'请输入商品主键',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_ID").focus();
			return false;
			}
			if($("#ATTRIBUTE").val()==""){
				$("#ATTRIBUTE").tips({
					side:3,
		            msg:'请输入属性',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ATTRIBUTE").focus();
			return false;
			}
			if($("#ATTRIBUTENUM").val()==""){
				$("#ATTRIBUTENUM").tips({
					side:3,
		            msg:'请输入数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ATTRIBUTENUM").focus();
			return false;
			}
			if($("#ATTRIBUTEPRICE").val()==""){
				$("#ATTRIBUTEPRICE").tips({
					side:3,
		            msg:'请输入单价',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ATTRIBUTEPRICE").focus();
			return false;
			}
			if($("#MARKETPRICE").val()==""){
				$("#MARKETPRICE").tips({
					side:3,
		            msg:'请输入市场价格',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MARKETPRICE").focus();
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