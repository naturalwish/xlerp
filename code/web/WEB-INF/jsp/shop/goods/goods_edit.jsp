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

						<form action="goods/${msg }.do" name="Form" id="Form" method="post">
							<input type="hidden" name="GOODS_ID" id="GOODS_ID" value="${pd.GOODS_ID}"/>
							<input type="hidden" name="GOODS_PIC" id="GOODS_PIC"/>
							<input type="hidden" name="GOODS_DELFLAG" id="GOODS_DELFLAG" value="0"/>
							<input type="hidden" name="GOODS_CATEGORY" id="GOODS_CATEGORY" value="0"/>
							<input type="hidden" name="GOODS_DETAILSPIC" id="GOODS_DETAILSPIC" value="${pd.GOODS_DETAILSPIC}"/>
							<div id="zhongxin" style="padding-top: 13px;">
								<table id="table_report" class="table table-striped table-bordered table-hover">
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">分类:</td>
										<td colspan="3">
											<select id="select_selectsplitter1" class="form-control"
													style="height: 100px" size="4" >
												<c:forEach items="${category['categoryList']}"
														   var="categoryList" varStatus="s">
													<optgroup label="${categoryList.CATEGORY_NAME}"
															  value="${categoryList.CATEGORY_ID}">
														<c:set value="childcategory${s.index}" var="childcategory"></c:set>
														<c:forEach items="${category[childcategory]}"
																   var="childcategory">

															<option value="${childcategory.CATEGORY_ID}"
																	<c:if test="${childcategory.CATEGORY_ID eq pd.GOODS_CATEGORY }">selected="selected"</c:if>>${childcategory.CATEGORY_NAME}</option>
														</c:forEach>
													</optgroup>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">选择图片:</td>
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
												<c:if test="${!empty pd.GOODS_PIC}">
													<c:forEach items="${fn:split(pd.GOODS_PIC,',')}" var="pic"
															   varStatus="index">
														<img alt="" src="<%=mallUploadUrl%>${pic}" width='100'
															 id="${index.index}" onclick="delImg('${index.index}')">
														<input type="hidden" name="picvalue" id="v${index.index}"
															   value="${pic}"/>
													</c:forEach>
												</c:if>
												<input type="hidden" id="picsize"
													   value="${fn:length(fn:split(pd.GOODS_PIC,','))}">
											</div>
										</td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">名称:</td>
										<td colspan="3"><input type="text" name="GOODS_NAME" id="GOODS_NAME" value="${pd.GOODS_NAME}" maxlength="255" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">编号:</td>
										<td><input type="text" name="GOODS_CODE" id="GOODS_CODE" value="${pd.GOODS_CODE}" maxlength="255" placeholder="这里输入编号" title="编号" style="width:98%;"/></td>
										<td style="width:120px;text-align: right;padding-top: 13px;">货号:</td>
										<td><input type="text" name="GOODS_NUMBER" id="GOODS_NUMBER" value="${pd.GOODS_NUMBER}" maxlength="255" placeholder="这里输入货号" title="货号" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">上架:</td>
										<td><input class="ace ace-switch ace-switch-5" type="checkbox" name="GOODS_FORSALE"
												   id="GOODS_FORSALE"
												   value="1" <c:if test="${pd.GOODS_FORSALE==1}">checked="checked" </c:if> <c:if test="${pd.GOODS_FORSALE==null}">checked="checked" </c:if>>
											<span class="lbl"></span>
										</td>
										<td style="width:120px;text-align: right;padding-top: 13px;">精品:</td>
										<td><input class="ace ace-switch ace-switch-5" type="checkbox" name="GOODS_BEST"
												   id="GOODS_BEST"
												   value="1" <c:if test="${pd.GOODS_BEST==1}">checked="checked" </c:if>>
											<span class="lbl"></span>
										</td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">新品:</td>
										<td><input class="ace ace-switch ace-switch-5" type="checkbox" name="GOODS_NEW"
												   id="GOODS_NEW"
												   value="1" <c:if test="${pd.GOODS_NEW==1}">checked="checked" </c:if>>
											<span class="lbl"></span>
										</td>
										<td style="width:120px;text-align: right;padding-top: 13px;">推荐:</td>
										<td><input class="ace ace-switch ace-switch-5" type="checkbox" name="GOODS_HOT"
												   id="GOODS_HOT"
												   value="1" <c:if test="${pd.GOODS_HOT==1}">checked="checked" </c:if>>
											<span class="lbl"></span>
										</td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">品牌:</td>
										<td><select class="chosen-select form-control" name="GOODS_BRAND" id="GOODS_BRAND" data-placeholder="请选择品牌" style="vertical-align:top;" title="品牌" style="width:98%;" >
											<option value=""></option>
											<c:forEach items="${brandList}" var="brandList">
												<option value="${brandList.BRAND_ID}" <c:if test="${brandList.BRAND_ID == pd.GOODS_BRAND}">selected</c:if>>${brandList.BRAND_NAME }</option>
											</c:forEach>
										</select>
										</td>
										<%--<td><input type="text" name="GOODS_BRAND" id="GOODS_BRAND" value="${pd.GOODS_BRAND}" maxlength="255" placeholder="这里输入品牌" title="品牌" style="width:98%;"/></td>--%>
										<td style="width:120px;text-align: right;padding-top: 13px;">标题:</td>
										<td><input type="text" name="GOODS_TITLE" id="GOODS_TITLE" value="${pd.GOODS_TITLE}" maxlength="255" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
										<%--<td style="width:120px;text-align: right;padding-top: 13px;">会员价格:</td>
										<td><input type="number" name="MEMBER_PRICE" id="MEMBER_PRICE" value="${empty pd.MEMBER_PRICE?0:pd.MEMBER_PRICE}" maxlength="32" placeholder="这里输入会员价格" title="会员价格" style="width:98%;"/></td>--%>
									</tr>

									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">商品简介:</td>
										<td><input type="text" name="GOODS_INTRODUCTION" id="GOODS_INTRODUCTION" value="${pd.GOODS_INTRODUCTION}" maxlength="255" placeholder="这里输入商品简介" title="商品简介" style="width:98%;"/></td>
										<td style="width:120px;text-align: right;padding-top: 13px;">商品关键词:</td>
										<td><input type="text" name="GOODS_KEYWORDS" id="GOODS_KEYWORDS" value="${pd.GOODS_KEYWORDS}" maxlength="255" placeholder="这里输入商品关键词" title="商品关键词" style="width:98%;"/></td>
									</tr>

									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">详细描述:</td>
										<td><input type="text" name="GOODS_DETAILS" id="GOODS_DETAILS" value="${pd.GOODS_DETAILS}" maxlength="255" placeholder="这里输入详细描述" title="详细描述" style="width:98%;"/></td>
										<td style="width:120px;text-align: right;padding-top: 13px;">重量:</td>
										<td><input type="number" name="GOODS_WEIGHT" id="GOODS_WEIGHT" value="${empty pd.GOODS_WEIGHT?0:pd.GOODS_WEIGHT}" maxlength="32" placeholder="这里输入重量" title="重量" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">数量:</td>
										<td><input type="number" name="GOODS_NUM" id="GOODS_NUM" value="${empty pd.GOODS_NUM?0:pd.GOODS_NUM}" maxlength="32" placeholder="这里输入数量" title="数量" style="width:98%;"/></td>
										<td style="width:120px;text-align: right;padding-top: 13px;">库存警告数量:</td>
										<td><input type="number" name="GOODS_LOW" id="GOODS_LOW" value="${empty pd.GOODS_LOW?0:pd.GOODS_LOW}" maxlength="32" placeholder="这里输入库存警告数量" title="库存警告数量" style="width:98%;"/></td>
									</tr>
									<tr>
										<td style="width:120px;text-align: right;padding-top: 13px;">排序:</td>
										<td><input type="text" name="GOODS_ORDER" id="GOODS_ORDER" value="${pd.GOODS_ORDER}" maxlength="255" placeholder="这里输入排序" title="排序" style="width:98%;"/></td>
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
<c:if test="${msg == 'edit' }">
	<div>
		<iframe name="treeFrame" id="treeFrame" frameborder="0" src="<%=basePath%>/attribute/list.do?GOODS_ID=${pd.GOODS_ID}" style="margin:0 auto;width:950px;height:368px;;"></iframe>
	</div>
