package sample.robot;

import java.awt.Color;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import jp.vstone.RobotLib.CRobotMem;
import jp.vstone.RobotLib.CRobotPose;
import jp.vstone.RobotLib.CRobotUtil;
import jp.vstone.RobotLib.CSotaMotion;
import sample.Message;
import sample.server.MyWebSocketListener;
//喋らせるための
import jp.co.nttit.speechrec.Nbest;
import jp.vstone.RobotLib.CPlayWave;
import jp.vstone.RobotLib.CRobotUtil;
import jp.vstone.sotatalk.SpeechRecog;
import jp.vstone.sotatalk.TextToSpeechSota;

public class RobotAPI {
  private static final Logger LOG = Log.getLogger(MyWebSocketListener.class);
  protected static CRobotMem memory;
  protected static CSotaMotion motion;
  protected static boolean sparkling = true;
  
  static final String TAG = "RobotAPI";

  public static void call(Message json) {
    boolean properlySetUp = setUpRobot();
    if (!properlySetUp) return;
    //actionがoriginalなら、以下の処理はしないで、オリジナルのクラスを作ってそれを実行
    if(json.action.contains("original")){
        LOG.info("愛知愛知、Original_Motionメソッドが呼ばれる");
    	Original_Motion original_motion = new Original_Motion();
    	original_motion.do_motion(json.action);
    }else{
    LOG.info("愛知愛知、actionの値は"+json.action);
    LOG.info("愛知愛知、dataの値は"+json.data);    
    //手足を動かすためにControllerクラスにactionとdataを渡す
    Controller controller = new MotionController();
    controller.executeAction(json.action, json.data);//json.actionでhtmlで選択したactionを取得
    //喋らせるため
    TextToSpeech textToSpeech=new TextToSpeech();
    textToSpeech.Speech(json.speech);
    }
    //カメラ処理(「今、カメラの調子が悪いよ」と発話してしまう。故障の可能性がある)
    /*
    CameraController cameraController = new CameraController();
    cameraController.executeAction(json.action, json.data);
    */
  }

  public static void terminateRobot() {
    CRobotPose pose = new CRobotPose();
    pose.setLED_Sota(Color.BLACK, Color.BLACK, 255, Color.BLACK);
    pose.SetPose(new Byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, new Short[] { 0, 0, -1000, 0, 1000, 0, 50, 0 });
    motion.play(pose, 500);
    CRobotUtil.wait(1000);
    motion.ServoOff();
  }

  public static boolean setUpRobot() {
    LOG.info("Start Set Up Robot");
    if (memory != null && memory.isConected()) {
      LOG.info("Already Properly Set Up");
      LOG.info("Finish Set Up Robot");
      return true;
    }
    memory = new CRobotMem();
    motion = new CSotaMotion(memory);

    if (!memory.Connect()) {
      LOG.info("Failed to connect memory");
      return false;
    }

    motion.InitRobot_Sota();
    motion.ServoOn();
    LOG.info("Finish Set Up Robot");
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      terminateRobot();
    }));
    return true;
  }

  public static void eyesSparkling() {
    Thread thread = new Thread(() -> {
      LOG.info("Eyes Sparkling: start");
      Color[] colors = { Color.GREEN, Color.ORANGE, Color.BLUE };
      int mod = colors.length;
      int index = 0;
      CRobotPose pose = new CRobotPose();
      pose.SetPose(new Byte[] { 1, 2, 3, 4, 5, 6, 7, 8 }, new Short[] { 0, 500, -100, -500, 100, 0, -300, 0 });
      motion.play(pose, 1000);
      CRobotUtil.wait(1000);

      while (sparkling) {
        index = index % mod;
        pose = new CRobotPose();
        pose.setLED_Sota(colors[index], colors[index], 255, Color.GREEN);
        motion.play(pose, 500);
        CRobotUtil.wait(500);
        index++;
      }

      pose = new CRobotPose();
      pose.setLED_Sota(Color.BLACK, Color.BLACK, 255, Color.GREEN);
      motion.play(pose, 500);
      CRobotUtil.wait(500);
      LOG.info("Eyes Sparkling: end");
    });
    thread.run();
  }
}
