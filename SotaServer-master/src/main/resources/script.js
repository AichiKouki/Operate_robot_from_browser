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
	if (!event || !event.data) return;
	console.log(event.data); 
}

function onClose(event) {
	console.log('disconnected!');
}

function onError(event) {
	console.log('error!');
}
// WebSocket end


// MessageBox start
//index.htmlから操作の種類を受け取る
MessageBox = {
  'setUpMessageBox': function() {//ロードした時点で読み込む
    var sendMessage = document.getElementById('submitMessage');
    sendMessage.addEventListener('click', MessageBox.submitMessage);
    var selectBox = document.querySelector('select[name="action"]');
    selectBox.addEventListener('change', MessageBox.changeAction);
    var textBox = document.querySelector('input[name="speechText"]');

  },
  'submitMessage': function(e) {//index.htmlのsubmitボタンが押されたら処理
    e.preventDefault();
    var action = document.querySelector('select[name="action"]').value;
    var speech = document.querySelector('input[name="speechText"]').value;

    if(!action) return;
    var keys   = document.querySelectorAll('input[name="data[key][]"]');
    var vals   = document.querySelectorAll('input[name="data[val][]"]');
    //manualを選択した場合は、複数の入力ボックスの値を全て取得
    var data   = {};
    for (var i = 0; i < keys.length; i++) {
      data[keys[i].value]  = vals[i].value;
    }
    
    //オリジナルのモーションをさせるか
    var do_original_motion=false;//このスクリプトは関係ないのでfalseで固定
    var motion_family=$(this).val();//モーションの種類を設定
//index.htmlから受け取ったアクションの名前を設定
    var message = {
      'action': action,
      'data'  : data,
      'speech' : speech,
    };
    var jsonString = JSON.stringify(message);
    webSocket.send(jsonString);
  },
  'changeAction': function(e) {//Javaに、sotaの体の動きを表す座標をここで作成。あとでjava側に送る
    var action = e.target.value;
    var vals = document.querySelectorAll('input[name="data[val][]"]');
    switch(action) {
      case 'index':
        [0, 0, -800, 0, 800, 0, 50, 0].map(function(v, i) { vals[i].value = v;});
        break;
      case 'kangFu':
        [500, 300, -300, 500, 500, -300, 0, 300].map(function(v, i) { vals[i].value = v;});
        break;
      case 'banzai':
        [0, 500, -100, -500, 100, 0, -200, 0].map(function(v, i) { vals[i].value = v;});
        break;
      default:
        [0, 0, 0, 0, 0, 0, 0, 0].map(function(v, i) { vals[i].value = v;});
    }
    //manualを選択した際の、いっぱいの入力ボックスでの手足のパラメータを取得
    var dataFields = document.getElementById('messageData');
    if (action != 'manual') {
     dataFields.style.display = 'none';
     return;
    }
    dataFields.style.display = '';

  },
  'displayMessage': function(message) {
    var itemList = document.getElementById('itemList');
    var item = document.createElement('div');
    item.innerText = message.action;
    itemList.insertBefore(item, itemList.childNodes[0]);
  },
}
// MessageBox end


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