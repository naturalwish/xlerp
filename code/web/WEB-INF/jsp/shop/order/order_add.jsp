<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
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
    <style type="text/css">
        p {
            padding-top: 7px;
        }

        #dialog-add, #dialog-message, #dialog-comment {
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0px;
            z-index: 10000;
            display: none;
        }

        .commitopacity {
            position: absolute;
            width: 100%;
            height: 100%;
            background: #7f7f7f;
            filter: alpha(opacity=50);
            -moz-opacity: 0.5;
            -khtml-opacity: 0.5;
            opacity: 0.5;
            top: 0px;
            z-index: 99999;
        }

        .commitbox {
            width: 80%;
            padding-left: 20%;
            padding-top: 60px;
            position: absolute;
            top: 0px;
            z-index: 99999;
        }

        .commitbox_inner {
            width: 96%;
            height: 235px;
            margin: 6px auto;
            background: #efefef;
            border-radius: 5px;
        }

        .commitbox_top {
            width: 100%;
            height: 233px;
            margin-bottom: 10px;
            padding-top: 10px;
            background: #FFF;
            border-radius: 5px;
            box-shadow: 1px 1px 3px #e8e8e8;
        }

        .commitbox_top textarea {
            width: 95%;
            height: 165px;
            display: block;
            margin: 0px auto;
            border: 0px;
        }

        .commitbox_cen {
            width: 60%;
            height: 40px;
            padding-top: 10px;
        }

        .commitbox_cen div.left {
            float: left;
            background-size: 15px;
            background-position: 0px 3px;
            padding-left: 18px;
            color: #f77500;
            font-size: 16px;
            line-height: 27px;
        }

        .commitbox_cen div.left img {
            width: 30px;
        }

        .commitbox_cen div.right {
            float: right;
            margin-top: 7px;
        }

        .commitbox_cen div.right span {
            cursor: pointer;
        }

        .commitbox_cen div.right span.save {
            border: solid 1px #c7c7c7;
            background: #6FB3E0;
            border-radius: 3px;
            color: #FFF;
            padding: 5px 10px;
        }

        .commitbox_cen div.right span.quxiao {
            border: solid 1px #f77400;
            background: #f77400;
            border-radius: 3px;
            color: #FFF;
            padding: 4px 9px;
        }

    </style>
</head>

