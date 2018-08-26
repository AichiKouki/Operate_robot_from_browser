package sample.robot;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import jp.vstone.RobotLib.*;
import jp.vstone.sotatalk.TextToSpeechSota;
/**
 * VSMDを使用し、モーション再生・音声再生するサンプル
 * @author Vstone
 *
 */
public class Original_Motion {
	static final String TAG = "MotionSample";
	public void do_motion(String motion_family){
		CRobotUtil.Log(TAG, "Start " + TAG);

		CRobotPose pose;
		//VSMDと通信ソケット・メモリアクセス用クラス
		CRobotMem mem = new CRobotMem();
		//Sota用モーション制御クラス
		CSotaMotion motion = new CSotaMotion(mem);

		if(mem.Connect()){
			//Sota仕様にVSMDを初期化
			motion.InitRobot_Sota();

			CRobotUtil.Log(TAG, "Rev. " + mem.FirmwareRev.get());

			//サーボモータを現在位置でトルクOnにする
			CRobotUtil.Log(TAG, "Servo On");
			motion.ServoOn();

			//すべての軸を動作
			pose = new CRobotPose();
			pose.SetPose(new Byte[] {1   ,2   ,3   ,4   ,5   ,6   ,7   ,8}	//id
			,  new Short[]{0   ,-900,0   ,900 ,0   ,0   ,0   ,0}				//target pos
					);
			//LEDを点灯（左目：赤、右目：赤、口：Max、電源ボタン：赤）
			pose.setLED_Sota(Color.RED, Color.RED, 255, Color.RED);

			//遷移時間1000msecで動作開始。
			CRobotUtil.Log(TAG, "play:" + motion.play(pose,1000));

			//補間完了まで待つ
			motion.waitEndinterpAll();

			//一部の軸を指定して動作
			//CSotaMotionの定数を利用してID指定する場合
			pose = new CRobotPose();
			pose.SetPose(new Byte[] {CSotaMotion.SV_HEAD_R, CSotaMotion.SV_L_SHOULDER,CSotaMotion.SV_L_ELBOW,CSotaMotion.SV_R_ELBOW}	//id
						,  new Short[]{200, 700 ,-200,200}	//target pos
			);
			pose.setLED_Sota(Color.GREEN, Color.GREEN, 255, Color.GREEN);
			motion.play(pose,1000);
			motion.waitEndinterpAll();
			
			//文字列で喋らせる
			CPlayWave.PlayWave(TextToSpeechSota.getTTS("こんにちは。僕はそーたくんだよ。愛知こうき君。これからよろしくね！"),true);
			CPlayWave.PlayWave(TextToSpeechSota.getTTS("では、いきなりですが歌わせていただきます！"),true);
			//音声ファイル再生
			//raw　Waveファイルのみ対応
			//CPlayWave.PlayWave("sound/cursor10.wav");
			//CPlayWave.PlayWave("sound/kimigayo_hatune.wav");
			CRobotUtil.wait(2000);//ミリ秒を指定。2000は2秒。2秒処理を待機する
			
			CPlayWave.PlayWave(TextToSpeechSota.getTTS("ありがとうございました。また歌ってほしいときは言ってね"),true);

			pose = new CRobotPose();
			//第2引数（Short[]）にて制御対象の角度を指定
			pose.SetPose(new Byte[] {1   ,2   ,3   ,4   ,5   ,6   ,7   ,8}	//id
						,  new Short[]{0   ,-900   ,0   ,900   ,0   ,0   ,0   ,0}	//target pos
			);
			pose.setLED_Sota(Color.BLUE, Color.BLUE, 255, Color.BLUE);
			motion.play(pose,1000);
			motion.waitEndinterpAll();

			//サーボモータのトルクオフ
			CRobotUtil.Log(TAG, "Servo Off");
			motion.ServoOff();
		}
	}
}
/*
 ～Sotaのモーション指定のメモ～
 単位は、「1当たり0.1度」となります。

また、各軸の制御限界範囲は下記になります。

・ピッチ軸(うなずく方向)：
(上を向く) -290 ～   80(下を向く)

・ヨー軸(かしげる方向)：
(右に傾く) -250 ～  250(左に傾く)

・ロール軸(振り向く方向)：
(右を向く)-1450 ～ 1450(左を向く)

 */
