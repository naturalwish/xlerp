<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
		p {
			padding-top: 7px;
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
				<div class="hr hr-18 dotted hr-double"></div>
				<div class="row">
					<div class="col-xs-12">

						<!-- 存放生成的hmlt开头  -->
						<!-- <form class="form-horizontal" role="form"> -->
						<form action="order/${msg}.do" name="Form" id="Form" method="post" class="form-horizontal">
							<input type="hidden" name="order_id" id="order_id" value="${pd.order_id}"/>
							<input type="hidden" name="user_id" id="user_id" value="${pd.user_id}"/>
							<div id="zhongxin" style="padding-top: 13px;">
								<table id="table_report" class="table table-striped table-bordered table-hover">
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">商城单号：</td>
										<td><input type="text" name="" id="" value="${pd.order_id}" maxlength="255" title="商城单号" style="width:75%;" readonly/></td>
										<td style="width:10%;text-align: right;padding-top: 13px;">订单时间：</td>
										<td><input type="date" name="addtime" id="addtime" value="${pd.addtime}" maxlength="255" title="订单时间" style="width:75%;" readonly/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">付款时间：</td>
										<td><input type="date" name="paymenttime" id="paymenttime" value="${pd.paymenttime}" maxlength="255" title="付款时间" style="width:75%;" readonly/></td>
										<td style="width:10%;text-align: right;padding-top: 13px;">发货时间：</td>
										<td><input type="date" name="invoicetime" id="invoicetime" value="${pd.invoicetime}" maxlength="255" title="发货时间" style="width:75%;" readonly/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">结束时间：</td>
										<td><input type="date" name="endtime" id="endtime" value="${pd.endtime}" maxlength="255" title="结束时间" style="width:75%;" readonly/></td>
										<td style="width:10%;text-align: right;padding-top: 13px;">关闭时间：</td>
										<td><input type="date" name="closetime" id="closetime" value="${pd.closetime}" maxlength="255" title="关闭时间" style="width:75%;" readonly/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">用户昵称：</td>
										<td><input type="text" name="username" id="username" value="${pd.username}" maxlength="255" title="用户昵称" style="width:75%;" readonly/></td>
										<td style="width:10%;text-align: right;padding-top: 13px;">用户手机：</td>
										<td><input type="text" name="phone" id="phone" value="${pd.phone}" maxlength="255" placeholder="" title="用户手机" style="width:75%;" readonly/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">收货人名称：</td>
										<td><input type="text" name="addr_realname" id="addr_realname" value="${pd.addr_realname}" maxlength="255" placeholder="这里输入收货人名称" title="收货人名称" style="width:75%;"/></td>
										<td style="width:10%;text-align: right;padding-top: 13px;">收货人电话：</td>
										<td><input type="text" name="addr_phone" id="addr_phone" value="${pd.addr_phone}" maxlength="255" placeholder="这里输入收货人电话" title="收货人电话" style="width:75%;"/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">收货人地址：</td>
										<td colspan="3"><input type="text" name="address" id="address" value="${pd.address}" maxlength="255" placeholder="这里输入收货人地址" title="收货人地址" style="width:89%;"/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">留言：</td>
										<td colspan="3"><input type="text" name="buyermessage" id="buyermessage" value="${pd.buyermessage}"
															   maxlength="255" placeholder="买家留言" title="买家留言" style="width:89%;"/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">优惠额度：</td>
										<td><input type="text" name="coupon_price" id="coupon_price" value="${pd.coupon_price}" maxlength="255" placeholder="这里输入优惠金额" title="优惠金额" style="width:75%;"/></td>
										<td style="width:10%;text-align: right;padding-top: 13px;">运费：</td>
										<td><input type="text" name="freight_price" id="freight_price" value="${pd.freight_price}" maxlength="255" placeholder="这里输入运费" title="运费" style="width:75%;"/></td>
									</tr>
									<tr>
										<td style="width:10%;text-align: right;padding-top: 13px;">支付方式：</td>
										<td colspan="3" style="text-align: left;padding-top: 13px;">
											<c:choose>
												<c:when test="${pd.pay_way eq 'bank'}">
													<span style="color:rebeccapurple">货到付款</span>
												</c:when>
												<c:when test="${pd.pay_way eq 'alipay'}">
													<span style="color:#108ee9">支付宝</span>
												</c:when>
												<c:when test="${pd.pay_way eq 'wechat'}">
													<span style="color:green">微信</span>
												</c:when>
												<c:otherwise>
													<span style="color:indianred">未支付</span>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>

								</table>


								<div class="col-md-12" >
									<div class="form-group" style="margin-bottom:10px">
										<label class="col-sm-3 control-label no-padding-right"
											   for="form-field-1">选择快递：</label>
										<div class="col-sm-9" style="width:20%">
											<select class="chosen-select form-control" name="express_title" id="express_title" onchange="dh()" style="height:27px;width:200px;margin-top: 5px">
												<option value="">请选择</option>
												<option value="wxkd" <c:if test="${'wxkd' eq pd.express_title}">selected</c:if>>无需快递</option>
												<c:forEach items="${express}" var="express">
													<option value="${express.EXPRESS_CODE}" <c:if test="${'wxkd' ne pd.EXPRESS_CODE}"><c:if test="${express.EXPRESS_CODE eq pd.express_title}">selected</c:if></c:if> >${express.EXPRESS_NAME}</option>
												</c:forEach>
											</select>
										</div>
										<div id="dh" <c:if test="${'wxkd' eq pd.express_title}">style="display:none"</c:if>>
											<label class="col-sm-3 control-label no-padding-right"
												   for="form-field-1">快递单号：</label>
											<div class="col-sm-9"style="width:20%" >
												<input type="text" id="express_num"  value="${pd.express_num}" placeholder="输入快递单号" style="height: 27px;width:180px;margin-top: 5px">
												<c:if test="${!empty pd.express_num}">
													<!--<a id="getdiv"  data-toggle="modal" data-target="#myModal">查询</a><!-- 陈清辉 说不要查询 -->
												</c:if>
											</div>
										</div>
									</div>
								</div>
								<div class="col-md-12">
									<div class="form-group">
										<label class="col-sm-3 control-label no-padding-right"
											   for="form-field-1"></label>
										<div class="col-sm-9">
											<c:if test="${pd.status==1||pd.status==2}">
												<a class="btn btn-mini btn-primary" onclick="send('${pd.order_id}')">发货</a>
											</c:if>
										</div>
									</div>
								</div>
								<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">
									<thead>
									<tr>
										<th class="center">商品</th>
										<th class="center">单价</th>
										<th class="center">数量</th>
										<th class="center">售后</th>
										<th class="center">状态</th>
										<th class="center">优惠</th>
										<th class="center">运费</th>
										<th class="center">实收款</th>
									</tr>
									</thead>

									<c:forEach items="${orderdetail}" var="order" varStatus="os">
										<tr>
											<td style="width:360px">
												<div style="float:left;width:60px;text-align: center;"><img src="<%=mallUploadUrl%>${order.goods_pic}" style="width: 50px;"></div>
												<div style=""> ${order.goods_name}<p style="color:#868484;font-size: 10px ">${order.attribute_detail_name}</p></div>
											</td>
											<td class='center' style="width:100px;vertical-align:middle;">${order.goods_price}</td>
											<td class='center' style="width:100px;vertical-align:middle;">${order.goods_count}</td>
											<td class='center' style="width:100px;vertical-align:middle;">
												<a href="refund/torefund?order_detail_id=${order.order_detail_id}">
													<c:if test="${order.status==3}">待退款</c:if>
													<c:if test="${order.status==4}">退款完成</c:if>
												</a>
											</td>
											<td class='center' style="width:100px;vertical-align:middle;<c:if test="${os.count!=pd.detaillength}"> border-bottom: 1px solid #f5f5f5;</c:if>">
												<c:if test="${os.index==0}">
													<c:if test="${order.status==0}">
														待付款
													</c:if>
													<c:if test="${order.status==1}">
														待发货
													</c:if>
													<c:if test="${order.status==2}">
														已发货
													</c:if>
													<c:if test="${order.status==3}">
														待退款
													</c:if>
													<c:if test="${order.status==4}">
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
											<td class='center' style="width:100px;vertical-align:middle;<c:if test="${os.count!=pd.detaillength}"> border-bottom: 1px solid #f5f5f5;</c:if>">${order.coupon_price}</td>
											<td class='center' style="width:100px;vertical-align:middle;<c:if test="${os.count!=pd.detaillength}"> border-bottom: 1px solid #f5f5f5;</c:if>">${order.freight_price}</td>
											<td class='center' style="width:100px;vertical-align:middle;<c:if test="${os.count!=pd.detaillength}"> border-bottom: 1px solid #f5f5f5;</c:if>">
												<c:if test="${os.index==0}">${pd.order_total }</c:if>
											</td>
										</tr>
									</c:forEach>
								</table>


								<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
									<div class="modal-dialog" role="document">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal" aria-label="Close">
													<span aria-hidden="true">×</span>
												</button>
												<h4 class="modal-title" id="myModalLabel">${pd.express_name}：${pd.express_num}</h4>
											</div>
											<table id="show-express" class="table table-striped table-bordered table-hover">
											</table>
											<div class="modal-footer">
												<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
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
		<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
		<a class="btn btn-mini btn-danger"  onclick="window.location.href = document.referrer;">返回</a>
	</div>
</footer>
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

    function send(order_id) {
        var express_title = $('#express_title').val();
        var express_name = encodeURIComponent($('#express_title').find(
            "option:selected").text());
        var express_num = $('#express_num').val();
        var orderaddtime = $('#addtime').val();
        var addr_realname = $('#addr_realname').val();
        if (express_title == '') {
            alert('请选择快递');
            return;
        }

        if (express_title != 'wxkd') {
            if (express_num == '') {
                alert('请填写快递单号');
                return;
            }
        }
        $.ajax({
            url : 'order/send',
            type : 'post',
            data : {
                order_id : order_id,
                express_name : express_name,
                express_title : express_title,
                express_num : express_num,
                orderaddtime : orderaddtime,
                addr_realname : addr_realname
            },
            success : function(data) {
                alert(data.message);
                if (data.result == 1) {
                    window.location.href = document.referrer;
                }
            }
        })
    }

    function dh() {
        var express_title = $('#express_title').val();
        if (express_title == 'wxkd') {
            $('#dh').hide();
        } else {
            $('#dh').show();
        }
    }
    $('#myModal').on('shown.bs.modal', function (e) {
        var express_title = $('#express_title').val();
        var express_num = $('#express_num').val();
        $.ajax({
            url : 'express/info',
            type : 'post',
            data : {
                express_title : express_title,
                express_num : express_num
            },
            success : function(data) {
                if (data.Success == true) {
                    $('#show-express').html('');
                    if(data.State==0){
                        $('#show-express').html('<p>'+data.Reason+'</p>');
                        return;
                    }
                    $.each(data.Traces,function(i,item){
                        $('#show-express').append('<p>'+item.AcceptTime+'&nbsp;&nbsp;'+item.AcceptStation+'</p>');
                    })
                }
                else{
                    $('#show-express').html('<p>'+data.Reason+'</p>');
                }
            }
        })
    });
    //下面可以使用单号查询快递
    /*$('#myModal').on('shown.bs.modal', function (e) {
        var express_title = $('#express_title').val();
        var express_num = $('#express_num').val();
        $('#show-express').html('<tr><th class="center" colspan="2">物流跟踪记录</th></tr>');
        $.ajax({
            url : 'tool/queryWuliujilu.do?tm='+new Date().getTime(),
            type : 'post',
            data: {number:express_num},
            dataType:'json',
            cache: false,
            success : function(data) {
                if (data.msg == "ok") {
                    var item = jQuery.parseJSON(data.value);
                    if("ok" == item.msg){
                        $.each(item.result.list, function(i, list){
                            $("#show-express").append('<tr><td width="200" class="center">'+list.time+'</td><td>'+list.status+'</td></tr>');
                        });
                    }else{
                        $("#show-express").append('<td class="center" colspan="2">'+item.msg+'</td>');
                    }
                } else{
                    $("#show-express").append('<td class="center" colspan="2">没有查询到记录</td>');
                }
            }
        })
    });*/

    //保存
    function save(){
        $("#Form").submit();
        $("#zhongxin").hide();
        $("#zhongxin2").show();
        window.location.reload();
    }
</script>


</body>
</html>