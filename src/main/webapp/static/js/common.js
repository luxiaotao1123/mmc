// 详情窗口-高度
var detailHeight = '90%';
// 详情窗口-宽度
var detailWidth = '90%';

// 非空判断
function isEmpty(obj){
    return typeof obj == "undefined" || obj == null || obj === "";
}

// 时间 ==>> 字符串
function dateToStr(date) {
    var time = new Date(date);
    var y = time.getFullYear();
    var M = time.getMonth() + 1;
    M = M < 10 ? ("0" + M) : M;
    var d = time.getDate();
    d = d < 10 ? ("0" + d) : d;
    var h = time.getHours();
    h = h < 10 ? ("0" + h) : h;
    var m = time.getMinutes();
    m = m < 10 ? ("0" + m) : m;
    var s = time.getSeconds();
    s = s < 10 ? ("0" + s) : s;
    return y + "-" + M + "-" + d + " " + h + ":" + m + ":" + s;
}

// 字符串 ===>> 时间
function strToDate(str) {
    var t = Date.parse(str);
    if (!isNaN(t)) {
        return new Date(Date.parse(str.replace(/-/g, "/")));
    } else {
        return null;
    }
}

// 清理对象null值
function reObject(data) {
    for (var obj in data) {
        if (data[obj]===null){
            delete data[obj];
        }
    }
    return data;
}

/**
 * disabled 属性转换
 */
function convertDisabled(el, param) {
    el.each(function () {
        $(this).attr("disabled", param);
    });
}
