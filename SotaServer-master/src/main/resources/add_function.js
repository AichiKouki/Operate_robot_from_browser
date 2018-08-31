var uri = "ws://" + location.host + '/api';
var webSocket = null;

window.onload = function() {
  ConnectionHandler.checkConnection();
  setInterval(ConnectionHandler.checkConnection, 1000);
  open();
  MessageBox.setUpMessageBox();
};

// WebSocket start
function open() {
  if (webSocket == null) {
    webSocket = new WebSocket(uri);
    webSocket.onopen    = onOpen;
    webSocket.onmessage = onMessage;
    webSocket.onclose   = onClose;
    webSocket.onerror   = onError;
  }
}

function onOpen(event) {
	console.log('connecting');
}

function onMessage(event) { 
	//if (!event || !event.data) return;
	//console.log(event.data); 
}

function onClose(event) {
	console.log('disconnected!');
}

function onError(event) {
	console.log('error!');
}
// WebSocket end


// MessageBox start
//JQueryでボタンを押しただけでsotaを動かす処理
$(function(){
        $("#motion_button").on("click",function(){
                var action="index";//仮に設定(indexは反映しない)
                var data   = ReturnMotionId($(this).val());
                var vals;
                var speech="";
                                                
                var message = {
                  'action': action,
                  'data'  : data,
                  'speech' : speech,
                };
    var jsonString = JSON.stringify(message);
    webSocket.send(jsonString);

        });
});

//ボタンの種類によって、モーションの座標をreturnする関数
function ReturnMotionId(motionFamily){
    var array;
    if(motionFamily=="azatoi")  array = {1:0, 2:180, 3:-850, 4:-180, 5:850, 6:-0, 7:0, 8:0};
    else  array = {1:0, 2:-900, 3:-0, 4:900, 5:0, 6:-0, 7:0, 8:0};
    return array;
}

// ConnectionHandler start
ConnectionHandler = {
  'connectionStatus': document.getElementById('connectionStatus'),
  'connecting'   : function() { 
    connectionStatus.innerText   = '接続をしようとしているよ...'; //connecting...
    connectionStatus.style.color = 'red';
  },
  'open'      : function() {
    connectionStatus.innerText = '接続中';//open
    connectionStatus.style.color = 'lime';//ライム色
  },
  'closing'   : function() { 
    connectionStatus.innerText   = '接続を閉じようとしているよ'; //closing...
    connectionStatus.style.color = 'red';
  },
  'closed'   : function() {
    connectionStatus.innerText = '接続してないよ';//closed
    connectionStatus.style.color = 'red';
  },
  'checkConnection': function() {
    if (webSocket == null) {
      ConnectionHandler.connecting();
      return;
    }
    
    switch (webSocket.readyState) {
      case 0:
        ConnectionHandler.connecting();
        break;
      case 1:
        ConnectionHandler.open();
        break;
      case 2:
        ConnectionHandler.closing();
        break;
      case 3:
        ConnectionHandler.closed();
        break;
    }
  }
}
// ConnectionHandler end