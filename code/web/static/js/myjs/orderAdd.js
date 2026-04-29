/**
 * @DA QQ:313596790
 */
var locat = (window.location+'').split('/');
$(function(){if('createCode'== locat[3]){locat =  locat[0]+'//'+locat[2];}else{locat =  locat[0]+'//'+locat[2]+'/'+locat[3];};});

//生成
function saveOrder(){

    /*if($("#TITLE").val()==""){
        $("#TITLE").tips({
            side:3,
            msg:'输入说明',
            bg:'#AE81FF',
            time:2
        });
        $("#TITLE").focus();
        return false;
    }

    if($("#fields").html() == ''){
        $("#table_report").tips({
            side:3,
            msg:'请添加属性',
            bg:'#AE81FF',
            time:2
        });
        return false;
    }*/
    if(arField.length==0){
        alert("请添加商品信息！！！");
        return false;
    }
    if(!confirm("确定要保存订单吗?")){
        return false;
    }
    var strArField = '';
    for(var i=0;i<arField.length;i++){
        strArField = strArField + arField[i] + "Q2012";
    }
    $("#FIELDLIST").val(strArField); 	//属性集合
    $("#Form").submit();//提交
    window.location.reload();

}

//保存编辑属性
function saveDD(){

    var goods_name = $("#goods_name").val(); 	 		 //商品名
    var attribute_detail_name = $("#attribute_detail_name").val(); 	 		 //型号
    var goods_price= $("#goods_price").val();   	 		 //单价
    var goods_count = $("#goods_count").val(); 		 //数量
    var goods_id = $("#goods_id").val(); 		 //商品ID
    var goods_pic = $("#goods_pic").val(); 		 //图片
    var msgIndex = $("#msgIndex").val(); 	 //msgIndex不为空时是修改
    if(goods_name==""){
        $("#goods_name").tips({
            side:3,
            msg:'选择商品',
            bg:'#AE81FF',
            time:2
        });
        $("#goods_name").focus();
        return false;
    }else{
        if(msgIndex != ''){
            var hcdname = $("#hcdname").val();
            if(hcdname != goods_name){
                if(!isSame(goods_name,attribute_detail_name)){
                    $("#goods_name").tips({
                        side:3,
                        msg:'商品已存在，请重新选择！',
                        bg:'#AE81FF',
                        time:2
                    });
                    $("#goods_name").focus();
                    return false;
                };
            };
        }else{
            if(!isSame(goods_name,attribute_detail_name)){
                $("#goods_name").tips({
                    side:3,
                    msg:'商品已存在，请重新选择！',
                    bg:'#AE81FF',
                    time:2
                });
                $("#goods_name").focus();
                return false;
            };
        }
    }

    if(goods_price==""){
        $("#goods_price").tips({
            side:3,
            msg:'输入单价',
            bg:'#AE81FF',
            time:2
        });
        $("#goods_price").focus();
        return false;
    }

    if(goods_count==""){
        $("#goods_count").tips({
            side:3,
            msg:'输入数量',
            bg:'#AE81FF',
            time:2
        });
        $("#goods_count").focus();
        return false;
    }
    var fields = goods_name + ',da,' + attribute_detail_name + ',da,' + goods_price + ',da,' + goods_count+ ',da,' + goods_id+ ',da,' + goods_pic;

    if(msgIndex == ''){
        arrayField(fields);
    }else{
        editArrayField(fields,msgIndex);
    }
    $("#dialog-add").css("display","none");
}

//打开编辑属性(新增)
function dialog_open(){
    $("#dialog-add").css("display","block");
    $("#goods_name").val('');
    $("#attribute_detail_name").val('');
    $("#goods_price").val('');
    $("#goods_pic").val('');
    $("#goods_id").val('');
    $("#msgIndex").val('');
    $("#goods_count").val('1');
}

//打开编辑属性(修改)
function editField(value,msgIndex){
    $("#dialog-add").css("display","block");
    var efieldarray = value.split(',da,');
    $("#goods_name").val(efieldarray[0]);
    $("#hcdname").val(efieldarray[0]);
    $("#attribute_detail_name").val(efieldarray[1]);
    $("#goods_price").val(efieldarray[2]);
    $("#goods_count").val(efieldarray[3]);
    $("#msgIndex").val(msgIndex);
}

