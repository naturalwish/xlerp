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
	<%@ include file="../index/top.jsp"%>
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
					
					<form action="appuserapplyforxy/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="APPUSERAPPLYFORXY_ID" id="APPUSERAPPLYFORXY_ID" value="${pd.APPUSERAPPLYFORXY_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">申请主键:</td>
								<td><input type="text" name="FORXY_ID" id="FORXY_ID" value="${pd.FORXY_ID}" maxlength="255" placeholder="这里输入申请主键" title="申请主键" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">UNION_ID:</td>
								<td><input type="text" name="UNION_ID" id="UNION_ID" value="${pd.UNION_ID}" maxlength="255" placeholder="这里输入UNION_ID" title="UNION_ID" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">申请人:</td>
								<td><input type="text" name="FORXY_NAME" id="FORXY_NAME" value="${pd.FORXY_NAME}" maxlength="255" placeholder="这里输入申请人" title="申请人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">申请电话:</td>
								<td><input type="text" name="FORXY_PHONE" id="FORXY_PHONE" value="${pd.FORXY_PHONE}" maxlength="255" placeholder="这里输入申请电话" title="申请电话" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">申请备注:</td>
								<td><input type="text" name="FORXY_MEMO" id="FORXY_MEMO" value="${pd.FORXY_MEMO}" maxlength="255" placeholder="这里输入申请备注" title="申请备注" style="width:98%;"/></td>
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
			if($("#FORXY_ID").val()==""){
				$("#FORXY_ID").tips({
					side:3,
		            msg:'请输入申请主键',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FORXY_ID").focus();
			return false;
			}
			if($("#UNION_ID").val()==""){
				$("#UNION_ID").tips({
					side:3,
		            msg:'请输入UNION_ID',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#UNION_ID").focus();
			return false;
			}
			if($("#FORXY_NAME").val()==""){
				$("#FORXY_NAME").tips({
					side:3,
		            msg:'请输入申请人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FORXY_NAME").focus();
			return false;
			}
			if($("#FORXY_PHONE").val()==""){
				$("#FORXY_PHONE").tips({
					side:3,
		            msg:'请输入申请电话',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FORXY_PHONE").focus();
			return false;
			}
			if($("#FORXY_MEMO").val()==""){
				$("#FORXY_MEMO").tips({
					side:3,
		            msg:'请输入申请备注',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#FORXY_MEMO").focus();
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