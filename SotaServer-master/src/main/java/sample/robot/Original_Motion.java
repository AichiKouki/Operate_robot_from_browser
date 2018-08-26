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
	static final String TAG = "Original_Motion";
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
			/*
			pose = new CRobotPose();
			pose.SetPose(new Byte[] {1   ,2   ,3   ,4   ,5   ,6   ,7   ,8}	//id
			,  new Short[]{0   ,-900,0   ,900 ,0   ,0   ,0   ,0}				//target pos
					);

				//音声ファイル再生
				//raw　Waveファイルのみ対応
				//CPlayWave.PlayWave("sound/kimigayo_hatune.wav");
			*/
			
			//LEDを点灯（左目：赤、右目：赤、口：Max、電源ボタン：赤）
			pose = new CRobotPose();
			pose.setLED_Sota(Color.RED, Color.RED, 255, Color.RED);

			//あざといポーズ
			if(motion_family.equals("original1")){
				Azatoi(pose,motion);
			}else if(motion_family.equals("original2")){
				Wave_hands(pose,motion);
			}
			CRobotUtil.wait(2000);//ミリ秒を指定。2000は2秒。2秒処理を待機する
				
			//手を下ろす
			pose = new CRobotPose();
			//第2引数（Short[]）にて制御対象の角度を指定
			pose.SetPose(new Byte[] {1   ,2   ,3   ,4   ,5   ,6   ,7   ,8}	//id
						,  new Short[]{0   ,-900   ,0   ,900   ,0   ,0   ,0   ,0}	//target pos
			);
			pose.setLED_Sota(Color.BLUE, Color.BLUE, 255, Color.BLUE);
			motion.play(pose,1000);
			motion.waitEndinterpAll();

			//サーボモータのトルクオフ
			//CRobotUtil.Log(TAG, "Servo Off");
			//motion.ServoOff();
		}
	}
	
	//あざといポーズ
	public static void Azatoi(CRobotPose pose,CSotaMotion motion){
		pose.SetPose(new Byte[] { 1, 2, 3, 4, 5 }, new Short[] { 0, 180, -850, -180, 850 });
		//遷移時間1000msecで動作開始。つまり1秒間かけて動かす
		CRobotUtil.Log(TAG, "play:" + motion.play(pose,1000));
		//補間完了まで待つ
		motion.waitEndinterpAll();			
		
		//文字列で喋らせる
		CPlayWave.PlayWave(TextToSpeechSota.getTTSData("あざといポーズだよ",10,13,11),true);
	}
	
	//手を振るモーション
	public static void Wave_hands(CRobotPose pose,CSotaMotion motion){
		pose.SetPose(new Byte[] { 1, 2, 3, 4, 5 }, new Short[] { 0, 180, 0, -180, 0 });
		//遷移時間1000msecで動作開始。つまり1秒間かけて動かす
		CRobotUtil.Log(TAG, "play:" + motion.play(pose,1000));
		//補間完了まで待つ
		motion.waitEndinterpAll();			
		
		//文字列で喋らせる
		CPlayWave.PlayWave(TextToSpeechSota.getTTSData("両手を上げてみた",10,13,11),true);

	}
}