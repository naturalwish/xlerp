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
					
					<form action="supplier/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="SUPPLIER_ID" id="SUPPLIER_ID" value="${pd.SUPPLIER_ID}"/>
						<input type="hidden" name="SUPPLIER_SORT" id="SUPPLIER_SORT" value="${pd.SUPPLIER_SORT}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">地区:</td>
								<td>
									<div class="col-xs-4 label label-lg label-light arrowed-in arrowed-right">
										<b>${null == pds.NAME ?'(无) 此项为顶级':pds.NAME}</b>
									</div>
								</td>
								<%--<td><input type="text" name="SUPPLIER_SORT" id="SUPPLIER_SORT" value="${pd.SUPPLIER_SORT}" maxlength="255" placeholder="这里输入分类" title="分类" style="width:98%;"/></td>--%>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">编码:</td>
								<td><input type="text" name="SUPPLIER_CODE" id="SUPPLIER_CODE" value="${pd.SUPPLIER_CODE}" maxlength="255" placeholder="这里输入编码" title="编码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">名称:</td>
								<td><input type="text" name="SUPPLIER_NAME" id="SUPPLIER_NAME" value="${pd.SUPPLIER_NAME}" maxlength="255" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">所属行业:</td>
								<td><input type="text" name="SUPPLIER_INDUSTRY" id="SUPPLIER_INDUSTRY" value="${pd.SUPPLIER_INDUSTRY}" maxlength="255" placeholder="这里输入所属行业" title="所属行业" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">地址:</td>
								<td><input type="text" name="SUPPLIER_ADDRESS" id="SUPPLIER_ADDRESS" value="${pd.SUPPLIER_ADDRESS}" maxlength="255" placeholder="这里输入地址" title="地址" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">邮编:</td>
								<td><input type="text" name="SUPPLIER_POSTCODE" id="SUPPLIER_POSTCODE" value="${pd.SUPPLIER_POSTCODE}" maxlength="255" placeholder="这里输入邮编" title="邮编" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">纳税人登记号:</td>
								<td><input type="text" name="SUPPLIER_TAXPAYER" id="SUPPLIER_TAXPAYER" value="${pd.SUPPLIER_TAXPAYER}" maxlength="255" placeholder="这里输入纳税人登记号" title="纳税人登记号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">开户银行:</td>
								<td><input type="text" name="SUPPLIER_BANK" id="SUPPLIER_BANK" value="${pd.SUPPLIER_BANK}" maxlength="255" placeholder="这里输入开户银行" title="开户银行" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">开户账号:</td>
								<td><input type="text" name="SUPPLIER_BANKCODE" id="SUPPLIER_BANKCODE" value="${pd.SUPPLIER_BANKCODE}" maxlength="255" placeholder="这里输入开户账号" title="开户账号" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">法人:</td>
								<td><input type="text" name="SUPPLIER_LEGAL" id="SUPPLIER_LEGAL" value="${pd.SUPPLIER_LEGAL}" maxlength="255" placeholder="这里输入法人" title="法人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">联系人:</td>
								<td><input type="text" name="SUPPLIER_TELER" id="SUPPLIER_TELER" value="${pd.SUPPLIER_TELER}" maxlength="255" placeholder="这里输入联系人" title="联系人" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">联系方式:</td>
								<td><input type="text" name="SUPPLIER_TEL" id="SUPPLIER_TEL" value="${pd.SUPPLIER_TEL}" maxlength="255" placeholder="这里输入联系方式" title="联系方式" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:100px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="SUPPLIER_MEMO" id="SUPPLIER_MEMO" value="${pd.SUPPLIER_MEMO}" maxlength="255" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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
			if($("#SUPPLIER_SORT").val()==""){
				$("#SUPPLIER_SORT").tips({
					side:3,
		            msg:'请输入分类',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_SORT").focus();
			return false;
			}
			if($("#SUPPLIER_CODE").val()==""){
				$("#SUPPLIER_CODE").tips({
					side:3,
		            msg:'请输入编码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_CODE").focus();
			return false;
			}
			if($("#SUPPLIER_NAME").val()==""){
				$("#SUPPLIER_NAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_NAME").focus();
			return false;
			}
			if($("#SUPPLIER_INDUSTRY").val()==""){
				$("#SUPPLIER_INDUSTRY").tips({
					side:3,
		            msg:'请输入所属行业',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_INDUSTRY").focus();
			return false;
			}
			if($("#SUPPLIER_ADDRESS").val()==""){
				$("#SUPPLIER_ADDRESS").tips({
					side:3,
		            msg:'请输入地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_ADDRESS").focus();
			return false;
			}
			/*if($("#SUPPLIER_POSTCODE").val()==""){
				$("#SUPPLIER_POSTCODE").tips({
					side:3,
		            msg:'请输入邮编',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_POSTCODE").focus();
			return false;
			}
			if($("#SUPPLIER_TAXPAYER").val()==""){
				$("#SUPPLIER_TAXPAYER").tips({
					side:3,
		            msg:'请输入纳税人登记号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_TAXPAYER").focus();
			return false;
			}
			if($("#SUPPLIER_BANK").val()==""){
				$("#SUPPLIER_BANK").tips({
					side:3,
		            msg:'请输入开户银行',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_BANK").focus();
			return false;
			}
			if($("#SUPPLIER_BANKCODE").val()==""){
				$("#SUPPLIER_BANKCODE").tips({
					side:3,
		            msg:'请输入开户账号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_BANKCODE").focus();
			return false;
			}
			if($("#SUPPLIER_LEGAL").val()==""){
				$("#SUPPLIER_LEGAL").tips({
					side:3,
		            msg:'请输入法人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_LEGAL").focus();
			return false;
			}
			if($("#SUPPLIER_TELER").val()==""){
				$("#SUPPLIER_TELER").tips({
					side:3,
		            msg:'请输入联系人',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_TELER").focus();
			return false;
			}
			if($("#SUPPLIER_TEL").val()==""){
				$("#SUPPLIER_TEL").tips({
					side:3,
		            msg:'请输入联系方式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_TEL").focus();
			return false;
			}
			if($("#SUPPLIER_MEMO").val()==""){
				$("#SUPPLIER_MEMO").tips({
					side:3,
		            msg:'请输入备注',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SUPPLIER_MEMO").focus();
			return false;
			}*/
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