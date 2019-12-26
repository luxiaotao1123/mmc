var pageCurr;
layui.use(['table','laydate', 'form'], function(){
    var table = layui.table;
    var $ = layui.jquery;
    var layer = layui.layer;
    var layDate = layui.laydate;
    var form = layui.form;

    // 数据渲染
    tableIns = table.render({
        elem: '#merchant',
        headers: {token: localStorage.getItem('token')},
        url: '/merchant/list/auth',
        page: true,
        limit: 16,
        toolbar: '#toolbar',
        cellMinWidth: 50,
        cols: [[
            {type: 'checkbox', fixed: 'left'}
            ,{field: 'id', title: 'ID', sort: true,align: 'center', fixed: 'left', width: 80}
            ,{field: 'appId', align: 'center',title: '应用ID'}
            ,{field: 'name', align: 'center',title: '商户名称'}
            ,{field: 'partner', align: 'center',title: '商户号'}
            ,{field: 'subject', align: 'center',title: '标题'}
            ,{field: 'charset$', align: 'center',title: '编码方式'}
            ,{field: 'signType$', align: 'center',title: '加密方式'}
            ,{field: 'privateKey', align: 'center',title: '私钥'}
            ,{field: 'publicKey', align: 'center',title: '公钥'}
            ,{field: 'weight', align: 'center',title: '权重值'}
            ,{field: 'sort', align: 'center',title: '排序'}
            ,{field: 'memo', align: 'center',title: '备注'}
            ,{field: 'createTime$', align: 'center',title: '添加时间'}
            ,{field: 'updateTime$', align: 'center',title: '修改时间'}
            ,{field: 'editor$', align: 'center',title: '操作人',event: 'editor', style: 'text-decoration: underline;cursor:pointer'}
            ,{field: 'status$', align: 'center',title: '状态'}

            ,{fixed: 'right', title:'操作', align: 'center', toolbar: '#operate', width:150}
        ]],
        request: {
            pageName: 'curr',
            pageSize: 'limit'
        },
        parseData: function (res) {
            return {
                'code': res.code,
                'msg': res.msg,
                'count': res.data.total,
                'data': res.data.records
            }
        },
        response: {
            statusCode: 200
        },
        done: function(res, curr, count) {
            if (res.code === 403) {
                top.location.href = "/";
            }
            pageCurr=curr;
        }
    });

    // 监听头工具栏事件
    table.on('toolbar(merchant)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch(obj.event) {
            case 'addData':
                layer.open({
                    type: 2,
                    title: '新增',
                    maxmin: true,
                    area: [top.detailWidth, top.detailHeight],
                    shadeClose: false,
                    content: '/merchant_detail',
                    success: function(layero, index){
                    	clearFormVal(layer.getChildFrame('#detail', index));
                        detailScreen(index);
                    }
                });
                break;
            case 'refreshData':
                tableIns.reload({
                    page: {
                        curr: pageCurr
                    }
                });
                break;
            case 'deleteData':
                var data = checkStatus.data;
                var ids=[];
                data.map(function (track) {
                    ids.push(track.id);
                });
                if (ids.length === 0){
                    layer.msg('请选择数据');
                } else {
                    layer.confirm('确定删除'+(ids.length===1?'此':ids.length)+'条数据吗', function(){
                        $.ajax({
                            url: "/merchant/delete/auth",
                            headers: {'token': localStorage.getItem('token')},
                            data: {ids: ids},
                            method: 'POST',
                            traditional:true,
                            success: function (res) {
                                if (res.code === 200){
                                    layer.closeAll();
                                    tableReload(false);
                                } else if (res.code === 403){
                                    top.location.href = "/";
                                } else {
                                    layer.msg(res.msg)
                                }
                            }
                        })
                    });
                }
                break;
            case 'exportData':
                layer.confirm('确定导出Excel吗', function(){
                    var titles=[];
                    var fields=[];
                    obj.config.cols[0].map(function (col) {
                        if (col.type === 'normal' && col.hide === false && col.toolbar == null) {
                            titles.push(col.title);
                            fields.push(col.field);
                        }
                    });
                    var exportData = {};
                    $.each($('#search-box [name]').serializeArray(), function() {
                        exportData[this.name] = this.value;
                    });
                    var param = {
                        'merchant': exportData,
                        'fields': fields
                    };
                    $.ajax({
                        url: "/merchant/export/auth",
                        headers: {'token': localStorage.getItem('token')},
                        data: JSON.stringify(param),
                        dataType:'json',
                        contentType:'application/json;charset=UTF-8',
                        method: 'POST',
                        success: function (res) {
                            layer.closeAll();
                            if (res.code === 200) {
                                table.exportFile(titles,res.data,'xls');
                            } else if (res.code === 403) {
                                top.location.href = "/";
                            } else {
                                layer.msg(res.msg)
                            }
                        }
                    });
                });
                break;
        }
    });

    // 监听行工具事件
    table.on('tool(merchant)', function(obj){
        var data = obj.data;
        switch (obj.event) {
            // 查看
            case 'detail':
                layer.open({
                    type: 2,
                    title: '查看',
                    maxmin: true,
                    area: [top.detailWidth, top.detailHeight],
                    shadeClose: false,
                    content: '/merchant_detail',
                    success: function(layero, index){
                        setFormVal(layer.getChildFrame('#detail', index), data, true);
                        top.convertDisabled(layer.getChildFrame('#data-detail :input', index), true);
                        layer.getChildFrame('#data-detail-submit', index).hide();
                        detailScreen(index);
                        layero.find('iframe')[0].contentWindow.layui.form.render('select');
                    }
                });
                break;
            // 编辑
            case 'edit':
                layer.open({
                    type: 2,
                    title: '修改',
                    maxmin: true,
                    area: [top.detailWidth, top.detailHeight],
                    shadeClose: false,
                    content: '/merchant_detail',
                    success: function(layero, index){
                        setFormVal(layer.getChildFrame('#detail', index), data, false);
                        top.convertDisabled(layer.getChildFrame('#data-detail :input', index), false);
                        detailScreen(index);
                        layero.find('iframe')[0].contentWindow.layui.form.render('select');
                    }
                });
                break;
            case 'editor':
                var param = top.reObject(data).editor;
                if (param === undefined) {
                    layer.msg("无数据");
                } else {
                   layer.open({
                       type: 2,
                       title: '操详情',
                       maxmin: true,
                       area: [top.detailHeight, top.detailWidth],
                       shadeClose: false,
                       content: 'user_detail',
                       success: function(layero, index){
                           $.ajax({
                               url: "/user/"+ param +"/auth",
                               headers: {'token': localStorage.getItem('token')},
                               method: 'GET',
                               success: function (res) {
                                   if (res.code === 200){
                                       setFormVal(layer.getChildFrame('#detail', index), res.data, true);
                                       top.convertDisabled(layer.getChildFrame('#data-detail :input', index), true);
                                       layer.getChildFrame('#data-detail-submit', index).hide();
                                       detailScreen(index);
                                       layero.find('iframe')[0].contentWindow.layui.form.render('select');
                                   } else if (res.code === 403){
                                       parent.location.href = "/";
                                   }else {
                                       layer.msg(res.msg)
                                   }
                               }
                           })
                       }
                   });
                }
                break;

        }
    });

    // 数据修改动作
    form.on('submit(edit)', function () {
        var index = layer.load(1, {
            shade: [0.5,'#000'] //0.1透明度的背景
        });
        var data = {
            id: $('#id').val(),
            appId: $('#appId').val(),
            name: $('#name').val(),
            partner: $('#partner').val(),
            subject: $('#subject').val(),
            charset: $('#charset').val(),
            signType: $('#signType').val(),
            privateKey: $('#privateKey').val(),
            publicKey: $('#publicKey').val(),
            weight: $('#weight').val(),
            sort: $('#sort').val(),
            memo: $('#memo').val(),
            createTime: top.strToDate($('#createTime\\$').val()),
            updateTime: top.strToDate($('#updateTime\\$').val()),
            editor: $('#editor').val(),
            status: $('#status').val(),

        };
        $.ajax({
            url: "/merchant/edit/auth",
            headers: {'token': localStorage.getItem('token')},
            data: top.reObject(data),
            method: 'POST',
            success: function (res) {
                if (res.code === 200){
                    parent.layer.closeAll();
                    tableReload(true);
                    $("#data-detail :input").each(function () {
                        $(this).val("");
                    });
                } else if (res.code === 403){
                    top.location.href = "/";
                }else {
                    layer.msg(res.msg)
                }
                layer.close(index);
            }
        })
    });

    // 搜索栏搜索事件
    form.on('submit(search)', function (data) {
        pageCurr = 1;
        tableReload(false);
    });

    // 搜索栏重置事件
    form.on('submit(reset)', function (data) {
        pageCurr = 1;
        clearFormVal($('#search-box'));
        tableReload(false);
    });

    // 时间选择器
    layDate.render({
        elem: '#createTime\\$',
        type: 'datetime'
    });
    layDate.render({
        elem: '#updateTime\\$',
        type: 'datetime'
    });


});

