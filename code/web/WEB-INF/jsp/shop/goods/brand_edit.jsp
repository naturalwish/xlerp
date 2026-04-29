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
					
					<form action="brand/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="BRAND_ID" id="BRAND_ID" value="${pd.BRAND_ID}"/>
						<input type="hidden" name="BRAND_VISIBLE" id="BRAND_VISIBLE" value="1"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">品牌名称:</td>
								<td><input type="text" name="BRAND_NAME" id="BRAND_NAME" value="${pd.BRAND_NAME}" maxlength="255" placeholder="这里输入品牌名称" title="品牌名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">品牌网址:</td>
								<td><input type="text" name="BRAND_URL" id="BRAND_URL" value="${pd.BRAND_URL}" maxlength="255" placeholder="这里输入品牌网址" title="品牌网址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">品牌描述:</td>
								<td><textarea name="BRAND_DETAIL" id="BRAND_DETAIL" style="width:98%;height:100px;"
											  placeholder="这里输入品牌描述" title="品牌描述">${pd.BRAND_DETAIL}</textarea>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序:</td>
								<td><input type="text" name="BRAND_ORDER" id="BRAND_ORDER" value="${pd.BRAND_ORDER}" maxlength="255" placeholder="这里输入排序" title="排序" style="width:98%;"/></td>
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
			if($("#BRAND_NAME").val()==""){
				$("#BRAND_NAME").tips({
					side:3,
		            msg:'请输入品牌名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BRAND_NAME").focus();
			return false;
			}
			if($("#BRAND_URL").val()==""){
				$("#BRAND_URL").tips({
					side:3,
		            msg:'请输入品牌网址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BRAND_URL").focus();
			return false;
			}
			if($("#BRAND_DETAIL").val()==""){
				$("#BRAND_DETAIL").tips({
					side:3,
		            msg:'请输入品牌描述',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BRAND_DETAIL").focus();
			return false;
			}
			if($("#BRAND_ORDER").val()==""){
				$("#BRAND_ORDER").tips({
					side:3,
		            msg:'请输入排序',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BRAND_ORDER").focus();
			return false;
			}
			if($("#BRAND_VISIBLE").val()==""){
				$("#BRAND_VISIBLE").tips({
					side:3,
		            msg:'请输入是否显示',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#BRAND_VISIBLE").focus();
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