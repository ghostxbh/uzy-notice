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
            url: '/notice/list',
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
                        html += "<td><button data-method=\"offset\" data-type=\"auto\" class=\"layui-btn layui-btn-admin\" value='" + JSON.stringify(item.headers) + "'>"
                            + item.headers.substring(0, 30) + '...' + "</button></td>";
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
                , content: '/' + text
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
                , id: type
                , title: 'JSON数据展示'
                , area: ['700px', '500px']
                , content: `<div style="padding: 35px"><pre>${formatJSON(json)}</pre></div>`
                , btn: ['关闭', '复制']
                , btnAlign: 'c' //按钮居中
                , shade: 0.6 //不显示遮罩
                , maxmin: true //允许全屏最小化
                , anim: 1 //0-6的动画形式，-1不开启
                , yes: function () {
                    layer.closeAll();
                }
                , btn2: function () {
                    var oInput = document.createElement('input');
                    oInput.value = JSON.parse(json);
                    oInput.style = 'position: absolute;top: -100px;';
                    document.body.appendChild(oInput);
                    oInput.select();
                    var command = document.execCommand("Copy");
                    if (command) {
                        layer.msg("复制成功", { icon: 1, time: 2000 })
                    }
                }
                , btn3: function () {
                    $('.jsonTxtInput').val(json);
                }
            });
        }
    };
    $('.layui-btn').on('click', function () {
        var othis = $(this), method = othis.data('method');
        active[method] ? active[method].call(this, othis) : '';
    });


    var host = window.document.location.host;
    var pathName = window.document.location.pathname;
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var wsServer = "wss://" + host + projectName;
    var webSocket;
    if ('WebSocket' in window || 'MozWebSocket' in window) {
        webSocket = new WebSocket(wsServer + "/socket.io/systemInfoSocketServer");
    } else {
        webSocket = new SockJS(wsServer + "/socket.io/sockjs/systemInfoSocketServer");
    }

    webSocket.onerror = function (event) {
        layer.msg("websockt连接发生错误，请刷新页面重试!", { icon: 5 })
    };

    // 接收到消息的回调方法
    webSocket.onmessage = function (event) {
        var res = event.data;
        var audio = window.document.createElement("audio");
        audio.src = "/notice/layui/css/modules/layim/voice/default.wav";
        audio.play().then(r => console.log(r));
        initilData(1, 10);

        if (window.Notification && Notification.permission !== "denied") {
            Notification.requestPermission(function (status) {
                new Notification('系统通知', {body: res});
            });
        } else {
            layer.msg("系统通知: " + res, { icon: 6, time: 2000 })
        }
    };

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

function formatJSON (json, indent, leftBracesInSameLine) {
    json = JSON.parse(json);
    function getIndentStr(level)
    {
        var str = '';
        for(var i=0; i<level; i++) str += (indent || '    ');
        return str;
    }
    function format(obj, level)
    {
        level = level == undefined ? 0 : level;
        var result = '';
        if(typeof obj == 'object' && obj != null) // 如果是object或者array
        {
            var isArray = obj instanceof Array, idx = 0;
            result += (isArray ? '[' : '{') + '\n';
            for(var i in obj)
            {
                result += (idx++ > 0 ? ',\n' : ''); // 如果不是数组或对象的第一个内容，追加逗号
                var nextIsObj = (typeof obj[i] == 'object' && obj[i] != null), indentStr = getIndentStr(level+1);
                result += (isArray && nextIsObj) ? '' : indentStr; // 如果当前是数组并且子项是对象，无需缩进
                result += isArray ? '' : ('"' + i + '": ' + (nextIsObj && !leftBracesInSameLine ? '\n' : '') );
                result += (!nextIsObj || (nextIsObj && leftBracesInSameLine && !isArray)) ? '' : indentStr;
                result += format(obj[i], level+1); // 递归调用
            }
            result += '\n' + getIndentStr(level) + (isArray ? ']' : '}') + '';
        }
        else // 如果是 null、number、boolean、string
        {
            var quot = typeof obj == 'string' ? '"' : '';//是否是字符串
            result += (quot + obj + quot + '');
        }
        return result;
    }
    return format(eval('(' + json + ')')); // 使用eval的好处是可以解析非标准JSON
}

