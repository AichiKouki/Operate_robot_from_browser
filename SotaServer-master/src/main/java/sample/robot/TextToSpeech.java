package sample.robot;

import jp.co.nttit.speechrec.Nbest;
import jp.vstone.RobotLib.CPlayWave;
import jp.vstone.RobotLib.CRobotUtil;
import jp.vstone.sotatalk.SpeechRecog;
import jp.vstone.sotatalk.TextToSpeechSota;
//入力された文字列を音声合成する
public class TextToSpeech {
	static final String TAG = "TextToSpeech";//同じ階層のJavaファイルの名前。おそらくこのJavaファイルも処理される
	public void Speech(String speech){
		CRobotUtil.Log(TAG,"おしゃべり用クラスを作成しておしゃべり");
		//CPlayWave.PlayWave("sound/kimigayo_hatune.wav");
		//CPlayWave.PlayWave(TextToSpeechSota.getTTS(speech),true);
		CPlayWave.PlayWave(TextToSpeechSota.getTTSData(speech,10,13,11),true);
	}
}

/*
 ・say_words…発話させる文章
 ・scene…発話するシーン
 ・Intonation…声の抑揚を1~15で設定。標準は11
 ・pitch…声の高さを1~20で設定。標準は13
 ・speechRate…話す速さを5~11で設定。標準は11
 
 ・getTTSData(java.lang.String text)  音声合成したWaveバイナリデータを取得
 ・getTTSData(java.lang.String text, int speechRate, int pitch, int intonation) 音声合成したWaveバイナリデータを取得
 
 (package private) static java.lang.String	getHwCode() 
static byte[]	getTTS(java.lang.String text)
音声合成したWaveバイナリデータを取得
static byte[]	getTTSData(java.lang.String text)
音声合成したWaveバイナリデータを取得
static byte[]	getTTSData(java.lang.String text, int speechRate, int pitch, int intonation)
音声合成したWaveバイナリデータを取得
static java.lang.String	getTTSFile(java.lang.String text)
音声合成したWaveファイルのパスを取得
static java.lang.String	getTTSFile(java.lang.String text, int speechRate, int pitch, int intonation)
音声合成したWaveファイルのパスを取得
(package private) static byte[]	getvoicedata(TextToSpeechSota.TTSRequest request) 
*/