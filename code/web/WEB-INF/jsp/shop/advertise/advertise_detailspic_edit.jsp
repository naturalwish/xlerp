<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

						<form action="advertise/${msg }.do" name="Form" id="Form" method="post">
							<input type="hidden" name="ADVERTISE_ID" id="ADVERTISE_ID" value="${pd.ADVERTISE_ID}"/>
							<input type="hidden" name="ADVERTISE_DETAILSPIC" id="ADVERTISE_DETAILSPIC"/>
							<div id="zhongxin" style="padding-top: 13px;">
								<table id="table_report" class="table table-striped table-bordered table-hover">
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">选择详细图片:</td>
										<td colspan="3">
											<div class="col-sm-9">
												<input type="file" id="file" name="file" onchange="upload()"/>
											</div>
										</td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">图片预览:</td>
										<td colspan="3">
											<div class="col-sm-9" style="width: 98%;" id="img">
												<c:if test="${!empty pd.ADVERTISE_DETAILSPIC}">
													<c:forEach items="${fn:split(pd.ADVERTISE_DETAILSPIC,',')}" var="pic"
															   varStatus="index">
														<img alt="" src="<%=mallUploadUrl%>${pic}" width='100%'
															 id="${index.index}" onclick="delImg('${index.index}')">
														<input type="hidden" name="picvalue" id="v${index.index}"
															   value="${pic}"/>
													</c:forEach>
												</c:if>
												<input type="hidden" id="picsize"
													   value="${fn:length(fn:split(pd.ADVERTISE_DETAILSPIC,','))}">
											</div>
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
<footer>
	<div style="width: 100%;padding-bottom: 2px;" class="center">
		<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
		<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
	</div>
</footer>


<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp"%>
<!--图片上传-->
<script type="text/javascript" src="static/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="static/js/myjs/uploadimgs.js"></script>

<script type="text/javascript">
    $(top.hangge());
    //保存
    function save(){
		var advertise_detailspic ="";
        var picvalue = $('input[name="picvalue"]');
        $.each(picvalue, function (i, item) {
            if (i == 0) {
				advertise_detailspic = picvalue[i].value;
            } else {
				advertise_detailspic = advertise_detailspic + "," + picvalue[i].value;
            }
        });
        $("#ADVERTISE_DETAILSPIC").val(advertise_detailspic);
        $("#Form").submit();
        $("#zhongxin").hide();
        $("#zhongxin2").show();
    }

    //删除图片
    function delImg(id) {
        var picsize = $("#picsize").val();
        picsize = picsize - 1;
        $("#picsize").val(picsize);
        $('#' + id).remove();
        $('#v' + id).remove();
    }
</script>
</body>
</html>