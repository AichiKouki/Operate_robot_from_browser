$(function(){
    //ボタンを押して、喋る内容を自動で入力
    
    //null2xの紹介
        $("#frequent_words1").on("click",function(){
            //valメソッドでinputフィールドのvalueを更新
                $("#speechText").val("こんにちは。ぬるつーエックスです。VRやARなどのゲームやAI開発、映像などが展示してあるのでごらんください");
        });
        //自己紹介
        $("#frequent_words2").on("click",function(){
            //valメソッドでinputフィールドのvalueを更新
                $("#speechText").val("こんにちは、僕は、そーたくんだよ。可愛いでしょ？");
        });

        $("#frequent_words3").on("click",function(){
            //valメソッドでinputフィールドのvalueを更新
                $("#speechText").val("ありがとうございます！");
        });

        $("#frequent_words4").on("click",function(){
            //valメソッドでinputフィールドのvalueを更新
                $("#speechText").val("早口言葉を言います。生麦生米生卵、生麦なまごみゅ、あ、失敗しました");
        });

        $("#frequent_words5").on("click",function(){
            //valメソッドでinputフィールドのvalueを更新
                $("#speechText").val("空前絶後の超絶孤高のロボットくん！ドラえもんを愛し、ドラえもんに愛された男！時給1240円、貯金残高84800円、キャッシュカードの暗証番号8931！もう1度言います！8931！ハクサイって覚えてくださーい！イエエェェー！！ジャスティス！");
        });

});