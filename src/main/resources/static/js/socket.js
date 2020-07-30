/**
 * Create by ghostxbh 2020/7/28
 */
layui.define(['element', 'layer', 'form'], function (exports) {
    var $ = layui.jquery;
    var host = window.document.location.host;
    var pathName = window.document.location.pathname;
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    var wsServer = "ws://" + host + projectName;

    var webSocket = null;
    if ('WebSocket' in window || 'MozWebSocket' in window) {
        webSocket = new WebSocket(wsServer + "/systemInfoSocketServer");
    } else {
        webSocket = new SockJS(wsServer + "/sockjs/systemInfoSocketServer");
    }

    webSocket.onerror = function (event) {
        layer.msg("websockt连接发生错误，请刷新页面重试!", { icon: 5 })
    };

    // 接收到消息的回调方法
    webSocket.onmessage = function (event) {
        var res = event.data;
        if (window.Notification && Notification.permission !== "denied") {
            Notification.requestPermission(function (status) {
                new Notification('系统通知', {body: res});
            });
        } else {
            layer.msg("系统通知: " + res, { icon: 6, time: 2000 })
        }
        location.href = "/notice/";
        var audio = window.document.createElement("audio");
        audio.src = "/notice/layui/css/modules/layim/voice/default.wav";
        audio.play()
    };
    exports('socket', {});
});