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
					
					<form action="goodsrate/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="GOODSRATE_ID" id="GOODSRATE_ID" value="${pd.GOODSRATE_ID}"/>
						<input type="hidden" name="RATE_GOODS" id="RATE_GOODS" value="${pd.GOODS_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">评价分数:</td>
								<td><input type="text" name="RATE_SCORE" id="RATE_SCORE" value="${pd.RATE_SCORE}" maxlength="255" placeholder="这里输入评价分数" title="评价分数" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">评价内容:</td>
								<td><input type="text" name="RATE_CONTENT" id="RATE_CONTENT" value="${pd.RATE_CONTENT}" maxlength="255" placeholder="这里输入评价内容" title="评价内容" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">宝贝相似度:</td>
								<td><input type="text" name="RATE_SIMLAR" id="RATE_SIMLAR" value="${pd.RATE_SIMLAR}" maxlength="255" placeholder="这里输入宝贝相似度" title="宝贝相似度" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">服务态度:</td>
								<td><input type="text" name="RATE_SERVICE" id="RATE_SERVICE" value="${pd.RATE_SERVICE}" maxlength="255" placeholder="这里输入服务态度" title="服务态度" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">物流服务质量:</td>
								<td><input type="text" name="RATE_LOGISTICS" id="RATE_LOGISTICS" value="${pd.RATE_LOGISTICS}" maxlength="255" placeholder="这里输入物流服务质量" title="物流服务质量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">评论时间:</td>
								<td><input class="span10 date-picker" name="RATE_ADDTIME" id="RATE_ADDTIME" value="${pd.RATE_ADDTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="评论时间" title="评论时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">评论IP:</td>
								<td><input type="text" name="RATE_IP" id="RATE_IP" value="${pd.RATE_IP}" maxlength="255" placeholder="这里输入评论IP" title="评论IP" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
								<td><input type="text" name="RATE_MEMO" id="RATE_MEMO" value="${pd.RATE_MEMO}" maxlength="255" placeholder="这里输入备注" title="备注" style="width:98%;"/></td>
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
			if($("#RATE_SCORE").val()==""){
				$("#RATE_SCORE").tips({
					side:3,
		            msg:'请输入评价分数',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RATE_SCORE").focus();
			return false;
			}
			if($("#RATE_CONTENT").val()==""){
				$("#RATE_CONTENT").tips({
					side:3,
		            msg:'请输入评价内容',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#RATE_CONTENT").focus();
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