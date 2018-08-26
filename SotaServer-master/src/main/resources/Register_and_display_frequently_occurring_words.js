//頻出単語を登録・表示するためのスクリプト
$(function(){        
        //テキストを登録するので、HTMLタグごとtxtファイルに書き込む
        $("#register_button").on("click",function(){
            var fs = WScript.CreateObject("Scripting.FileSystemObject");
            var file = fs.CreateTextFile("data.txt");
            file.Write("マルペケつくろ～");
            file.Close();
        $("#register_sentences").replaceWith("<p>replaceWithで置き換えた</p>");
        });
        
        //テキストを登録して生成されたボタンを押したら、送信部分のinputフィールドのvalueを書き換える
    });