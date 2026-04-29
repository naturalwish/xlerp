<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path0 = request.getContextPath();
	String basePath0 = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ path0 +"/";
	String mallUploadUrl = (String) request.getSession().getAttribute("mall_upload_url");
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<base href="<%=basePath0%>">
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
        <a class="btn btn-mini btn-primary" onclick="updown('${pd.GOODS_ID}');">排序</a>
	</div>
</footer>
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">

						<form action="goods/${msg }.do" name="Form" id="Form" method="post">
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
														<img alt="" src="<%=mallUploadUrl%>${pic}" width='100%'
															 id="${index.index}" onclick="delImg('${index.index}')">
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
<!--图片上传-->
<script type="text/javascript" src="static/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="static/js/myjs/uploadimgs.js"></script>

<script type="text/javascript">
    $(top.hangge());
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

    //删除图片
    function delImg(id) {
        var picsize = $("#picsize").val();
        picsize = picsize - 1;
        $("#picsize").val(picsize);
        $('#' + id).remove();
        $('#v' + id).remove();
    }

    //调整图片顺序
    function updown(Id){
        //排序
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag=true;
        diag.Title ="选择秒杀活动";
        diag.URL = '<%=basePath0%>goods/goEditOrder.do?GOODS_ID='+Id;
        diag.Width = 860;
        diag.Height = 680;
        diag.CancelEvent = function(){ //关闭事件
            var tbodyWin = diag.innerFrame.contentWindow;
            $("#SECKILL_ID").val(tbodyWin.$("#selectID").val());
            $("#SECKILL_NAME").val(tbodyWin.$("#selectName").val());
            diag.close();
        };
        diag.show();
    }
</script>
</body>
</html>