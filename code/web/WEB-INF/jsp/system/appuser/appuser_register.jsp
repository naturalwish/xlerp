<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>${pd.SYSNAME}</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link rel="stylesheet" href="static/login/bootstrap.min.css" />
<link rel="stylesheet" href="static/login/css/camera.css" />
<link rel="stylesheet" href="static/login/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="static/login/matrix-login.css" />
<link href="static/login/font-awesome.css" rel="stylesheet" />
<script type="text/javascript" src="static/login/js/jquery-1.5.1.min.js"></script>
 <style type="text/css">
      .cavs{
    	z-index:1;
    	position: fixed;
    	width:95%;
    	margin-left: 20px;
    	margin-right: 20px;
    }
  </style>
</head>
<body>
	<canvas class="cavs"></canvas>
	<div style="width:100%;text-align: center;margin: 0 auto;position: absolute;">
		<!-- 注册 -->
		<div id="windows2" style="display: block;">
		<div id="loginbox">
			<form action="" method="post" name="loginForm" id="loginForm">
				<input type="hidden" name="YZM_SERVER" id="YZM_SERVER" value=""/>
				<input type="hidden" name="PHONE_OLD" id="PHONE_OLD" value=""/>
				<div class="control-group normal_text">
					<h3>
						<%--<img src="static/login/logo.png" alt="Logo" />--%>
					</h3>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_lg">
							<i>手机号</i>
							</span><input type="number" name="PHONE" id="PHONE" value="" placeholder="请输入手机号" />
						</div>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_ly">
							<i>密码</i>
							</span><input type="password" name="PASSWORD" id="PASSWORD" placeholder="请输入密码"  value=""/>
						</div>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_ly">
							<i>重输</i>
							</span><input type="password" name="PASSWORD1" id="PASSWORD1" placeholder="请重新输入密码" value=""/>
						</div>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_lg">
							<i>验证码</i>
							</span><input type="text" name="YZM" id="YZM" value="" placeholder="请输入验证码" style="width: 40%" />
								<input type="button"  name="BTYZM" id="BTYZM" value="发送验证码" style="float: right;width:35%" onclick="sendYZM(this);" />
						</div>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_lg">
							<i>邀请码</i>
							</span><input type="text" name="PARENT_ID" id="PARENT_ID" value="${PARENT_ID}" placeholder="邀请码" />
						</div>
					</div>
				</div>
				<div class="form-actions">
					<div style="width:86%;padding-left:8%;">
						<span class="pull-center"><a onclick="register();" class="flip-link btn btn-info" id="to-recover">&nbsp;&nbsp;注册&nbsp;&nbsp;</a></span>
					</div>
				</div>
			</form>
		</div>
		</div>

	</div>
	<div id="templatemo_banner_slide" class="container_wapper">
		<div class="camera_wrap camera_emboss" id="camera_slide">
			<!-- 背景图片 -->
			<c:choose>
				<c:when test="${not empty pd.listImg}">
					<c:forEach items="${pd.listImg}" var="var" varStatus="vs">
						<div data-src="static/login/images/${var.FILEPATH }"></div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div data-src="static/login/images/banner_slide_01.jpg"></div>
					<div data-src="static/login/images/banner_slide_02.jpg"></div>
					<div data-src="static/login/images/banner_slide_03.jpg"></div>
					<div data-src="static/login/images/banner_slide_04.jpg"></div>
					<div data-src="static/login/images/banner_slide_05.jpg"></div>
				</c:otherwise>
			</c:choose>
		</div>
		<!-- #camera_wrap_3 -->
	</div>

	<script type="text/javascript">

        //倒计时
        var countdown=60;
        function sendYZM(val) {
            if (countdown == 0) {
                val.removeAttribute("disabled");
                val.value="获取验证码";
                countdown = 60;
                return false;
            } else if (countdown == 60){
                //发送验证码
                if ($("#PHONE").val() == "") {
                    $("#PHONE").tips({
                        side: 3,
                        msg: '输入手机号',
                        bg: '#AE81FF',
                        time: 2
                    });
                    $("#PHONE").focus();
                    $("#PHONE").val('');
                    return false;
                } else {
                    $("#PHONE").val(jQuery.trim($('#PHONE').val()));
                    var re = /^1\d{10}$/                                //以1开始后面加10位数字
                    if (!re.test($("#PHONE").val())) {
                        $("#PHONE").tips({
                            side: 3,
                            msg: '手机号格式不正确',
                            bg: '#AE81FF',
                            time: 2
                        });
                        $("#PHONE").focus();
                        $("#PHONE").val('');
                        return false;
                    }
                }
                send();
                val.setAttribute("disabled", true);
                val.value="重新发送(" + countdown + ")";
                countdown--;
			}else{
                val.setAttribute("disabled", true);
                val.value="重新发送(" + countdown + ")";
                countdown--;
            }
            setTimeout(function() {
                sendYZM(val);
            },1000);
        }

        function send() {
            $("#PHONE_OLD").val($("#PHONE").val());
            $.ajax({
                type: "POST",
                url: 'sms/app/sendsms.do',
                data: {PHONE:$("#PHONE").val(),tm:new Date().getTime()},
                dataType:'json',
                cache: false,
                success: function(data){
                    $("#YZM_SERVER").val(data.STATUSCODE);
                }
            });

        }

	//注册
	function rcheck() {
        if ($("#PHONE").val() == "") {
            $("#PHONE").tips({
                side: 3,
                msg: '输入手机号',
                bg: '#AE81FF',
                time: 2
            });
            $("#PHONE").focus();
            $("#PHONE").val('');
            return false;
        } else {
            $("#PHONE").val(jQuery.trim($('#PHONE').val()));
            var re = /^1\d{10}$/                                //以1开始后面加10位数字
            if (!re.test($("#PHONE").val())) {
                $("#PHONE").tips({
                    side: 3,
                    msg: '手机号格式不正确',
                    bg: '#AE81FF',
                    time: 2
                });
                $("#PHONE").focus();
                $("#PHONE").val('');
                return false;
            }
        }

        if ($("#PASSWORD").val() == "") {
            $("#PASSWORD").tips({
                side: 3,
                msg: '输入密码',
                bg: '#AE81FF',
                time: 2
            });
            $("#PASSWORD").focus();
            return false;
        }
        if ($("#PASSWORD").val() != $("#PASSWORD1").val()) {
            $("#PASSWORD1").tips({
                side: 3,
                msg: '两次密码不相同',
                bg: '#AE81FF',
                time: 3
            });
            $("#PASSWORD1").focus();
            return false;
        }
        if($("#YZM_SERVER").val()==""){
            $("#BTYZM").tips({
                side:3,
                msg:'请先发送验证码',
                bg:'#AE81FF',
                time:2
            });
            $("#BTYZM").focus();
            return false;
        }
        if($("#YZM").val()==""){
            $("#YZM").tips({
                side:3,
                msg:'输入验证码',
                bg:'#AE81FF',
                time:2
            });
            $("#YZM").focus();
            return false;
        }
        if ($("#YZM").val() != $("#YZM_SERVER").val()) {
            $("#YZM").tips({
                side: 3,
                msg: '验证码不正确',
                bg: '#AE81FF',
                time: 3
            });
            $("#YZM").focus();
            return false;
        }
        if ($("#PHONE_OLD").val() == "") {
            $("#BTYZM").tips({
                side:3,
                msg:'请先发送验证码',
                bg:'#AE81FF',
                time:2
            });
            $("#BTYZM").focus();
            return false;
        }

        if ($("#PHONE_OLD").val() != $("#PHONE").val()) {
            $("#YZM_SERVER").val("");//如果手机号前后不一致 则 清空验证码 并提示
            $("#PHONE_OLD").val("");
            $("#PHONE").tips({
                side: 3,
                msg: '手机号改变,需要重新发送验证码',
                bg: '#AE81FF',
                time: 2
            });
            $("#PHONE").focus();
            return false;
        }
        return true;
	}

	//提交注册
	function register(){
		if(rcheck()){
			var nowtime = date2str(new Date(),"yyyyMMdd");
			$.ajax({
				type: "POST",
				url: 'happuser/app/regist.do',
		    	data: {PHONE:$("#PHONE").val(),PASSWORD:$("#PASSWORD").val(),PARENT_ID:$("#PARENT_ID").val(),FKEY:$.md5('PHONE'+nowtime+',da,'),tm:new Date().getTime()},
				dataType:'json',
				cache: false,
				success: function(data){
					if("1" == data.RESULT){
                        window.location.reload();
						$("#loginbox").tips({
							side : 1,
							msg : '注册成功,请登录',
							bg : '#68B500',
							time : 10
						});
                        alert(data.MESSAGE);
					}else if("0" == data.RESULT){
						$("#PHONE").tips({
							side : 1,
							msg : "用户名已存在",
							bg : '#FF5080',
							time : 5
						});
						$("#PHONE").focus();
					}
				}
			});
		}
	}

        //邮箱格式校验
	function ismail(mail){
		return(new RegExp(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/).test(mail));
	}
	//js  日期格式
	function date2str(x,y) {
	     var z ={y:x.getFullYear(),M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
	     return y.replace(/(y+|M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-(v.length>2?v.length:2))});
	 	};
	</script>

	<script src="static/login/js/bootstrap.min.js"></script>
	<script src="static/js/jquery-1.7.2.js"></script>
	<script src="static/login/js/jquery.easing.1.3.js"></script>
	<script src="static/login/js/jquery.mobile.customized.min.js"></script>
	<script src="static/login/js/camera.min.js"></script>
	<script src="static/login/js/templatemo_script.js"></script>
	<script src="static/login/js/ban.js"></script>
	<script type="text/javascript" src="static/js/jQuery.md5.js"></script>
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript" src="static/js/jquery.cookie.js"></script>
</body>

</html>