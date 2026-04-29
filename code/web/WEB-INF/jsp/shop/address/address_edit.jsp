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
					
					<form action="address/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="ADDRESS_ID" id="ADDRESS_ID" value="${pd.ADDRESS_ID}"/>
						<input type="hidden" name="USER_ID" id="USER_ID" value="${pd.USER_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">收货人:</td>
								<td><input type="text" name="ADDR_REALNAME" id="ADDR_REALNAME" value="${pd.ADDR_REALNAME}" maxlength="100" placeholder="这里输入收货人" title="收货人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">电话:</td>
								<td><input type="text" name="ADDR_PHONE" id="ADDR_PHONE" value="${pd.ADDR_PHONE}" maxlength="100" placeholder="这里输入电话" title="电话" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">所在地区:</td>
								<td><input type="text" name="ADDR_CITY" id="ADDR_CITY" value="${pd.ADDR_CITY}" maxlength="255" placeholder="这里输入所在地区" title="所在地区" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">详细地址:</td>
								<td><textarea name="ADDR_DETAILS" id="ADDR_DETAILS" style="width:98%;height:100px;"
											  placeholder="这里输入详细地址" title="详细地址">${pd.ADDR_DETAILS}</textarea></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">是否默认:</td>
								<td>
									<select name="IS_DEFAULT" title="默认地址" style="width:98%;">
										<option value="0"
												<c:if test="${pd.IS_DEFAULT == '0' }">selected</c:if> >取消默认
										</option>
										<option value="1"
												<c:if test="${pd.IS_DEFAULT == '1' }">selected</c:if> >设为默认
										</option>
									</select>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="ADDR_MEMO" id="ADDR_MEMO" value="${pd.ADDR_MEMO}" maxlength="255" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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
			if($("#ADDR_REALNAME").val()==""){
				$("#ADDR_REALNAME").tips({
					side:3,
		            msg:'请输入收货人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ADDR_REALNAME").focus();
			return false;
			}
			if($("#ADDR_PHONE").val()==""){
				$("#ADDR_PHONE").tips({
					side:3,
		            msg:'请输入电话',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ADDR_PHONE").focus();
			return false;
			}
			if($("#ADDR_CITY").val()==""){
				$("#ADDR_CITY").tips({
					side:3,
		            msg:'请输入所在地区',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ADDR_CITY").focus();
			return false;
			}
			if($("#ADDR_DETAILS").val()==""){
				$("#ADDR_DETAILS").tips({
					side:3,
		            msg:'请输入详细地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ADDR_DETAILS").focus();
			return false;
			}
			if($("#IS_DEFAULT").val()==""){
				$("#IS_DEFAULT").tips({
					side:3,
		            msg:'请输入是否默认',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IS_DEFAULT").focus();
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