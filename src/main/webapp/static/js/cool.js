/**
 * 搜索自动补全 -- 局外点击隐藏
 */
$(function(){
    $(document).on("click",function(e){
        if(!$(e.target).parents(".cool-auto-complete").length){
            $(".cool-auto-complete-window").hide();
        }
    });
});


/**
 * 搜索自动补全 -- div
 */
function autoShow(id) {
    var cac = document.getElementById(id).parentNode;
    var cacw = cac.getElementsByClassName("cool-auto-complete-window")[0];
    if (cacw.style.display === "none" || cacw.style.display === ""){
        cacw.style.display = "block";
        var cacwi = cacw.getElementsByClassName("cool-auto-complete-window-input")[0];
        cacwi.focus();
        autoLoad(cacwi.getAttribute('data-key'));
    }else {
        cacw.style.display = "none";
    }
}

/**
 * 搜索自动补全 -- input
 */
function autoLoad(val) {
    var inputDomVal = document.querySelector("input[data-key="+val+"]").value;
    var selectDom = document.querySelector("select[data-key="+val+"Select]");
    selectDom.length = 0;
    var defaultOption = new Option("取消选择", "");
    defaultOption.title = "";
    selectDom.appendChild(defaultOption);
    selectDom.style.display='none';
    $.ajax({
        url: "/"+getForeignKeyQuery(val)+"/auth",
        headers: {'token': localStorage.getItem('token')},
        data: {condition: inputDomVal},
        method: 'POST',
        traditional:true,
        success: function (res) {
            if (res.code === 200){
                var list = res.data;
                for (var i=0;i<list.length;i++){
                    var option = new Option(list[i].value, i);
                    option.title = list[i].id;
                    selectDom.options[i+1] = option;
                }
                selectDom.style.display='block';
            } else if (res.code === 403){
                top.location.href = "/";
            } else {
                layer.msg(res.msg)
            }
        }
    });
}

/**
 * 搜索自动补全 -- select
 */
function confirmed(val){
    var inputDom = $("input[data-key="+val.substring(0,val.length - 6)+"]");
    var selectDom = $("select[data-key="+val+"]");

    selectDom.dblclick(function(){
        inputDom.focus();
        selectDom.css("display","none");
    });

    // 修饰字段
    var cacw = inputDom.parent();
    cacw.css("display", "none");
    var cacd = cacw.parent().find(".cool-auto-complete-div");
    var realDom = cacd.prev();
    // id字段
    var selectOptionDom = selectDom.find("option:selected");
    var html = selectOptionDom.html();
    if (html === "取消选择"){
        cacd.val("");
        realDom.val("");
    } else {
        cacd.val(selectOptionDom.html());
        realDom.val(selectOptionDom.attr("title"));
    }
    inputDom.val("");
}

function reviewImg(src) {
    window.open().document.write("<img src="+src+" />");
}

/**
 * 截取By之前的字符串
 */
function getForeignKeyQuery(str) {
    var index = str.indexOf('By');
    if (index !== -1){
        return str.substring(0, index);
    }
    return str;
}
