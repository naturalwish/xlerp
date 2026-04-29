<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String mallUploadUrl = (String) request.getSession().getAttribute("mall_upload_url");
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
					
					<form action="newsort/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="NEWSORT_ID" id="NEWSORT_ID" value="${pd.NEWSORT_ID}"/>
						<input type="hidden" name="SORT_IMG" id="SORT_IMG" value="${pd.SORT_IMG}"/>
						<input type="hidden" name="SORT_ALLOW" id="SORT_ALLOW" value="${pd.SORT_ALLOW}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">分类名字:</td>
								<td><input type="text" name="SORT_NAME" id="SORT_NAME" value="${pd.SORT_NAME}" maxlength="255" placeholder="这里输入分类名字" title="分类名字" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">选择图片:</td>
								<td>
									<div class="col-sm-9">
										<input type="file" id="file" name="file" onchange="delandupload()"/>
									</div>
								</td>
							</tr>
							<tr>
								<td style="width:120px;text-align: right;padding-top: 13px;">图片预览:</td>
								<td>
									<div class="col-sm-9" style="width: 98%;" id="img">
										<c:if test="${!empty pd.SORT_IMG}">
											<c:forEach items="${fn:split(pd.SORT_IMG,',')}" var="pic"
													   varStatus="index">
												<img alt="" src="<%=mallUploadUrl%>${pic}" width='100' name="picimg"
													 id="${index.index}" onclick="delImg('${index.index}')">
												<input type="hidden" name="picvalue" id="v${index.index}"
													   value="${pic}"/>
											</c:forEach>
										</c:if>
										<input type="hidden" id="picsize"
											   value="${fn:length(fn:split(pd.SORT_IMG,','))}">
									</div>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">排序:</td>
								<td><input type="number" name="SORT" id="SORT" value="${pd.SORT}" maxlength="32" placeholder="这里输入排序" title="排序" style="width:98%;"/></td>
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
<!--图片上传-->
<script type="text/javascript" src="static/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="static/js/myjs/uploadimgs.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
        //清除图片并重新上传
        function delandupload(){
            var picvalue = $('input[name="picvalue"]');
            $.each(picvalue, function (i, item) {
                $('#' + picvalue[i].id).remove();
            });
            var picimg = $('img[name="picimg"]');
            $.each(picimg, function (i, item) {
                $('#' + picimg[i].id).remove();
            });
            $("#picsize").val(0);
            upload();
        }
        //删除图片
        function delImg(id) {
            var picsize = $("#picsize").val();
            picsize = picsize - 1;
            $("#picsize").val(picsize);
            $('#' + id).remove();
            $('#v' + id).remove();
        }
		//保存
		function save(){
            var sort_img = "";
            var picvalue = $('input[name="picvalue"]');
            $.each(picvalue, function (i, item) {
                if (i == 0) {
                    sort_img = picvalue[i].value;
                } else {
                    sort_img = sort_img + "," + picvalue[i].value;
                }
            });
            $("#SORT_IMG").val(sort_img);
			if($("#SORT_NAME").val()==""){
				$("#SORT_NAME").tips({
					side:3,
		            msg:'请输入分类名字',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SORT_NAME").focus();
			return false;
			}
			if($("#SORT_IMG").val()==""){
				$("#SORT_IMG").tips({
					side:3,
		            msg:'请输入分类图',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SORT_IMG").focus();
			return false;
			}
			if($("#SORT").val()==""){
				$("#SORT").tips({
					side:3,
		            msg:'请输入排序',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SORT").focus();
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