<body class="no-skin">

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <input type="hidden" name="msgIndex" id="msgIndex" value=""/>
        <input type="hidden" name="hcdname" id="hcdname" value=""/>
        <div class="main-content-inner">
            <div class="page-content">
                <div class="hr hr-18 dotted hr-double"></div>
                <div class="row">
                    <div class="col-xs-12">
                        <div id="dialog-add">
                            <div class="commitopacity"></div>
                            <div class="commitbox">
                                <div class="commitbox_inner">
                                    <div class="commitbox_top">
                                        <br/>
                                        <table id="table_MX" class="table table-striped table-bordered table-hover">
                                            <tr>
                                                <td style="width:15%;text-align: right;padding-top: 13px;">商品名称：</td>
                                                <td><input type="text" id="goods_name" value="" maxlength="255"
                                                           title="点击选择商品" style="width:90%;" onclick="goodOpen();"
                                                           placeholder="点击选择商品"/>
                                                    <%--<a class="btn  btn-info btn-xs" ><i class="ace-icon glyphicon glyphicon-search"></i>选择商品</a>--%>
                                                </td>

                                                <td style="width:15%;text-align: right;padding-top: 13px;">规格型号：</td>
                                                <td>
                                                    <select id="attribute_detail_name" name="attribute_detail_name" onchange="change1()" style="width: 90%">
                                                        <option>请选择</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="width:15%;text-align: right;padding-top: 13px;">单价：</td>
                                                <td><input type="number" id="goods_price" value="" maxlength="255" min="0.0" step="0.01"
                                                           title="单价" style="width:90%;" readonly/></td>
                                                <td style="width:15%;text-align: right;padding-top: 13px;">数量：</td>
                                                <td><input type="number" id="goods_count" value="" maxlength="255"
                                                           title="数量" style="width:90%;"/></td>
                                            </tr>
                                        </table>
                                        <input name="goods_id" id="goods_id" value="" type="hidden"/>
                                        <input name="goods_pic" id="goods_pic" value="" type="hidden"/>
                                        <div class="col-sm-9"><a class="btn btn-mini btn-primary"
                                                                 onClick="saveDD()">添加</a>
                                            <a class="btn btn-mini btn-danger" onClick="cancel_pl()">取消</a>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 存放生成的hmlt开头  -->
                        <!-- <form class="form-horizontal" role="form"> -->
                        <form action="order/save.do" name="Form" id="Form" method="post" class="form-horizontal">
                            <input type="hidden" name="order_id" id="order_id" value="${pd.order_id}"/>
                            <input type="hidden" name="zindex" id="zindex" value="0">
                            <input type="hidden" name="FIELDLIST" id="FIELDLIST" value="">
                            <input type="hidden" name="user_id" id="user_id" value="">
                            <table id="table_report" class="table table-striped table-bordered table-hover">
                                <tr>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">商城单号：</td>
                                    <td><input type="text" name="" id="" value="${pd.order_id}" maxlength="255"
                                               title="商城单号" style="width:75%;" disabled/></td>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">订单时间：</td>
                                    <td><input type="text" name="addtime" id="addtime" value="${pd.addtime}"
                                               maxlength="255" title="订单时间" style="width:75%;" disabled/></td>
                                </tr>
                                <tr>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">用户昵称：</td>
                                    <td><input type="text" name="username" id="username" value="${pd.username}"
                                               maxlength="255" title="点击选择用户" style="width:75%;" onclick="userOpen()"
                                               placeholder="点击选择用户"/>
                                        <%--<a class="btn  btn-info btn-xs" onclick="user_Open();"><i class="ace-icon glyphicon glyphicon-search"></i>选择用户</a>--%>
                                    </td>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">用户手机：</td>
                                    <td><input type="number" name="phone" id="phone" value="${pd.phone}" maxlength="255"
                                               placeholder="" title="用户手机" style="width:75%;" disabled/></td>
                                </tr>
                                <tr>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">收货人名称：</td>
                                    <td><input type="text" name="addr_realname" id="addr_realname" value=""
                                               maxlength="255" placeholder="这里输入收货人名称" title="收货人名称"
                                               style="width:75%;"/></td>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">收货人电话：</td>
                                    <td><input type="number" name="addr_phone" id="addr_phone" value="" maxlength="255"
                                               placeholder="这里输入收货人电话" title="收货人电话" style="width:75%;"/></td>
                                </tr>
                                <tr>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">收货人地址：</td>
                                    <td colspan="3"><input type="text" name="address" id="address" value="" maxlength="255"
                                               placeholder="这里输入收货人地址" title="收货人地址" style="width:89%;"/></td>
                                </tr>
                                <tr>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">优惠：</td>
                                    <td><input type="number" name="coupon_price" id="coupon_price" value="0"
                                               maxlength="255" placeholder="优惠金额" title="优惠金额" style="width:75%;"/></td>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">运费：</td>
                                    <td><input type="number" name="freight_price" id="freight_price" value="0"
                                               maxlength="255" placeholder="这里输入运费" title="运费" style="width:75%;"/></td>
                                </tr>
                                <tr>
                                    <td style="width:10%;text-align: right;padding-top: 13px;">留言：</td>
                                    <td colspan="3"><input type="text" name="buyermessage" id="buyermessage" value=""
                                               maxlength="255" placeholder="买家留言" title="买家留言" style="width:89%;"/></td>
                                </tr>
                            </table>
                            <table id="table_report2">
                                <tr>
                                    <td style="text-align: left;" colspan="100">
                                        <a class="btn  btn-success btn-xs" onclick="dialog_open();"><i
                                                class="ace-icon glyphicon glyphicon-plus"></i>新增商品</a>
                                    </td>
                                </tr>
                            </table>
                            <table id="simple-table" class="table table-striped table-bordered table-hover"
                                   style="margin-top:5px;">
                                <thead>
                                <tr>
                                    <th class="center" width="10%">序号</th>
                                    <th class="center" width="30%">商品名称</th>
                                    <th class="center" width="15%">规格型号</th>
                                    <th class="center" width="10%">单价</th>
                                    <th class="center" width="10%">数量</th>
                                    <th class="center" width="10%">金额</th>
                                    <th class="center" width="15%">操作</th>
                                    <th class="center" style="display:none"></th>
                                    <th class="center" style="display:none"></th>
                                </tr>
                                </thead>

                                <tbody id="fields"></tbody>
                            </table>
                        </form>
                        <!-- 存放生成的hmlt结尾 -->

                    </div>
                    <!-- /.col -->
                </div>
                <!-- /.row -->
            </div>
            <!-- /.page-content -->
        </div>
    </div>
    <!-- /.main-content -->


    <!-- 返回顶部 -->
    <a href="#" id="btn-scroll-up"
       class="btn-scroll-up btn btn-sm btn-inverse"> <i
            class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>

