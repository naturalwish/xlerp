<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
    <link rel="stylesheet" href="static/ace/css/bootstrap-timepicker.css"/>
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

                        <form action="seckill/${msg }.do" name="Form" id="Form" method="post">
                            <input type="hidden" name="SECKILL_ID" id="SECKILL_ID" value="${pd.SECKILL_ID}"/>
                            <input type="hidden" name="SECKILL_STATE" id="SECKILL_STATE" value="${pd.SECKILL_STATE}"/>
                            <input type="hidden" name="ADD_TIME" id="ADD_TIME" value="${pd.ADD_TIME}"/>
                            <div id="zhongxin" style="padding-top: 13px;">
                                <table id="table_report" class="table table-striped table-bordered table-hover">
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">名称:</td>
                                        <td><input type="text" name="SECKILL_NAME" id="SECKILL_NAME"
                                                   value="${pd.SECKILL_NAME}" maxlength="100" placeholder="这里输入名称"
                                                   title="名称" style="width:98%;"/></td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">开始时间:</td>
                                        <td>
                                            <div class="input-group bootstrap-timepicker" style="width:98%;">
                                                <input class="form-control timepicker"
                                                       name="START_TIME" id="START_TIME" value="${pd.START_TIME}"
                                                       type="text" placeholder="开始时间" title="开始时间"/>
                                                <span class="input-group-addon">
													<i class="fa fa-clock-o bigger-110"></i>
												</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">结束时间:</td>
                                        <td>
                                            <div class="input-group bootstrap-timepicker" style="width:98%;">
                                                <input class="form-control timepicker"
                                                       name="END_TIME" id="END_TIME" value="${pd.END_TIME}"
                                                       type="text" placeholder="结束时间" title="结束时间"/>
                                                <span class="input-group-addon">
													<i class="fa fa-clock-o bigger-110"></i>
												</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:75px;text-align: right;padding-top: 13px;">备注:</td>
                                        <td><textarea name="SECKILL_MEMO" id="SECKILL_MEMO"
                                                      style="width:98%;height:100px;"
                                                      title="这里输入备注">${pd.SECKILL_MEMO}</textarea></td>
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
<script src="static/ace/js/date-time/bootstrap-timepicker.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
<script type="text/javascript">
    $(top.hangge());

    //时间框
    $('.timepicker').timepicker({
        minuteStep: 1,
        showSeconds: true,
        showMeridian: false
    }).next().on(ace.click_event, function () {
        $(this).prev().focus();
    });

    //保存
    function save() {
        if ($("#SECKILL_NAME").val() == "") {
            $("#SECKILL_NAME").tips({
                side: 3,
                msg: '请输入名称',
                bg: '#AE81FF',
                time: 2
            });
            $("#SECKILL_NAME").focus();
            return false;
        }
        if ($("#START_TIME").val() == "") {
            $("#START_TIME").tips({
                side: 3,
                msg: '请输入开始时间',
                bg: '#AE81FF',
                time: 2
            });
            $("#START_TIME").focus();
            return false;
        }
        if ($("#END_TIME").val() == "") {
            $("#END_TIME").tips({
                side: 3,
                msg: '请输入结束时间',
                bg: '#AE81FF',
                time: 2
            });
            $("#END_TIME").focus();
            return false;
        }
        $("#Form").submit();
        $("#zhongxin").hide();
        $("#zhongxin2").show();
    }

    /* $(function() {
     //日期框
     $('.timepicker').timepicker({autoclose: true,todayHighlight: true});
     });*/
</script>
</body>
</html>