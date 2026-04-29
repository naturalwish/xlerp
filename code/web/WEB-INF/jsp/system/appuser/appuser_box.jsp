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
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <!-- 下拉框 -->
    <link rel="stylesheet" href="static/ace/css/chosen.css" />
    <!-- jsp文件头和头部 -->
    <%@ include file="../index/top.jsp"%>
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
                        <!-- 检索  -->
                        <form action="happuser/goBox.do" method="post" name="userForm" id="userForm">
                            <input type="hidden" name="selectStr" id="selectStr" value=""/>
                            <table style="margin-top:5px;">
                                <tr>
                                    <td>
                                        <div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
                                        </div>
                                    </td>
                                    <td style="padding-left:2px;"><input class="span10 date-picker" name="lastLoginStart" id="lastLoginStart"  value="${pd.lastLoginStart}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="最近登录开始"/></td>
                                    <td style="padding-left:2px;"><input class="span10 date-picker" name="lastLoginEnd" name="lastLoginEnd"  value="${pd.lastLoginEnd}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="最近登录结束"/></td>
                                    <td style="vertical-align:top;padding-left:2px;">
                                        <select class="chosen-select form-control" name="ROLE_ID" id="role_id" data-placeholder="请选择角色" style="vertical-align:top;width: 120px;">
                                            <option value=""></option>
                                            <option value="">全部</option>
                                            <c:forEach items="${roleList}" var="role">
                                                <option value="${role.ROLE_ID }" <c:if test="${pd.ROLE_ID==role.ROLE_ID}">selected</c:if>>${role.ROLE_NAME }</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td style="vertical-align:top;padding-left:2px;">
                                        <select class="chosen-select form-control" name="STATUS" id="STATUS" data-placeholder="状态" style="vertical-align:top;width: 79px;">
                                            <option value=""></option>
                                            <option value="">全部</option>
                                            <option value="1" <c:if test="${pd.STATUS == '1' }">selected</c:if> >正常</option>
                                            <option value="0" <c:if test="${pd.STATUS == '0' }">selected</c:if> >冻结</option>
                                        </select>
                                    </td>
                                    <c:if test="${QX.cha == 1 }">
                                        <td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
                                        <c:if test="${QX.toExcel == 1 }"><td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a></td></c:if>
                                    </c:if>
                                </tr>
                            </table>
                            <!-- 检索  -->
                            <table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">
                                <thead>
                                <tr>
                                    <th class="center" style="width:35px;">
                                        <label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
                                    </th>
                                    <th class="center" style="width:50px;">序号</th>
                                    <th class="center">用户名</th>
                                    <th class="center">姓名</th>
                                    <th class="center">等级</th>
                                    <th class="center"><i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>到期日期</th>
                                    <th class="center">年限</th>
                                    <th class="center"><i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>最近登录</th>
                                    <th class="center">上次登录IP</th>
                                    <th class="center">状态</th>
                                </tr>
                                </thead>
                                <tbody>
                                <!-- 开始循环 -->
                                <c:choose>
                                    <c:when test="${not empty userList}">
                                        <c:if test="${QX.cha == 1 }">
                                            <c:forEach items="${userList}" var="user" varStatus="vs">
                                                <tr>
                                                    <td class='center' style="width: 30px;">
                                                        <label><input type='checkbox' name='ids' value="${user.USER_ID }" id="${user.EMAIL }" alt="${user.PHONE }" class="ace"/><span class="lbl"></span></label>
                                                    </td>
                                                    <td class='center' style="width: 30px;">${vs.index+1}</td>
                                                    <td class="center">${user.USERNAME }</td>
                                                    <td class="center">${user.NAME }</td>
                                                    <td class="center">${user.ROLE_NAME }</td>
                                                    <td class="center">${user.END_TIME }</td>
                                                    <td class="center">${user.YEARS }</td>
                                                    <td class="center">${user.LAST_LOGIN}</td>
                                                    <td class="center">${user.IP}</td>
                                                    <td style="width: 60px;" class="center">
                                                        <c:if test="${user.STATUS == '0' }"><span class="label label-important arrowed-in">冻结</span></c:if>
                                                        <c:if test="${user.STATUS == '1' }"><span class="label label-success arrowed">正常</span></c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${QX.cha == 0 }">
                                            <tr>
                                                <td colspan="100" class="center">您无权查看</td>
                                            </tr>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="main_info">
                                            <td colspan="100" class="center" >没有相关数据</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                            <div class="page-header position-relative">
                                <table style="width:100%;">
                                    <tr>
                                        <td style="vertical-align:top;">
                                            <a class="btn btn-mini btn-primary" onclick="confirm();">确定</a>
                                            <a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
                                        </td>
                                        <td style="vertical-align:top;">
                                            <div class="pagination"
                                                 style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
                                        </td>
                                    </tr>
                                </table>
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
    <!-- 返回顶部 -->
    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div>
<!-- /.main-container -->
<!-- basic scripts -->
<!-- 页面底部js¨ -->
<%@ include file="../index/foot.jsp"%>
<!-- 删除时确认窗口 -->
<script src="static/ace/js/bootbox.js"></script>
<!-- ace scripts -->
<script src="static/ace/js/ace/ace.js"></script>
<!-- 日期框 -->
<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
<!-- 下拉框 -->
<script src="static/ace/js/chosen.jquery.js"></script>
<!--提示框-->
<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript">
    $(top.hangge());
    //检索
    function confirm() {
        var selectStr = "";
        var ids = $("input[name='ids']");
        for (var i = 0; i < ids.length; i++) {
            if (ids[i].checked) {
                if (selectStr == '') {
                    selectStr += ids[i].value;
                } else {
                    selectStr += ',' + ids[i].value;
                }
            }
        }
        if (selectStr == '') {
            bootbox.dialog({
                message: "<span class='bigger-110'>您没有选择任何内容!</span>",
                buttons: {"button": {"label": "确定", "className": "btn-sm btn-success"}}
            });
            $("#zcheckbox").tips({
                side: 1,
                msg: '点这里全选',
                bg: '#AE81FF',
                time: 8
            });
            return;
        }
        $("#selectStr").val(selectStr);
        top.Dialog.close();
    }

    $(function() {
        //日期框
        $('.date-picker').datepicker({autoclose: true,todayHighlight: true});

        //下拉框
        if(!ace.vars['touch']) {
            $('.chosen-select').chosen({allow_single_deselect:true});
            $(window)
                .off('resize.chosen')
                .on('resize.chosen', function() {
                    $('.chosen-select').each(function() {
                        var $this = $(this);
                        $this.next().css({'width': $this.parent().width()});
                    });
                }).trigger('resize.chosen');
            $(document).on('settings.ace.chosen', function(e, event_name, event_val) {
                if(event_name != 'sidebar_collapsed') return;
                $('.chosen-select').each(function() {
                    var $this = $(this);
                    $this.next().css({'width': $this.parent().width()});
                });
            });
            $('#chosen-multiple-style .btn').on('click', function(e){
                var target = $(this).find('input[type=radio]');
                var which = parseInt(target.val());
                if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
                else $('#form-field-select-4').removeClass('tag-input-style');
            });
        }

        //复选框全选控制
        var active_class = 'active';
        $('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
            var th_checked = this.checked;//checkbox inside "TH" table header
            $(this).closest('table').find('tbody > tr').each(function(){
                var row = this;
                if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
                else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
            });
        });
    });

    //检索
    function searchs(){
        top.jzts();
        $("#userForm").submit();
    }

</script>
</html>