</div>
<footer>
    <div style="width: 100%;padding-bottom: 2px;" class="center">
        <a class="btn btn-mini btn-primary" onclick="saveOrder();">保存</a>
        <a class="btn btn-mini btn-danger" href="order/list.do">前往订单列表</a>
    </div>
</footer>
<!-- /.main-container -->

<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../../system/index/foot.jsp" %>
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
<script src="static/js/myjs/orderAdd.js"></script>
<script type="text/javascript">
    $(top.hangge());//关闭加载状态
    $(reductionFields());
    //修改时还原属性列表
    function reductionFields() {
        var msg = '${msg}';
        if ('edit' == msg) {
            var nowarField = '${pd.FIELDLIST}';
            var fieldarray = nowarField.split('Q2012');
            for (var i = 0; i < fieldarray.length; i++) {
                if (fieldarray[i] != '') {
                    appendC(fieldarray[i]);
                    arField[i] = fieldarray[i];
                }
            }
        }
    }
    //新增用户
    function userOpen() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "选择用户";
        diag.URL = '<%=basePath%>happuser/goBox.do';
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
                    $("#username").val(tbodyObj.rows[key].cells[3].innerHTML+"("+tbodyObj.rows[key].cells[2].innerHTML+")");
                    $("#user_id").val(tbodyWin.$("#selectStr").val());
                    return false;
                }
            });
            diag.close();
        };
        diag.show();
    }

    //新增商品
    function goodOpen() {
        top.jzts();
        var diag = new top.Dialog();
        diag.Drag = true;
        diag.Title = "添加商品";
        diag.URL = '<%=basePath%>goods/goBox.do';
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
                    $("#goods_name").val(tbodyObj.rows[key].cells[4].innerHTML);
                    $("#goods_id").val(tbodyWin.$("#selectStr").val());
                    $("#goods_pic").val(tbodyObj.rows[key].cells[6].innerHTML);
                    //$("#goods_price").val(tbodyObj.rows[key].cells[6].innerHTML);
                    //$("#attribute_detail_name").val(tbodyObj.rows[key].cells[5].innerHTML);
                    return false;
                }
            });
            diag.close();
            $.ajax({
                type: "POST",
                url: '<%=basePath%>attribute/getAttribute.do?',
                data: {GOODS_ID:$("#goods_id").val()},
                dataType:'json',
                cache: false,
                success: function(data){
                    $("#attribute_detail_name").html('<option>请选择</option>');
                    $.each(data.list, function(i, dvar){
                        $("#attribute_detail_name").append("<option value="+dvar.ATTRIBUTE+ " ATTRIBUTEPRICE="+dvar.ATTRIBUTEPRICE+
                        ">"+dvar.ATTRIBUTE+"</option>");
                    });
                }
            });
        };
        diag.show();
    }

    function change1(){
        var options=$("#attribute_detail_name option:selected"); //获取选中的项
        //alert(options.val()); //拿到选中项的值
        //alert(options.text()); //拿到选中项的文本
        //alert(options.attr('url')); //拿到选中项的url值
        $("#goods_price").val(options.attr('ATTRIBUTEPRICE'));
    }
</script>

</body>
</html>