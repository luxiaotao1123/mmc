layui.use(['form', 'tree'], function() {
    var form = layui.form;
    var tree = layui.tree;
    var $ = layui.jquery;
    var layer = layui.layer;

    // 权限分配树形图
    var powerTree = tree.render({
        elem: '#power-tree',
        id: 'powerTree',
        showCheckbox: true,
        data: parent.powerTreeData,
        isJump: true
    });

    $.ajax({
        url: "/power/"+parent.roleId+"/auth",
        headers: {'token': localStorage.getItem('token')},
        method: 'GET',
        success: function (res) {
            if (res.code === 200){
                tree.setChecked('powerTree', res.data);
            } else if (res.code === 403){
                top.location.href = "/";
            } else {
                layer.msg(res.msg)
            }
        }
    });

    // 数据修改动作
    form.on('submit(save)', function () {
        var param = [];
        var checkData = tree.getChecked('powerTree');
        checkData.map(function (obj) {
            obj.children.map(function (resource) {
                param.push(resource.id);
            })
        });
        $.ajax({
            url: "/power/auth",
            traditional: true,
            headers: {'token': localStorage.getItem('token')},
            data: {
                'roleId': parent.roleId,
                'powers': param
            },
            method: 'POST',
            success: function (res) {
                if (res.code === 200){
                    parent.layer.closeAll();
                } else if (res.code === 403){
                    top.location.href = "/";
                } else {
                    layer.msg(res.msg)
                }
            }
        });
    });
});

// 关闭动作
$(document).on('click','#data-detail-close', function () {
    parent.layer.closeAll();
});