</c:if>

<footer>
	<div style="width: 100%;padding-bottom: 2px;" class="center">
		<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
		<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
	</div>
</footer>


<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp"%>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<!--图片上传-->
<script type="text/javascript" src="static/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="static/js/myjs/uploadimgs.js"></script>
<!----分类选择框----->
<script type="text/javascript" src="static/ace/js/components-form-tools2.js"></script>
<script src="static/ace/js/bootstrap-selectsplitter.min.js" type="text/javascript"></script>

<script type="text/javascript">
    $(function() {
        ComponentsFormTools2.init();
    });
</script>

<script type="text/javascript">
    $(top.hangge());
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
        var category_id = $("#select-2").val();
        $("#GOODS_CATEGORY").val(category_id);
        if ($("#select-1").val() == ""||$("#select-1").val() == null) {
            $("#select-1").tips({
                side : 3,
                msg : '请选择分类',
                bg : '#ae81ff',
                time : 2
            });
            $("#select-1").focus();
            return false;
        }
        if ($("#select-2").val() == ""||$("#select-2").val() == null) {
            $("#select-2").tips({
                side : 3,
                msg : '请选择分类',
                bg : '#ae81ff',
                time : 2
            });
            $("#select-2").focus();
            return false;
        }
        var goods_pic = "";
        var picvalue = $('input[name="picvalue"]');
        $.each(picvalue, function (i, item) {
            if (i == 0) {
                goods_pic = picvalue[i].value;
            } else {
                goods_pic = goods_pic + "," + picvalue[i].value;
            }
        });
        $("#GOODS_PIC").val(goods_pic);
        /*if($("#GOODS_PIC").val()==""){
            $("#GOODS_PIC").tips({
                side:3,
                msg:'请输入图片',
                bg:'#AE81FF',
                time:2
            });
            $("#GOODS_PIC").focus();
            return false;
        }*/
        if($("#GOODS_NAME").val()==""){
            $("#GOODS_NAME").tips({
                side:3,
                msg:'请输入名称',
                bg:'#AE81FF',
                time:2
            });
            $("#GOODS_NAME").focus();
            return false;
        }
        if($("#GOODS_NUM").val()==""){
            $("#GOODS_NUM").tips({
                side:3,
                msg:'请输入数量',
                bg:'#AE81FF',
                time:2
            });
            $("#GOODS_NUM").focus();
            return false;
        }
        if($("#GOODS_CODE").val()==""){
            $("#GOODS_CODE").tips({
                side:3,
                msg:'请输入编号',
                bg:'#AE81FF',
                time:2
            });
            $("#GOODS_CODE").focus();
            return false;
        }
        if(!$("#GOODS_FORSALE").prop('checked')) $("#GOODS_FORSALE").val("0");
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