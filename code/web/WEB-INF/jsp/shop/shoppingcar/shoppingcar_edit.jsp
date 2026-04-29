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
					
					<form action="shoppingcar/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="SHOPPINGCAR_ID" id="SHOPPINGCAR_ID" value="${pd.SHOPPINGCAR_ID}"/>
						<input type="hidden" name="GOODS_ID" id="GOODS_ID" value="${pd.GOODS_ID}"/>
						<input type="hidden" name="USERNAME" id="USERNAME" value="${pd.USERNAME}"/>
						<input name="GOODS_PIC" id="GOODS_PIC" value="${pd.GOODS_PIC}" type="hidden"/>
						<input type="hidden" name="GOODS_ATTRIBUTE_ID" id="GOODS_ATTRIBUTE_ID" value="${pd.GOODS_ATTRIBUTE_ID}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">商品名称:</td>
								<td><input type="text" name="GOODS_NAME" id="GOODS_NAME" value="${pd.GOODS_NAME}" onclick="goodOpen();" maxlength="255" placeholder="这里输入商品名称" title="商品名称" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">规格型号:</td>
								<td><select id="GOODS_ATTRIBUTE" name="GOODS_ATTRIBUTE" onchange="change1()" style="width: 90%">
									<option>${pd.GOODS_ATTRIBUTE}</option>
								    </select>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">数量:</td>
								<td><input type="number" name="GOODS_NUM" id="GOODS_NUM" value="${pd.GOODS_NUM}" onblur="change2()" maxlength="32" placeholder="这里输入数量" title="数量" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">单价:</td>
								<td><input type="number" name="GOODS_PRICE" id="GOODS_PRICE" value="${pd.GOODS_PRICE}" maxlength="32" placeholder="这里输入单价" title="单价" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">小计:</td>
								<td><input type="number" name="GOODS_TOTAL" id="GOODS_TOTAL" value="${pd.GOODS_TOTAL}" maxlength="32" placeholder="这里输入小计" title="小计" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">姓名:</td>
								<td><input type="text" name="REALNAME" id="REALNAME" value="${pd.REALNAME}" onclick="userOpen();" maxlength="255" placeholder="这里输入姓名" title="姓名" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">添加时间:</td>
								<td><input class="span10 date-picker" name="ADDTIME" id="ADDTIME" value="${pd.ADDTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="添加时间" title="添加时间" style="width:98%;"/></td>
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
		//保存
		function save(){
			if($("#GOODS_NAME").val()==""){
				$("#GOODS_NAME").tips({
					side:3,
		            msg:'请输入商品名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_NAME").focus();
			return false;
			}
			if($("#GOODS_ATTRIBUTE").val()==""){
				$("#GOODS_ATTRIBUTE").tips({
					side:3,
		            msg:'请输入规格型号',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_ATTRIBUTE").focus();
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
			if($("#GOODS_TOTAL").val()==""){
				$("#GOODS_TOTAL").tips({
					side:3,
		            msg:'请输入小计',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#GOODS_TOTAL").focus();
			return false;
			}
			if($("#REALNAME").val()==""){
				$("#REALNAME").tips({
					side:3,
		            msg:'请输入姓名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#REALNAME").focus();
			return false;
			}
			if($("#ADDTIME").val()==""){
				$("#ADDTIME").tips({
					side:3,
		            msg:'请输入添加时间',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#ADDTIME").focus();
			return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
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
                        $("#GOODS_NAME").val(tbodyObj.rows[key].cells[4].innerHTML);
                        $("#GOODS_ID").val(tbodyWin.$("#selectStr").val());
                        $("#GOODS_PIC").val(tbodyObj.rows[key].cells[6].innerHTML);
                        //$("#goods_price").val(tbodyObj.rows[key].cells[6].innerHTML);
                        //$("#attribute_detail_name").val(tbodyObj.rows[key].cells[5].innerHTML);
                        return false;
                    }
                });
                diag.close();
                $.ajax({
                    type: "POST",
                    url: '<%=basePath%>attribute/getAttribute.do?',
                    data: {GOODS_ID:$("#GOODS_ID").val()},
                    dataType:'json',
                    cache: false,
                    success: function(data){
                        $("#GOODS_ATTRIBUTE").html('<option>请选择</option>');
                        $.each(data.list, function(i, dvar){
                            $("#GOODS_ATTRIBUTE").append("<option value="+dvar.ATTRIBUTE+ " ATTRIBUTEPRICE="+dvar.ATTRIBUTEPRICE+ " ATTRIBUTEID="+dvar.ATTRIBUTE_ID+
                                ">"+dvar.ATTRIBUTE+"</option>");
                        });
                    }
                });
            };
            diag.show();
        }

        function change1(){
            var options=$("#GOODS_ATTRIBUTE option:selected"); //获取选中的项
            //alert(options.val()); //拿到选中项的值
            //alert(options.text()); //拿到选中项的文本
            //alert(options.attr('url')); //拿到选中项的url值
            $("#GOODS_NUM").val(1);
            $("#GOODS_PRICE").val(options.attr('ATTRIBUTEPRICE'));
            $("#GOODS_TOTAL").val(options.attr('ATTRIBUTEPRICE'));
            $("#GOODS_ATTRIBUTE_ID").val(options.attr('ATTRIBUTEID'));
        }
        function change2(){
            if($("#GOODS_NAME").val()==""){
                $("#GOODS_NAME").tips({
                    side:3,
                    msg:'请输入商品名称',
                    bg:'#AE81FF',
                    time:2
                });
                $("#GOODS_NAME").focus();
                return false;
            }
            if($("#GOODS_ATTRIBUTE_ID").val()==""){
                $("#GOODS_ATTRIBUTE").tips({
                    side:3,
                    msg:'请输入规格型号',
                    bg:'#AE81FF',
                    time:2
                });
                $("#GOODS_ATTRIBUTE").focus();
                return false;
            }

            $("#GOODS_TOTAL").val($("#GOODS_PRICE").val()*$("#GOODS_NUM").val());
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
                        $("#REALNAME").val(tbodyObj.rows[key].cells[3].innerHTML+"("+tbodyObj.rows[key].cells[2].innerHTML+")");
                        $("#USERNAME").val(tbodyWin.$("#selectStr").val());
                        return false;
                    }
                });
                diag.close();
            };
            diag.show();
        }

		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>