<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String basePath = (String) request.getSession().getAttribute("mall_url");
    String mallUploadUrl = (String) request.getSession().getAttribute("mall_upload_url");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css"/>
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/index/top.jsp" %>
    <!-- 日期框 -->
    <link rel="stylesheet" href="static/ace/css/datepicker.css"/>
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
                            <input type="hidden" name="SECKILL_ID" id="SECKILL_ID" value="${pd.SECKILL_ID}"/>
                            <input type="hidden" name="COUPON_ID" id="COUPON_ID" value="${pd.COUPON_ID}"/>
                            <input type="hidden" name="ADVERTISE_PIC" id="ADVERTISE_PIC"/>
                            <div id="zhongxin" style="padding-top: 13px;">
                                <table id="table_report" class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">广告名称:</td>
                                        <td><input type="text" name="ADVERTISE_NAME" id="ADVERTISE_NAME"
                                                   value="${pd.ADVERTISE_NAME}" maxlength="100" placeholder="这里输入广告名称"
                                                   title="广告名称" style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">广告位置:</td>
                                        <td><select class="chosen-select form-control" name="ADVERTISE_POSITION" id="ADVERTISE_POSITION" data-placeholder="请选择广告位置" style="vertical-align:top;" title="广告位置" style="width:98%;" >
                                            <option value=""></option>
                                            <c:forEach items="${positionList}" var="position">
                                                <option value="${position.POSITION_ID }" <c:if test="${position.POSITION_ID == pd.ADVERTISE_POSITION }">selected</c:if>>${position.POSITION_NAME }</option>
                                            </c:forEach>
                                        </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">开始日期:</td>
                                        <td><input class="span10 date-picker" name="STARTDATE" id="STARTDATE"
                                                   value="${pd.STARTDATE}" type="text" data-date-format="yyyy-mm-dd"
                                                   readonly="readonly" placeholder="开始日期" title="开始日期"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">结束日期:</td>
                                        <td><input class="span10 date-picker" name="ENDDATE" id="ENDDATE"
                                                   value="${pd.ENDDATE}" type="text" data-date-format="yyyy-mm-dd"
                                                   readonly="readonly" placeholder="结束日期" title="结束日期"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">秒杀活动:</td>
                                        <td><input type="text" name="SECKILL_NAME" id="SECKILL_NAME" value="${pd.SECKILL_NAME}" maxlength="100" placeholder="这里选择秒杀活动" title="秒杀活动" style="width:92%;"/>
                                            <a class="btn btn-xs btn-info" style="margin-top: -5px;" title="选择" onclick="choiceSeckill();">选择 </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">优惠活动:</td>
                                        <td><input type="text" name="COUPON_NAME" id="COUPON_NAME" value="${pd.COUPON_NAME}" maxlength="100" placeholder="这里选择优惠活动" title="优惠活动" style="width:92%;"/>
                                            <a class="btn btn-xs btn-info" style="margin-top: -5px;" title="选择" onclick="choiceCoupon();">选择 </a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">广告链接:</td>
                                        <td><input type="text" name="ADVERTISE_LINK" id="ADVERTISE_LINK"
                                                   value="${pd.ADVERTISE_LINK}" maxlength="255" placeholder="这里输入广告链接"
                                                   title="广告链接" style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">选择图片:</td>
                                        <td>
                                            <div class="col-sm-9">
                                                <input type="file" id="file" name="file" onchange="delandupload()"/>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">图片预览:</td>
                                        <td>
                                            <div class="col-sm-9" style="width: 98%;" id="img">
                                                <c:if test="${!empty pd.ADVERTISE_PIC}">
                                                    <c:forEach items="${fn:split(pd.ADVERTISE_PIC,',')}" var="pic"
                                                               varStatus="index">
                                                        <img name="picimg" alt="" src="<%=mallUploadUrl%>${pic}" width='100'
                                                             id="${index.index}" onclick="delImg('${index.index}')">
                                                        <input type="hidden" name="picvalue" id="v${index.index}"
                                                               value="${pic}"/>
                                                    </c:forEach>
                                                </c:if>
                                                <input type="hidden" id="picsize"
                                                       value="${fn:length(fn:split(pd.ADVERTISE_PIC,','))}">
                                            </div>
                                        </td>
                                    </tr>
                                    <%--<tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">是否启用:</td>
                                        <td><input class="ace ace-switch ace-switch-5" type="checkbox" name="IS_ENABLE"
                                                   id="IS_ENABLE"
                                                   value="1" <c:if test="${pd.IS_ENABLE==1}">checked="checked" </c:if>>
                                            <span class="lbl"></span>
                                        </td>
                                    </tr>--%>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">联系人:</td>
                                        <td><input type="text" name="LINK_MAN" id="LINK_MAN" value="${pd.LINK_MAN}"
                                                   maxlength="255" placeholder="这里输入联系人" title="联系人"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">Email:</td>
                                        <td><input type="text" name="EMAIL" id="EMAIL" value="${pd.EMAIL}"
                                                   maxlength="255" placeholder="这里输入联系人Email" title="联系人Email"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">联系电话:</td>
                                        <td><input type="text" name="TELEPHONE" id="TELEPHONE" value="${pd.TELEPHONE}"
                                                   maxlength="255" placeholder="这里输入联系电话" title="联系电话"
                                                   style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="text-align: center;" colspan="10">
                                            <a class="btn btn-mini btn-primary" onclick="save();">保存</a>
                                            <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img
                                    src="static/images/jiazai.gif"/><br/><h4 class="lighter block green">提交中...</h4>
                            </div>
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
<%@ include file="../../system/index/foot.jsp" %>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<!--图片上传-->
<script type="text/javascript" src="static/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="static/js/myjs/uploadimgs.js"></script>

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

    //选择秒杀活动
    function choiceSeckill(){
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag=true;
        diag.Title ="选择秒杀活动";
        diag.URL = '<%=basePath%>seckill/goListBox.do';
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

    //选择优惠活动
    function choiceCoupon() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "选择优惠活动";
        diag.URL = '<%=basePath%>coupon/goBox.do';
        diag.Width = 1000;
        diag.Height = 650;
        diag.Modal = true;				//有无遮罩窗口
        diag.ShowMaxButton = true;	//最大化按钮
        diag.ShowMinButton = true;		//最小化按钮
        diag.CancelEvent = function () { //关闭事件
            var tbodyWin = diag.innerFrame.contentWindow;
            var tbodyObj = tbodyWin.document.getElementById('simple-table');
            $(tbodyWin.$("table :checkbox")).each(function (key, value) {
                if (tbodyWin.$(value).prop('checked')) {
                    $("#COUPON_ID").val(tbodyWin.$("#selectStr").val());
                    $("#COUPON_NAME").val(tbodyObj.rows[key].cells[2].innerHTML);
                    return false;
                }
            });
            diag.close();
        };
        diag.show();
    }

    //保存
    function save() {
        if ($("#ADVERTISE_NAME").val() == "") {
            $("#ADVERTISE_NAME").tips({
                side: 3,
                msg: '请输入广告名称',
                bg: '#AE81FF',
                time: 2
            });
            $("#ADVERTISE_NAME").focus();
            return false;
        }
        if ($("#ADVERTISE_POSITION").val() == "") {
            $("#ADVERTISE_POSITION").tips({
                side: 3,
                msg: '请输入广告位置',
                bg: '#AE81FF',
                time: 2
            });
            $("#ADVERTISE_POSITION").focus();
            return false;
        }
        if ($("#STARTDATE").val() == "") {
            $("#STARTDATE").tips({
                side: 3,
                msg: '请输入开始日期',
                bg: '#AE81FF',
                time: 2
            });
            $("#STARTDATE").focus();
            return false;
        }
        if ($("#ENDDATE").val() == "") {
            $("#ENDDATE").tips({
                side: 3,
                msg: '请输入结束日期',
                bg: '#AE81FF',
                time: 2
            });
            $("#ENDDATE").focus();
            return false;
        }
        if ($("#ADVERTISE_LINK").val() == "") {
            $("#ADVERTISE_LINK").tips({
                side: 3,
                msg: '请输入广告链接',
                bg: '#AE81FF',
                time: 2
            });
            $("#ADVERTISE_LINK").focus();
            return false;
        }
        var advertise_pic = "";
        var picvalue = $('input[name="picvalue"]');
        $.each(picvalue, function (i, item) {
            if (i == 0) {
                advertise_pic = picvalue[i].value;
            } else {
                advertise_pic = advertise_pic + "," + picvalue[i].value;
            }
        });
        $("#ADVERTISE_PIC").val(advertise_pic);

        if (advertise_pic == "") {
            $("#file").tips({
                side: 3,
                msg: '请上传图片',
                bg: '#ae81ff',
                time: 2
            });
            $("#ADVERTISE_PIC").focus();
            return false;
        }
        $("#Form").submit();
        $("#zhongxin").hide();
        $("#zhongxin2").show();
    }

    $(function () {
        //日期框
        $('.date-picker').datepicker({autoclose: true, todayHighlight: true});
    });
</script>
</body>
</html>