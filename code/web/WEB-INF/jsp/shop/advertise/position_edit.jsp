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
					
					<form action="position/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="POSITION_ID" id="POSITION_ID" value="${pd.POSITION_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">位置名称:</td>
								<td><input type="text" name="POSITION_NAME" id="POSITION_NAME" value="${pd.POSITION_NAME}" maxlength="100" placeholder="这里输入位置名称" title="位置名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">位置宽度:</td>
								<td><input type="number" name="POSITION_WIDTH" id="POSITION_WIDTH" value="${pd.POSITION_WIDTH}" maxlength="32" placeholder="这里输入位置宽度" title="位置宽度" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">位置高度:</td>
								<td><input type="number" name="POSITION_HEIGHT" id="POSITION_HEIGHT" value="${pd.POSITION_HEIGHT}" maxlength="32" placeholder="这里输入位置高度" title="位置高度" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">位置描述:</td>
								<td><input type="text" name="POSITION_DESCRIBE" id="POSITION_DESCRIBE" value="${pd.POSITION_DESCRIBE}" maxlength="255" placeholder="这里输入位置描述" title="位置描述" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">位置模板:</td>
								<td><input type="text" name="POSITION_TEMPLATE" id="POSITION_TEMPLATE" value="${pd.POSITION_TEMPLATE}" maxlength="500" placeholder="这里输入位置模板" title="位置模板" style="width:98%;"/></td>
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
			if($("#POSITION_NAME").val()==""){
				$("#POSITION_NAME").tips({
					side:3,
		            msg:'请输入位置名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POSITION_NAME").focus();
			return false;
			}
			if($("#POSITION_WIDTH").val()==""){
				$("#POSITION_WIDTH").tips({
					side:3,
		            msg:'请输入位置宽度',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POSITION_WIDTH").focus();
			return false;
			}
			if($("#POSITION_HEIGHT").val()==""){
				$("#POSITION_HEIGHT").tips({
					side:3,
		            msg:'请输入位置高度',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#POSITION_HEIGHT").focus();
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