/**
 * json美化
 *   jsonFormat2(json)这样为格式化代码。
 *   jsonFormat2(json,true)为开启压缩模式
 * @param txt
 * @param compress
 * @returns {string}
 */
function jsonFormat1(txt,compress){
    var indentChar = '    ';
    if(/^\s*$/.test(txt)){
        alert('数据为空,无法格式化! ');
        return;
    }
    try{var data=eval('('+txt+')');}
    catch(e){
        alert('数据源语法错误,格式化失败! 错误信息: '+e.description,'err');
        return;
    };
    var draw=[],last=false,This=this,line=compress?'':'\n',nodeCount=0,maxDepth=0;

    var notify=function(name,value,isLast,indent/*缩进*/,formObj){
        nodeCount++;/*节点计数*/
        for (var i=0,tab='';i<indent;i++ )tab+=indentChar;/* 缩进HTML */
        tab=compress?'':tab;/*压缩模式忽略缩进*/
        maxDepth=++indent;/*缩进递增并记录*/
        if(value&&value.constructor==Array){/*处理数组*/
            draw.push(tab+(formObj?('"'+name+'":'):'')+'['+line);/*缩进'[' 然后换行*/
            for (var i=0;i<value.length;i++)
                notify(i,value[i],i==value.length-1,indent,false);
            draw.push(tab+']'+(isLast?line:(','+line)));/*缩进']'换行,若非尾元素则添加逗号*/
        }else   if(value&&typeof value=='object'){/*处理对象*/
            draw.push(tab+(formObj?('"'+name+'":'):'')+'{'+line);/*缩进'{' 然后换行*/
            var len=0,i=0;
            for(var key in value)len++;
            for(var key in value)notify(key,value[key],++i==len,indent,true);
            draw.push(tab+'}'+(isLast?line:(','+line)));/*缩进'}'换行,若非尾元素则添加逗号*/
        }else{
            if(typeof value=='string')value='"'+value+'"';
            draw.push(tab+(formObj?('"'+name+'":'):'')+value+(isLast?'':',')+line);
        };
    };
    var isLast=true,indent=0;
    notify('',data,isLast,indent,false);
    $("#geoJsonTxt").html(draw.join(''));
}

/**
 * json格式化便于美观在html页面输出
 * @param json
 * @param options
 * @returns {string}
 */
function jsonFormat2(json, options) {
    json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>').replace(/\\n/g, '');
    var reg = null,
        formatted = '',
        pad = 0,
        PADDING = '    '; // one can also use '\t' or a different number of spaces

    // optional settings
    options = options || {};
    // remove newline where '{' or '[' follows ':'
    options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
    // use a space after a colon
    options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

    // begin formatting...
    if (typeof json !== 'string') {
        // make sure we start with the JSON as a string
        json = JSON.stringify(json);
    } else {
        // is already a string, so parse and re-stringify in order to remove extra whitespace
        json = JSON.parse(json);
        json = JSON.stringify(json);
    }

    // add newline before and after curly braces
    reg = /([\{\}])/g;
    json = json.replace(reg, '\r\n$1\r\n');

    // add newline before and after square brackets
    reg = /([\[\]])/g;
    json = json.replace(reg, '\r\n$1\r\n');

    // add newline after comma
    reg = /(\,)/g;
    json = json.replace(reg, '$1\r\n');

    // remove multiple newlines
    reg = /(\r\n\r\n)/g;
    json = json.replace(reg, '\r\n');

    // remove newlines before commas
    reg = /\r\n\,/g;
    json = json.replace(reg, ',');

    // optional formatting...
    if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
        reg = /\:\r\n\{/g;
        json = json.replace(reg, ':{');
        reg = /\:\r\n\[/g;
        json = json.replace(reg, ':[');
    }
    if (options.spaceAfterColon) {
        reg = /\:/g;
        json = json.replace(reg, ': ');
    }

    $.each(json.split('\r\n'), function(index, node) {
        var i = 0,
            indent = 0,
            padding = '';

        if (node.match(/\{$/) || node.match(/\[$/)) {
            indent = 1;
        } else if (node.match(/\}/) || node.match(/\]/)) {
            if (pad !== 0) {
                pad -= 1;
            }
        } else {
            indent = 0;
        }

        for (i = 0; i < pad; i++) {
            padding += PADDING;
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });
    $("#geoJsonTxt").html(formatted.substring(1, formatted.length - 3));
}