//关闭编辑属性
function cancel_pl(){
    $("#dialog-add").css("display","none");
}

//赋值类型
function setType(value){
    $("#dtype").val(value);
    $("#decimal").val('');
    $("#decimal").attr("disabled",true);
    if(value == 'Integer'){
        if(Number($("#flength").val())-0>11){
            $("#flength").val(11);
        }
    }else if(value == 'Date'){
        $("#flength").val(32);
    }else if(value == 'Double'){
        if(Number($("#flength").val())-0>11){
            $("#flength").val(11);
        }
        $("#decimal").val(2);
        $("#decimal").attr("disabled",false);
    }else{
        $("#flength").val(255);
    }
}

//赋值是否前台录入
function isQian(value){
    if(value == '是'){
        $("#isQian").val('是');
        $("#ddefault").val("无");
        $("#ddefault").attr("disabled",true);
    }else{
        $("#isQian").val('否');
        $("#ddefault").val('');
        $("#ddefault").attr("disabled",false);
    }
}

var arField = new Array();
var index = 0;
//追加属性列表
function appendC(value){
    var fieldarray = value.split(',da,');
    $("#fields").append(
        '<tr>'+
        '<td class="center">'+Number(index+1)+'</td>'+
        '<td class="center">'+fieldarray[0]+'<input type="hidden" name="field0'+index+'" value="'+fieldarray[0]+'"></td>'+
        '<td class="center">'+fieldarray[1]+'<input type="hidden" name="field1'+index+'" value="'+fieldarray[1]+'"></td>'+
        '<td class="center">'+fieldarray[2]+'<input type="hidden" name="field2'+index+'" value="'+fieldarray[2]+'"></td>'+
        '<td class="center">'+fieldarray[3]+'<input type="hidden" name="field3'+index+'" value="'+fieldarray[3]+'"></td>'+
        '<td class="center">'+fieldarray[2]*fieldarray[3]+'<input type="hidden" name="field4'+index+'" value="'+fieldarray[2]*fieldarray[3]+'"></td>'+
        '<td class="center" style="width:100px;">'+
        '<input type="hidden" name="field'+index+'" value="'+value+'">'+
        '<a class="btn btn-mini btn-info" title="编辑" onclick="editField(\''+value+'\',\''+index+'\')"><i class="ace-icon fa fa-pencil-square-o bigger-120"></i></a>&nbsp;'+
        '<a class="btn btn-mini btn-danger" title="删除" onclick="removeField(\''+index+'\')"><i class="ace-icon fa fa-trash-o bigger-120"></i></a>'+
        '</td>'+
        '<td class="center" style="display:none"><input type="hidden" name="field5'+index+'" value="'+fieldarray[4]+'"></td>'+
        '<td class="center" style="display:none"><input type="hidden" name="field5'+index+'" value="'+fieldarray[5]+'"></td>'+
        '</tr>'
    );
    index++;
    $("#zindex").val(index);
}

//保存属性后往数组添加元素
function arrayField(value){
    arField[index] = value;
    appendC(value);
}

//修改属性
function editArrayField(value,msgIndex){
    arField[msgIndex] = value;
    index = 0;
    $("#fields").html('');
    for(var i=0;i<arField.length;i++){
        appendC(arField[i]);
    }
}

//删除数组添加元素并重组列表
function removeField(value){
    index = 0;
    $("#fields").html('');
    arField.splice(value,1);
    for(var i=0;i<arField.length;i++){
        appendC(arField[i]);
    }
}

//判断属性名是否重复
function isSame(value,value1){
    for(var i=0;i<arField.length;i++){
        var array0 = arField[i].split(',da,')[0];
        var array1 = arField[i].split(',da,')[1];
        if(array0 == value&&array1 == value1){
            return false;
        }
    }
    return true;
}


/**
 * @DA QQ: 3 1 3 596790
 */