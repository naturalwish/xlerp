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
					
					<form action="invoice/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="INVOICE_ID" id="INVOICE_ID" value="${pd.INVOICE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发货单号:</td>
								<td><input type="text" name="CODE" id="CODE" value="${pd.CODE}" disabled maxlength="255" placeholder="这里输入发货单号" title="发货单号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">订单号:</td>
								<td><input type="text" name="ORDER_ID" id="ORDER_ID" value="${pd.ORDER_ID}" disabled maxlength="255" placeholder="这里输入订单号" title="订单号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">下单时间:</td>
								<td><input class="span10 date-picker" name="ADDORDERTIME" id="ADDORDERTIME" value="${pd.ADDORDERTIME}" disabled type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="下单时间" title="下单时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收货人:</td>
								<td><input type="text" name="CONSIGNEE" id="CONSIGNEE" value="${pd.CONSIGNEE}" disabled maxlength="255" placeholder="这里输入收货人" title="收货人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发货时间:</td>
								<td><input class="span10 date-picker" name="INVOICETIME" id="INVOICETIME" value="${pd.INVOICETIME}" disabled type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="发货时间" title="发货时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发货单状态:</td>
								<td>
									<select class="chosen-select form-control" name="STATUS" id="STATUS" data-placeholder="请选择" style="vertical-align:top;width: 120px;">
										<option value="0" <c:if test="${0 == pd.STATUS }">selected</c:if>>已发货</option>
										<option value="1" <c:if test="${1 == pd.STATUS }">selected</c:if>>已关闭</option>
										<option value="2" <c:if test="${2 == pd.STATUS }">selected</c:if>>已完成</option>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">操作人:</td>
								<td><input type="text" name="OPERATOR" id="OPERATOR" value="${pd.OPERATOR}" disabled maxlength="255" placeholder="这里输入操作人" title="操作人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">操作时间:</td>
								<td><input class="span10 date-picker" name="OPERATIONTIME" id="OPERATIONTIME" value="${pd.OPERATIONTIME}" disabled type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="操作时间" title="操作时间" style="width:98%;"/></td>
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
			if($("#CODE").val()==""){
				$("#CODE").tips({
					side:3,
		            msg:'请输入发货单号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CODE").focus();
			return false;
			}
			if($("#ORDER_ID").val()==""){
				$("#ORDER_ID").tips({
					side:3,
		            msg:'请输入订单号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ORDER_ID").focus();
			return false;
			}
			if($("#ADDORDERTIME").val()==""){
				$("#ADDORDERTIME").tips({
					side:3,
		            msg:'请输入下单时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ADDORDERTIME").focus();
			return false;
			}
			if($("#CONSIGNEE").val()==""){
				$("#CONSIGNEE").tips({
					side:3,
		            msg:'请输入收货人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONSIGNEE").focus();
			return false;
			}
			if($("#INVOICETIME").val()==""){
				$("#INVOICETIME").tips({
					side:3,
		            msg:'请输入发货时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#INVOICETIME").focus();
			return false;
			}
			if($("#STATUS").val()==""){
				$("#STATUS").tips({
					side:3,
		            msg:'请输入发货单状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STATUS").focus();
			return false;
			}
			if($("#OPERATOR").val()==""){
				$("#OPERATOR").tips({
					side:3,
		            msg:'请输入操作人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPERATOR").focus();
			return false;
			}
			if($("#OPERATIONTIME").val()==""){
				$("#OPERATIONTIME").tips({
					side:3,
		            msg:'请输入操作时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#OPERATIONTIME").focus();
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