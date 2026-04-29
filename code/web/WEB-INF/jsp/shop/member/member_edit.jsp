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
					
					<form action="member/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="MEMBER_ID" id="MEMBER_ID" value="${pd.MEMBER_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员名称:</td>
								<td><input type="text" name="MEMBER_NAME" id="MEMBER_NAME" value="${pd.MEMBER_NAME}" maxlength="255" placeholder="这里输入会员名称" title="会员名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">邮件地址:</td>
								<td><input type="text" name="MEMBER_EMAIL" id="MEMBER_EMAIL" value="${pd.MEMBER_EMAIL}" maxlength="255" placeholder="这里输入邮件地址" title="邮件地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">登录密码:</td>
								<td><input type="text" name="MEMBER_PASSWORD" id="MEMBER_PASSWORD" value="${pd.MEMBER_PASSWORD}" maxlength="255" placeholder="这里输入登录密码" title="登录密码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员等级:</td>
								<td><input type="text" name="MEMBER_LEVEL" id="MEMBER_LEVEL" value="${pd.MEMBER_LEVEL}" maxlength="255" placeholder="这里输入会员等级" title="会员等级" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员性别:</td>
								<td><input type="number" name="MEMBER_SEX" id="MEMBER_SEX" value="${pd.MEMBER_SEX}" maxlength="32" placeholder="这里输入会员性别" title="会员性别" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">生日:</td>
								<td><input class="span10 date-picker" name="MEMBER_BIRTHDAY" id="MEMBER_BIRTHDAY" value="${pd.MEMBER_BIRTHDAY}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="生日" title="生日" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">信用额度:</td>
								<td><input type="number" name="MEMBER_CREDIT" id="MEMBER_CREDIT" value="${pd.MEMBER_CREDIT}" maxlength="32" placeholder="这里输入信用额度" title="信用额度" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">QQ:</td>
								<td><input type="text" name="MEMBER_QQ" id="MEMBER_QQ" value="${pd.MEMBER_QQ}" maxlength="255" placeholder="这里输入QQ" title="QQ" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">办公电话:</td>
								<td><input type="text" name="MEMBER_TEL" id="MEMBER_TEL" value="${pd.MEMBER_TEL}" maxlength="255" placeholder="这里输入办公电话" title="办公电话" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">手机:</td>
								<td><input type="text" name="MEMBER_PHONE" id="MEMBER_PHONE" value="${pd.MEMBER_PHONE}" maxlength="255" placeholder="这里输入手机" title="手机" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">会员积分:</td>
								<td><input type="number" name="MEMBER_SHIPPOINT" id="MEMBER_SHIPPOINT" value="${pd.MEMBER_SHIPPOINT}" maxlength="32" placeholder="这里输入会员积分" title="会员积分" style="width:98%;"/></td>
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
			if($("#MEMBER_NAME").val()==""){
				$("#MEMBER_NAME").tips({
					side:3,
		            msg:'请输入会员名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_NAME").focus();
			return false;
			}
			if($("#MEMBER_EMAIL").val()==""){
				$("#MEMBER_EMAIL").tips({
					side:3,
		            msg:'请输入邮件地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_EMAIL").focus();
			return false;
			}
			if($("#MEMBER_PASSWORD").val()==""){
				$("#MEMBER_PASSWORD").tips({
					side:3,
		            msg:'请输入登录密码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_PASSWORD").focus();
			return false;
			}
			if($("#MEMBER_LEVEL").val()==""){
				$("#MEMBER_LEVEL").tips({
					side:3,
		            msg:'请输入会员等级',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_LEVEL").focus();
			return false;
			}
			if($("#MEMBER_SEX").val()==""){
				$("#MEMBER_SEX").tips({
					side:3,
		            msg:'请输入会员性别',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_SEX").focus();
			return false;
			}
			if($("#MEMBER_BIRTHDAY").val()==""){
				$("#MEMBER_BIRTHDAY").tips({
					side:3,
		            msg:'请输入生日',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_BIRTHDAY").focus();
			return false;
			}
			if($("#MEMBER_CREDIT").val()==""){
				$("#MEMBER_CREDIT").tips({
					side:3,
		            msg:'请输入信用额度',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_CREDIT").focus();
			return false;
			}
			if($("#MEMBER_QQ").val()==""){
				$("#MEMBER_QQ").tips({
					side:3,
		            msg:'请输入QQ',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_QQ").focus();
			return false;
			}
			if($("#MEMBER_TEL").val()==""){
				$("#MEMBER_TEL").tips({
					side:3,
		            msg:'请输入办公电话',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_TEL").focus();
			return false;
			}
			if($("#MEMBER_PHONE").val()==""){
				$("#MEMBER_PHONE").tips({
					side:3,
		            msg:'请输入手机',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_PHONE").focus();
			return false;
			}
			if($("#MEMBER_SHIPPOINT").val()==""){
				$("#MEMBER_SHIPPOINT").tips({
					side:3,
		            msg:'请输入会员积分',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#MEMBER_SHIPPOINT").focus();
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