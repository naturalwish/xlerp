<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String mallUploadUrl = (String) request.getSession().getAttribute("mall_upload_url");
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
							
						<!-- 检索  -->
						<form action="goods/list.do" method="post" name="Form" id="Form">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
								<c:if test="${QX.toExcel == 1 }"><td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a></td></c:if>
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
									<th class="center">编号</th>
									<th class="center" style="width: 500px">名称</th>
									<th class="center">货号</th>
									<th class="center">上架</th>
									<th class="center">精品</th>
									<th class="center">新品</th>
									<th class="center">热销</th>
									<th class="center">数量</th>
									<th class="center">图片</th>
									<th class="center">排序</th>
									<th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty varList}">
									<c:if test="${QX.cha == 1 }">
									<c:forEach items="${varList}" var="var" varStatus="vs">
										<tr>
											<td class='center'>
												<label class="pos-rel"><input type='checkbox' name='ids' value="${var.GOODS_ID}" class="ace" /><span class="lbl"></span></label>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center'>${var.GOODS_CODE}</td>
											<td class='center'>${var.GOODS_NAME}</td>
											<td class='center'>${var.GOODS_NUMBER}</td>
											<td class='center' style="height: 20px;">
												<label>
													<input name="switch-field-1" onclick="updateForsale('${var.GOODS_ID}',this.checked)" class="ace ace-switch ace-switch-3" type="checkbox" <c:if test="${var.GOODS_FORSALE == 1 }">checked="checked"</c:if> >
													<span class="lbl"></span>
												</label>
											</td>
											<td style="width: 100px;" class="center">
												<c:if test="${var.GOODS_BEST == '0' }"><span class="label label-important arrowed-in">普通</span></c:if>
												<c:if test="${var.GOODS_BEST == '1' }"><span class="label label-success arrowed">精品</span></c:if>
											</td>
											<td style="width: 100px;" class="center">
												<c:if test="${var.GOODS_NEW == '0' }"><span class="label label-important arrowed-in">非新品</span></c:if>
												<c:if test="${var.GOODS_NEW == '1' }"><span class="label label-success arrowed">新品</span></c:if>
											</td>
											<td style="width: 100px;" class="center">
												<c:if test="${var.GOODS_HOT == '0' }"><span class="label label-important arrowed-in">热销品</span></c:if>
												<c:if test="${var.GOODS_HOT == '1' }"><span class="label label-success arrowed">非热销品</span></c:if>
											</td>
											<td class='center'>${var.GOODS_NUM}</td>
											<td class='center'><img src="<%=mallUploadUrl%>${var.GOODS_PIC}" alt="${var.GOODS_NAME}" height="60px" width="60px"></td>
											<td class='center'>${var.GOODS_ORDER}</td>
											<td class="center">
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.edit == 1 }">
														<a class="btn btn-xs btn-success" title="编辑" onclick="edit('${var.GOODS_ID}');">
															<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
														</a>

														<a class="btn btn-xs btn-success" title="编辑详情" onclick="editDetails('${var.GOODS_ID}','${var.GOODS_CODE}');">
															<i class="ace-icon fa fa-film bigger-120" title="编辑详情"></i>
														</a>
														<a class="btn btn-xs btn-success" title="添加分类" onclick="addCategory('${var.GOODS_ID}');">
															<i class="ace-icon fa fa-check-square-o bigger-120" title="添加分类"></i>
														</a>
														<a class="btn btn-xs btn-success" title="添加型号" onclick="addAttribute('${var.GOODS_ID}','${var.GOODS_CODE}');">
															<i class="ace-icon fa fa-plus-circle bigger-120" title="添加型号"></i>
														</a>
													</c:if>
													<a class="btn btn-xs btn-success" title="查看评论" onclick="listRate('${var.GOODS_ID}');">
														<i class="ace-icon fa fa-file-o bigger-120" title="查看评论"></i>
													</a>
													<c:if test="${QX.del == 1 }">
														<a class="btn btn-xs btn-danger" onclick="del('${var.GOODS_ID}');">
															<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
														</a>
													</c:if>
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
			
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															<c:if test="${QX.edit == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="edit('${var.GOODS_ID}');" class="tooltip-success" data-rel="tooltip" title="修改">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
															<c:if test="${QX.del == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="del('${var.GOODS_ID}');" class="tooltip-error" data-rel="tooltip" title="删除">
																	<span class="red">
																		<i class="ace-icon fa fa-trash-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
														</ul>
													</div>
												</div>
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
									<c:if test="${QX.add == 1 }">
									<a class="btn btn-mini btn-success" onclick="add();">新增</a>
									</c:if>
									<c:if test="${QX.del == 1 }">
									<a class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
									</c:if>
								</td>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
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

        //新增商品规格属性
        function addAttribute(value,title){
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag=true;
            diag.Title ="商品编号："+title+" 规格属性";
            diag.URL = '<%=basePath%>attribute/list.do?GOODS_ID='+value;
            diag.Width = 1000;
            diag.Height = 650;
            diag.Modal = true;				//有无遮罩窗口
            diag. ShowMaxButton = true;	//最大化按钮
            diag.ShowMinButton = true;		//最小化按钮
            diag.CancelEvent = function(){ //关闭事件

                diag.close();
            };
            diag.show();
        }
		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>goods/goAdd.do';
			 diag.Width = 950;
			 diag.Height = 750;
			 diag.Modal = true;				//有无遮罩窗口
			 diag.ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 nextPage(${page.currentPage});
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>goods/delete.do?GOODS_ID="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						nextPage(${page.currentPage});
					});
				}
			});
		}
		
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>goods/goEdit.do?GOODS_ID='+Id;
			 diag.Width = 1000;
			 diag.Height = 650;
			 diag.Modal = true;				//有无遮罩窗口
			 diag.ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮 
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}

        //新增商品详情
        function editDetails(id,title){
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag=true;
            diag.Title ="商品编号："+title+" 详情图片";
            diag.URL = '<%=basePath%>goods/goEditDetails.do?GOODS_ID='+id;
            diag.Width = 1000;
            diag.Height = 650;
            diag.Modal = true;				//有无遮罩窗口
            diag.ShowMaxButton = true;	//最大化按钮
            diag.ShowMinButton = true;		//最小化按钮
            diag.CancelEvent = function(){ //关闭事件
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
								url: '<%=basePath%>goods/deleteAll.do?tm='+new Date().getTime(),
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

        //更新商品是否上架
        function updateForsale(goodsID,c){
            var val = c? 1:0;
            $.ajax({
                type: "POST",
                url: "<%=basePath%>goods/listUpdate.do?tm="+new Date().getTime(),
                data: {GOODS_ID:goodsID,GOODS_FORSALE:val},
                dataType:'json',
                cache: false,
                success: function(data){
                }
            });
        }

        //查看评论
        function listRate(Id){
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag=true;
            diag.Title ="查看评论";
            diag.URL = '<%=basePath%>goodsrate/list.do?GOODS_ID='+Id;
            diag.Width = 1000;
            diag.Height = 650;
            diag.Modal = true;				//有无遮罩窗口
            diag. ShowMaxButton = true;	//最大化按钮
            diag.ShowMinButton = true;		//最小化按钮
            diag.show();
        }

        function addCategory(GOODS_ID){
            top.jzts();
            var diag = new top.Dialog();
            diag.Drag = true;
            diag.Title = "设置商品扩展分类";
            diag.URL = '<%=basePath%>goods/chooseCategory.do?GOODS_ID='+GOODS_ID;
            diag.Width = 330;
            diag.Height = 450;
            diag.CancelEvent = function(){ //关闭事件
                diag.close();
            };
            diag.show();
        }
		
		//导出excel
		function toExcel(){
			window.location.href='<%=basePath%>goods/excel.do';
		}
	</script>


</body>
</html>