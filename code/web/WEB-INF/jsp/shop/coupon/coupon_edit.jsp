<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

					<form action="coupon/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="COUPON_ID" id="COUPON_ID" value="${pd.COUPON_ID}"/>
						<input type="hidden" name="ADDTIME" id="ADDTIME" value="${pd.ADDTIME}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">名称:</td>
								<td><input type="text" name="COUPON_NAME" id="COUPON_NAME" value="${pd.COUPON_NAME}" maxlength="100" placeholder="这里输入名称" title="名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">领取方式:</td>
								<td><select name="COUPON_TYPE" id="COUPON_TYPE" style="width:98%;" onchange="showChange()">
									<option value="1" <c:if test="${pd.COUPON_TYPE==1}">selected</c:if>>网页</option>
									<option value="2" <c:if test="${pd.COUPON_TYPE==2}">selected</c:if>>兑换码</option>
								</select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">开始时间:</td>
								<td><input class="span10 date-picker" name="STARTTIME" id="STARTTIME" value="${pd.STARTTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="开始时间" title="开始时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">结束时间:</td>
								<td><input class="span10 date-picker" name="ENDTIME" id="ENDTIME" value="${pd.ENDTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="结束时间" title="结束时间" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">优惠券价格:</td>
								<td><input type="number" name="COUPON_PRICE" id="COUPON_PRICE" value="${pd.COUPON_PRICE}" maxlength="32" placeholder="这里输入优惠券价格" title="优惠券价格" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">使用额度:</td>
								<td><input type="number" name="USE_PRICE" id="USE_PRICE" value="${pd.USE_PRICE}" maxlength="32" placeholder="这里输入使用额度" title="使用额度" style="width:98%;"/></td>
							</tr>
                            <tr id="change" <c:if test="${pd.COUPON_TYPE!=2}">style="display: none"</c:if>>
								<td style="width:75px;text-align: right;padding-top: 13px;">兑换码:</td>
								<td><input type="text" name="CHANGE_CODE" id="CHANGE_CODE" value="${pd.CHANGE_CODE}" maxlength="255" placeholder="这里输入兑换码" title="兑换码" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">优惠券数量:</td>
								<td><input type="number" name="COUPON_NUM" id="COUPON_NUM" value="${pd.COUPON_NUM}" maxlength="32" placeholder="这里输入优惠券数量" title="优惠券数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
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


	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());

        function showChange(){
            var coupon_type = $('#COUPON_TYPE').val();
            if(coupon_type==2){
                $('#change').show();
            }else{
                $('#change').hide();
            }
        }

		//保存
		function save(){
            var coupon_type = $('#COUPON_TYPE').val();
            if($("#COUPON_NAME").val()==""){
				$("#COUPON_NAME").tips({
					side:3,
		            msg:'请输入名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COUPON_NAME").focus();
			return false;
			}
			if($("#COUPON_TYPE").val()==""){
				$("#COUPON_TYPE").tips({
					side:3,
		            msg:'请输入领取方式',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COUPON_TYPE").focus();
			return false;
			}
			if($("#STARTTIME").val()==""){
				$("#STARTTIME").tips({
					side:3,
		            msg:'请输入开始时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STARTTIME").focus();
			return false;
			}
			if($("#ENDTIME").val()==""){
				$("#ENDTIME").tips({
					side:3,
		            msg:'请输入结束时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ENDTIME").focus();
			return false;
			}
            if($("#STARTTIME").val()>$("#ENDTIME").val()){
                $("#ENDTIME").tips({
                    side:3,
                    msg:'结束时间必须大于等于开始时间',
                    bg:'#AE81FF',
                    time:2
                });
                $("#ENDTIME").focus();
                return ;
            }
			if($("#COUPON_PRICE").val()==""){
				$("#COUPON_PRICE").tips({
					side:3,
		            msg:'请输入优惠券价格',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COUPON_PRICE").focus();
			return false;
			}
			if($("#USE_PRICE").val()==""){
				$("#USE_PRICE").tips({
					side:3,
		            msg:'请输入使用额度',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#USE_PRICE").focus();
			return false;
			}
            if(parseFloat($("#USE_PRICE").val())<parseFloat($("#COUPON_PRICE").val())){
                $("#USE_PRICE").tips({
                    side:3,
                    msg:'使用额度必须大于等于优惠券价格',
                    bg:'#AE81FF',
                    time:2
                });
                $("#USE_PRICE").focus();
                return ;
            }
            if(coupon_type==2) {
                if ($("#CHANGE_CODE").val() == "") {
                    $("#CHANGE_CODE").tips({
                        side: 3,
                        msg: '请输入兑换码',
                        bg: '#AE81FF',
                        time: 2
                    });
                    $("#CHANGE_CODE").focus();
                    return false;
                }
            }
			if($("#COUPON_NUM").val()==""){
				$("#COUPON_NUM").tips({
					side:3,
		            msg:'请输入优惠券数量',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#COUPON_NUM").focus();
			return false;
			}
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