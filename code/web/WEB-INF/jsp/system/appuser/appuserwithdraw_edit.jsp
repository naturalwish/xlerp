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
					
					<form action="appuserwithdraw/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="APPUSERWITHDRAW_ID" id="APPUSERWITHDRAW_ID" value="${pd.APPUSERWITHDRAW_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">用户ID:</td>
								<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="32" placeholder="这里输入用户ID" title="用户ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">提现金额:</td>
								<td><input type="number" name="WITHDRAWTOTAL" id="WITHDRAWTOTAL" value="${pd.WITHDRAWTOTAL}" maxlength="32" placeholder="这里输入提现金额" title="提现金额" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">提现时间:</td>
								<td><input class="span10 date-picker" name="WITHDRAWDATE" id="WITHDRAWDATE" value="${pd.WITHDRAWDATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="提现时间" title="提现时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审核状态:</td>
								<td><input type="text" name="CHECKSTATUS" id="CHECKSTATUS" value="${pd.CHECKSTATUS}" maxlength="10" placeholder="这里输入审核状态" title="审核状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审核用户:</td>
								<td><input type="text" name="CHECKUSERID" id="CHECKUSERID" value="${pd.CHECKUSERID}" maxlength="32" placeholder="这里输入审核用户" title="审核用户" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审核时间:</td>
								<td><input class="span10 date-picker" name="CHECKDATE" id="CHECKDATE" value="${pd.CHECKDATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="审核时间" title="审核时间" style="width:98%;"/></td>
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
			if($("#USERID").val()==""){
				$("#USERID").tips({
					side:3,
		            msg:'请输入用户ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USERID").focus();
			return false;
			}
			if($("#WITHDRAWTOTAL").val()==""){
				$("#WITHDRAWTOTAL").tips({
					side:3,
		            msg:'请输入提现金额',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#WITHDRAWTOTAL").focus();
			return false;
			}
			if($("#WITHDRAWDATE").val()==""){
				$("#WITHDRAWDATE").tips({
					side:3,
		            msg:'请输入提现时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#WITHDRAWDATE").focus();
			return false;
			}
			if($("#CHECKSTATUS").val()==""){
				$("#CHECKSTATUS").tips({
					side:3,
		            msg:'请输入审核状态',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHECKSTATUS").focus();
			return false;
			}
			if($("#CHECKUSERID").val()==""){
				$("#CHECKUSERID").tips({
					side:3,
		            msg:'请输入审核用户',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHECKUSERID").focus();
			return false;
			}
			if($("#CHECKDATE").val()==""){
				$("#CHECKDATE").tips({
					side:3,
		            msg:'请输入审核时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CHECKDATE").focus();
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