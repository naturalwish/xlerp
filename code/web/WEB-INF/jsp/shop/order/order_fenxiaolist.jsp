<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String mallUploadUrl= (String)request.getSession().getAttribute("mall_upload_url");
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
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
	<style type="text/css">

		.order {
			border:0 solid;
			vertical-align: middle;
		}
	</style>
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
						<form action="order/list.do" method="post" name="Form" id="Form">
							<input type="hidden" name="status" id="status" value="${pd.status}"/>
							<!-- 检索  -->

							<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">
								<thead>
								<tr>
									<th class="center" style="width:35px;">
										<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">商品</th>
									<th class="center">单价</th>
									<th class="center">数量</th>
									<th class="center">售后</th>
									<th class="center">状态</th>
									<th class="center">实收款</th>
								</tr>
								</thead>

								<tbody>
								<!-- 开始循环 -->
								<c:choose>
									<c:when test="${not empty list}">
										<c:if test="${qx.cha == 1 }">
											<c:forEach items="${list}" var="var" varStatus="vs">
												<tr style="background: #dfe9f6">
													<td class='center'>
														<label class="pos-rel"><input type='checkbox' name='ids' value="${var.order_id}" class="ace" /><span class="lbl"></span></label>
													</td>
													<td class='center' style="width: 30px;">${vs.index+1}</td>

													<td colspan="1">订单号：${var.order_id}</td>
													<td colspan="1">买家：${var.username}</td>
													<td colspan="1">下单时间：${var.addtime}</td>
													<td colspan="1">发货时间：${var.invoicetime}</td>
													<td colspan="2">支付方式：
														<c:choose>
															<c:when test="${var.pay_way eq 'bank'}">
																<span style="color:rebeccapurple">货到付款</span>
															</c:when>
															<c:when test="${var.pay_way eq 'alipay'}">
																<span style="color:#108ee9">支付宝</span>
															</c:when>
															<c:when test="${var.pay_way eq 'wechat'}">
																<span style="color:green">微信</span>
															</c:when>
															<c:otherwise>
																<span style="color:indianred">未支付</span>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>

												<c:forEach items="${var.orderdetail}" var="order" varStatus="os">
													<tr class="order">
														<td class='center'>
															<label class="pos-rel"><span class="lbl"></span></label>
														</td>
														<td class='center' style="width: 30px;"></td>

														<td style="width:260px">
															<div style="float:left;width:60px;text-align: center;"><img src="<%=mallUploadUrl%>${order.goods_pic}" style="width: 50px;"></div>
															<div> ${order.goods_name}<p style="color:#868484;font-size: 10px ">${order.attribute_detail_name}&nbsp;&nbsp;${order.goods_number}</p></div>
														</td>
														<td class='center' style="width:100px;vertical-align:middle;">${order.goods_price}</td>
														<td class='center' style="width:100px;vertical-align:middle;">${order.goods_count}</td>
														<td class='center' style="width:100px;vertical-align:middle;">
															<a href="refund/torefund?order_detail_id=${order.order_detail_id}">
																<c:if test="${order.status==3}">待退款</c:if>
																<c:if test="${order.status==4}">退款完成</c:if>
															</a>
														</td>
														<td class='center' style="width:100px;vertical-align:middle;<c:if test="${os.count!=var.detaillength}"> border-bottom: 1px solid #f5f5f5;</c:if>">
															<c:if test="${os.index==0}">
																<c:if test="${var.status==0}">
																	待付款
																</c:if>
																<c:if test="${var.status==1}">
																	待发货
																</c:if>
																<c:if test="${var.status==2}">
																	已发货
																</c:if>
																<c:if test="${var.status==3}">
																	待退款
																</c:if>
																<c:if test="${var.status==4}">
																	退款成功
																</c:if>
																<c:if test="${order.status==5||order.status==6}">
																	交易成功
																</c:if>
																<c:if test="${order.status==99}">
																	已关闭
																</c:if>
															</c:if>
														</td>
														<td class='center' style="width:100px;vertical-align:middle;<c:if test="${os.count!=var.detaillength}"> border-bottom: 1px solid #f5f5f5;</c:if>">
															<c:if test="${os.index==0}">${var.order_total }</c:if>
														</td>
													</tr>
												</c:forEach>
											</c:forEach>
										</c:if>
										<c:if test="${qx.cha == 0 }">
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
    //检索
    function tosearch(){
        top.jzts();
        $("#Form").submit();
    }
    $(function() {

        //日期框
        $('.date-picker').datepicker({
            autoclose: true,
            todayHighlight: true
        });

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



    //删除
    function del(Id){
        bootbox.confirm("确定要删除吗?", function(result) {
            if(result) {
                top.jzts();
                var url = "<%=basePath%>order/delete.do?order_id="+Id+"&tm="+new Date().getTime();
                $.get(url,function(data){
                    nextPage(${page.currentPage});
                });
            }
        });
    }

    //关闭
    function orderClose(Id){
        bootbox.confirm("确定要关闭订单吗?", function(result) {
            if(result) {
                top.jzts();
                var url = "<%=basePath%>order/close.do?order_id="+Id+"&tm="+new Date().getTime();
                $.get(url,function(data){
                    nextPage(${page.currentPage});
                });
            }
        });
    }

    //退货
    function orderReturn(Id){
        bootbox.confirm("确定要退货吗?", function(result) {
            if(result) {
                top.jzts();
                var url = "<%=basePath%>order/returnBill.do?order_id="+Id+"&tm="+new Date().getTime();
                $.get(url,function(data){
                    nextPage(${page.currentPage});
                });
            }
        });
    }

    //修改
    function edit(Id){
        top.jzts();
        alert(1111);
        var diag = new top.Dialog();
        diag.Drag=true;
        diag.Title ="编辑";
        diag.URL = '<%=basePath%>order/goEdit.do?order_id='+Id;
        diag.Width = 450;
        diag.Height = 355;
        diag.CancelEvent = function(){ //关闭事件
            if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
                nextPage(${page.currentPage});
            }
            diag.close();
        };
        diag.show();
    }

    //批量操作
    function makeAll(msg){
        bootbox.confirm(msg, function(result) {
            if(result) {
                var str = '';
                for(var i=0;i < document.getElementsByName('ids').length;i++){
                    if(document.getElementsByName('ids')[i].checked){
                        if(str=='') str += document.getElementsByName('ids')[i].value;
                        else str += ',' + document.getElementsByName('ids')[i].value;
                    }
                }
                if(str==''){
                    bootbox.dialog({
                        message: "<span class='bigger-110'>您没有选择任何内容!</span>",
                        buttons:
                            { "button":{ "label":"确定", "className":"btn-sm btn-success"}}
                    });
                    $("#zcheckbox").tips({
                        side:1,
                        msg:'点这里全选',
                        bg:'#AE81FF',
                        time:8
                    });
                    return;
                }else{
                    if(msg == '确定要删除选中的数据吗?'){
                        top.jzts();
                        $.ajax({
                            type: "POST",
                            url: '<%=basePath%>order/deleteAll.do?tm='+new Date().getTime(),
                            data: {DATA_IDS:str},
                            dataType:'json',
                            //beforeSend: validateData,
                            cache: false,
                            success: function(data){
                                $.each(data.list, function(i, list){
                                    nextPage(${page.currentPage});
                                });
                            }
                        });
                    }
                }
            }
        });
    };

    //导出excel
    function toExcel(){
        window.location.href='<%=basePath%>order/excel.do';
    }
</script>


</body>
</html>