<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
<footer>
	<div style="width: 100%;padding-top: 20px; padding-bottom: 2px;" class="center">
		<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
		<a class="btn btn-mini btn-danger" onclick="window.location.reload()">刷新</a>
	</div>
</footer>
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-container" >
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">

							<form action="goods/${msg}.do" name="Form" id="Form" method="post">
								<input type="hidden" name="GOODS_ID" id="GOODS_ID" value="${pd.GOODS_ID}"/>
								<input type="hidden" name="GOODS_DETAILSPIC" id="GOODS_DETAILSPIC" value="0"/>
								<div style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td colspan="4">
												<div class="col-sm-9" style="width: 98%;" id="img">
													<c:if test="${!empty pd.GOODS_DETAILSPIC}">
														<c:forEach items="${fn:split(pd.GOODS_DETAILSPIC,',')}" var="pic"
																   varStatus="index">
															<div id="container">
																<div class="main_box user_style2" align="center" data-hipop="">
																	<a href="javascript:goup('${index.index}')"><img width="4%" src="uploadFiles/uploadImgs/up.png" /></a>
																	<img alt="${pic}" src="<%=mallUploadUrl%>${pic}" width='300' onmouseenter="" onmouse=""
																		 id="${index.index}" onclick="delImg('${index.index}')">
																	<a href="javascript:godown('${index.index}')"><img width="4%" src="uploadFiles/uploadImgs/down.png" /></a>
																</div>
															</div>
															<input type="hidden" name="picvalue" id="v${index.index}"
																   value="${pic}"/>
														</c:forEach>
													</c:if>
													<input type="hidden" id="picsize"
														   value="${fn:length(fn:split(pd.GOODS_DETAILSPIC,','))}">
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

	<!-- 返回顶部 -->
	<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
		<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
	</a>

</div>
<!-- /.main-container -->

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp"%>
<!-- 删除时确认窗口 -->
<script src="static/ace/js/bootbox.js"></script>
<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
    $(top.hangge());//关闭加载状态
    //调整图片顺序(1代表源位置，2代表目标位置)
    function goup(id1){
        if(id1 == 0){
			alert("第一张图片不能上移！");
			return;
		}
        var picvalue = $('input[name="picvalue"]');
        var picv1 = picvalue[id1].value;
        var src1 = $('#' + id1).attr('src');
        var id2 = id1 - 1;
        var picv2 = picvalue[id2].value;
        var src2 = $('#' + id2).attr('src');
        $('#' + id2).attr('src',src1);
        $('#' + id1).attr('src',src2);
        picvalue[id2].value = picv1;
        picvalue[id1].value = picv2;
        var GOODS_DETAILSPIC = "";
        $.each(picvalue, function (i, item) {
            if (i == 0) {
                GOODS_DETAILSPIC = picvalue[i].value;
            } else {
                GOODS_DETAILSPIC = GOODS_DETAILSPIC + "," + picvalue[i].value;
            }
        });
    }
    //(1代表源位置，2代表目标位置)
    function godown(id1){
        var picvalue = $('input[name="picvalue"]');
        if(id1 == picvalue.length -1){
            alert("最后一张图片不能下移！");
            return;
		}
        var picv1 = picvalue[id1].value;
        var src1 = $('#' + id1).attr('src');
        var id2 = parseInt(id1) + 1;
        var picv2 = picvalue[id2].value;
        var src2 = $('#' + id2).attr('src');
        $('#' + id2).attr('src',src1);
        $('#' + id1).attr('src',src2);
        picvalue[id2].value = picv1;
        picvalue[id1].value = picv2;
        var GOODS_DETAILSPIC = "";
        $.each(picvalue, function (i, item) {
            if (i == 0) {
                GOODS_DETAILSPIC = picvalue[i].value;
            } else {
                GOODS_DETAILSPIC = GOODS_DETAILSPIC + "," + picvalue[i].value;
            }
        });
    }
    //保存
    function save(){
        var GOODS_DETAILSPIC = "";
        var picvalue = $('input[name="picvalue"]');
        $.each(picvalue, function (i, item) {
            if (i == 0) {
                GOODS_DETAILSPIC = picvalue[i].value;
            } else {
                GOODS_DETAILSPIC = GOODS_DETAILSPIC + "," + picvalue[i].value;
            }
        });
        $("#GOODS_DETAILSPIC").val(GOODS_DETAILSPIC);
        $("#Form").submit();
        $("#zhongxin").hide();
        $("#zhongxin2").show();
    }
</script>
</body>
</html>