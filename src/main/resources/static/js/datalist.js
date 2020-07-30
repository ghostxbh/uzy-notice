layui.define(['element', 'laypage', 'layer', 'form', 'pagesize'], function (exports) {
    var element = layui.element();
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form(),
        laypage = layui.laypage;
    var laypageId = 'pageNav';
    initilData(1, 10);

    /**
     * 初始化数据
     * currentIndex：当前也下标
     * pageSize：页容量（每页显示的条数）
     */
    function initilData(currentIndex, pageSize) {
        var index = layer.load(1);
        $.ajax({
            url: '/n/list',
            async: false,
            type: 'post',
            dateType: 'json',
            data: {'time': (new Date()).toString(), "pageNo": currentIndex, "pageSize": pageSize},
            success: function (result) {
                if (result.success) {
                    var count = result.body.count;
                    var data = result.body.list;
                    layer.close(index);
                    //计算总页数（一般由后台返回）
                    pages = Math.ceil(count / pageSize);
                    var html = '';
                    //遍历账单集合
                    for (var i = 0; i < data.length; i++) {
                        var item = data[i];
                        html += "<tr>";
                        html += "<td>" + item.id + "</td>";
                        html += "<td>" + item.type + "</td>";
                        html += "<td><button data-method=\"offset\" data-type=\"auto\" class=\"layui-btn layui-btn-admin\" value='" + item.contents + "'>"
                            + item.contents.substring(0, 30) + '...' + "</button></td>";
                        html += "<td>" + '' + "</td>";
                        html += "<td>" + item.createTime + "</td>";
                        html += "</tr>";
                    }
                    $('#dataContent').html(html);
                    $('#dataConsole,#dataList').attr('style', 'display:block'); //显示FiledBox
                    laypage({
                        cont: laypageId,
                        pages: pages,
                        groups: 8,
                        skip: true,
                        curr: currentIndex,
                        jump: function (obj, first) {
                            var currentIndex = obj.curr;
                            if (!first) {
                                initilData(currentIndex, pageSize);
                            }
                            $('.layui-btn').on('click', function () {
                                var othis = $(this), method = othis.data('method');
                                active[method] ? active[method].call(this, othis) : '';
                            });
                        }
                    });
                    /**
                     * 设置每页数量
                     */
                    layui.pagesize(laypageId, pageSize).callback(function (newPageSize) {
                        initilData(1, newPageSize);
                    });
                } else {
                    layer.msg(result.msg, {icon: 5});
                }
            }
        });
    }

    //触发事件
    var active = {
        setTop: function (othis) {
            var type = othis.data('type'), text = othis.text();
            //多窗口模式，层叠置顶
            layer.open({
                type: 2
                , title: 'JSON数据展示'
                , area: ['700px', '500px']
                , content: '/n/' + text
                , btn: '关闭'
                , btnAlign: 'c' //按钮居中
                , shade: 0.6 //不显示遮罩
                , maxmin: true //允许全屏最小化
                , anim: 1 //0-6的动画形式，-1不开启
                , yes: function () {
                    layer.closeAll();
                }
            });
        }
        , offset: function (othis) {
            var type = othis.data('type'), text = othis.text(), json = othis.val();
            layer.open({
                type: 1
                , title: 'JSON数据展示'
                , area: ['700px', '500px']
                , content: '<div style="padding: 35px"><pre id="geoJsonTxt">' + JsonFormat(json) + '</pre></div>'
                , btn: '关闭'
                , btnAlign: 'c' //按钮居中
                , shade: 0.6 //不显示遮罩
                , maxmin: true //允许全屏最小化
                , anim: 1 //0-6的动画形式，-1不开启
                , yes: function () {
                    layer.closeAll();
                }
                , success: function (layero) {
                    layer.setTop(layero); //重点2
                }
            });
        }
    };
    $('.layui-btn').on('click', function () {
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });
    exports('datalist', {});
});

function JsonFormat(json) {
    if (typeof json == 'string') {
        try {
            json = JSON.parse(json);
        } catch (e) {
            json = JSON.stringify(json, undefined, 2);
        }
    } else {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
    $("#geoJsonTxt").html(json);
}