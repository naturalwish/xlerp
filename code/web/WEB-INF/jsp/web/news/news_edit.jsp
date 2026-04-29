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
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<style type="text/css">
	.yulantu{
		 z-index: 9999999999999999;
		 position:absolute;
		 border:2px solid #76ACCD;
		 display: none;
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
					
					<form action="news/${msg }.do" name="Form" id="Form" method="post">
						<input type="hidden" name="NEWS_ID" id="NEWS_ID" value="${pd.NEWS_ID}"/>
						<input type="hidden" name="HIDE" id="HIDE" value="${pd.HIDE }"/>
						<input type="hidden" name="STATE" id="STATE" value="${pd.STATE }"/>
						<input type="hidden" name="TARGET" id="TARGET" value="${pd.TARGET }"/>
						<textarea style="display: none;" name="CONTENT" id="CONTENT">${pd.CONTENT}</textarea>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:95px;text-align: right;padding-top: 13px;">分类:</td>
								<td id="st">
									<select class="chosen-select form-control" name="SORTID" id="SORTID"
											data-placeholder="请选择主题" style="vertical-align:top;" title="主题"
											style="width:50%;">
										<option value=""></option>
										<c:forEach items="${pd.SORTLIST}" var="sort">
											<option value="${sort.NEWSORT_ID }"
													<c:if test="${sort.NEWSORT_ID == pd.SORTID }">selected</c:if>>${sort.SORT_NAME }</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td style="width:95px;text-align: right;padding-top: 13px;">标题:</td>
								<td><input type="text" name="TITLE" id="TITLE" value="${pd.TITLE}" maxlength="50" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">来源:</td>
								<td><input type="text" name="STEMFROM" id="STEMFROM" value="${pd.STEMFROM}" maxlength="100" placeholder="这里输入来源" title="来源" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片:</td>
								<td><input onmouseover="showTU('IMAGE','yulantu1');" onmouseout="hideTU('yulantu1');" type="text" name="IMAGE" id="IMAGE" value="${pd.IMAGE}" maxlength="100" placeholder="这里输入图片" title="图片" style="width:88%;"/>
									<div class="yulantu" id="yulantu1"></div>
									<a class="btn btn-xs btn-info" style="margin-top: -5px;" title="选择" onclick="xuanTp('IMAGE');">选择 </a>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">简述:</td>
								<td>
								<textarea name="SHORTCONTENT" id="SHORTCONTENT" placeholder="这里输入简述" title="简述" style="width:98%;">${pd.SHORTCONTENT}</textarea>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">内容:</td>
								<td>
								<script id="editor" type="text/plain" style="width:93%;height:420px;">${pd.CONTENT}</script>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">权重:</td>
								<td><input type="number" name="WEIGHT" id="WEIGHT" value="${pd.WEIGHT}" maxlength="11" placeholder="这里输入权重" title="权重" style="width:33%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">隐藏状态:</td>
								<td>
									<div class="col-sm-9">
									<label style="float:left;padding-left: 8px;padding-top:7px;">
										<input name="form-field-radio2" type="radio" class="ace" id="form-field-radio3" <c:if test="${pd.HIDE == '0' }">checked="checked"</c:if> onclick="setHide('0');"/>
										<span class="lbl"> 显示</span>
									</label>
									<label style="float:left;padding-left: 5px;padding-top:7px;">
										<input name="form-field-radio2" type="radio" class="ace" id="form-field-radio4" <c:if test="${pd.HIDE == '1' }">checked="checked"</c:if> onclick="setHide('1');"/>
										<span class="lbl"> 隐藏</span>
									</label>
									</div>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">审核状态:</td>
								<td>
									<div class="col-sm-9">
										<label style="float:left;padding-left: 8px;padding-top:7px;">
											<input name="form-field-radio3" type="radio" class="ace" id="form-field-radio5" <c:if test="${pd.STATE == '0' }">checked="checked"</c:if> onclick="setState('0');"/>
											<span class="lbl"> 未审核</span>
										</label>
										<label style="float:left;padding-left: 5px;padding-top:7px;">
											<input name="form-field-radio3" type="radio" class="ace" id="form-field-radio6" <c:if test="${pd.STATE == '1' }">checked="checked"</c:if> onclick="setState('1');"/>
											<span class="lbl"> 已审核</span>
										</label>
									</div>
								</td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">显示目标:</td>
								<td>
									<div class="col-sm-9">
										<label style="float:left;padding-left: 8px;padding-top:7px;">
											<input name="TARGET_TEMP" type="checkbox" class="ace" id="TARGET_APP" value="app" onclick="setTarget();"/>
											<span class="lbl">app</span>
										</label>
										<label style="float:left;padding-left: 5px;padding-top:7px;">
											<input name="TARGET_TEMP" type="checkbox" class="ace" id="TARGET_WEB" value="web" onclick="setTarget();"/>
											<span class="lbl">web</span>
										</label>
									</div>
								</td>
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
	<!-- 百度富文本编辑框-->

	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
	<!-- 百度富文本编辑框-->
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		$(function() {
			var target = $("#TARGET").val();
			if(target){
			$.each(target.split(","), function(i,val){
				if(val=="app"){
					$("#TARGET_APP").attr("checked","true");
				}else if(val=="web"){
					$("#TARGET_WEB").attr("checked","true");
				}
				});
				}
			});

		//保存
		function save(){
            if($("#SORTID").val()==""){
                $("#st").tips({
                    side:3,
                    msg:'请选择主题分类',
                    bg:'#AE81FF',
                    time:2
                });
                $("#SORTID").focus();
                return false;
            }
			if($("#TITLE").val()==""){
				$("#TITLE").tips({
					side:3,
		            msg:'请输入标题',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TITLE").focus();
			return false;
			}
			if($("#STEMFROM").val()==""){
				$("#STEMFROM").tips({
					side:3,
		            msg:'请输入来源',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#STEMFROM").focus();
			return false;
			}
			if($("#IMAGE").val()==""){
				$("#IMAGE").tips({
					side:3,
		            msg:'请输入图片',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#IMAGE").focus();
			return false;
			}
			if($("#SHORTCONTENT").val()==""){
				$("#SHORTCONTENT").tips({
					side:3,
		            msg:'请输入简述',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#SHORTCONTENT").focus();
			return false;
			}
			$("#CONTENT").val(getContent());
			if($("#CONTENT").val()==""){
				$("#CONTENT").tips({
					side:3,
		            msg:'请输入内容',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#CONTENT").focus();
			return false;
			}
			if($("#WEIGHT").val()==""){
				$("#WEIGHT").tips({
					side:3,
					msg:'请输入权重',
					bg:'#AE81FF',
					time:2
				});
				$("#WEIGHT").focus();
				return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		//百度富文本
		setTimeout("ueditor()",500);
		function ueditor(){
			UE.getEditor('editor');
		}
		//ueditor有标签文本
		function getContent() {
		    var arr = [];
		    arr.push(UE.getEditor('editor').getContent());
		    return arr.join("");
		}
		
		//选择图片
		function xuanTp(ID){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="选择图片";
			 diag.URL = '<%=basePath%>pictures/listfortc.do';
			 diag.Width = 860;
			 diag.Height = 680;
			 diag.CancelEvent = function(){ //关闭事件
				 $("#"+ID).val('<%=basePath%>'+diag.innerFrame.contentWindow.document.getElementById('xzvalue').value);
				 diag.close();
			 };
			 diag.show();
		}
		
		//显示图片
		function showTU(ID,TPID){
			 $("#"+TPID).html('<img width="200" src="'+$("#"+ID).val()+'">');
			 $("#"+TPID).show();
		}
		
		//隐藏图片
		function hideTU(TPID){
			 $("#"+TPID).hide();
		}
		
		//设置是否隐藏
		function setHide(value){
			$("#HIDE").val(value);
		}

		//设置是否审核
		function setState(value){
			$("#STATE").val(value);
		}

		//显示目标
		function setTarget(){
			var target = $('input:checkbox[name="TARGET_TEMP"]:checked').map(function(){
				return this.value;
			}).get().join(",");
			$("#TARGET").val(target);
		}
		</script>
</body>
</html>