// 关闭动作
$(document).on('click','#data-detail-close', function () {
    parent.layer.closeAll();
});

function tableReload(child) {
    var searchData = {};
    $.each($('#search-box [name]').serializeArray(), function() {
        searchData[this.name] = this.value;
    });
    (child ? parent.tableIns : tableIns).reload({
        where: searchData,
        page: {
            curr: pageCurr
        },
        done: function (res, curr, count) {
            if (res.code === 403) {
                top.location.href = "/";
            }
            pageCurr=curr;
            if (res.data.length === 0 && count !== 0) {
                tableIns.reload({
                    where: searchData,
                    page: {
                        curr: pageCurr-1
                    }
                });
                pageCurr -= 1;
            }
        }
    });
}

function setFormVal(el, data, showImg) {
    for (var val in data) {
        var find = el.find(":input[id='" + val + "']");
        find.val(data[val]);
        if (showImg){
            var next = find.next();
            if (next.get(0)){
                if (next.get(0).localName === "img") {
                    find.hide();
                    next.attr("src", data[val]);
                    next.show();
                }
            }
        }
    }
}

function clearFormVal(el) {
    $(':input', el)
        .val('')
        .removeAttr('checked')
        .removeAttr('selected');
}

function detailScreen(index) {
    var detail = layer.getChildFrame('#data-detail', index);
    var height = detail.height()+60;
    if (height > ($(window).height()*0.9)) {
        height = ($(window).height()*0.9);
    }
    layer.style(index, {
        top: (($(window).height()-height)/3)+"px",
        height: height+'px'
    });
    $(".layui-layer-shade").remove();
}

$('body').keydown(function () {
    if (event.keyCode === 13) {
        $("#search").click();
    }
});
