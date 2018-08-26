package sample.robot;

import static jp.vstone.RobotLib.CSotaMotion.SV_BODY_Y;
import static jp.vstone.RobotLib.CSotaMotion.SV_HEAD_P;
import static jp.vstone.RobotLib.CSotaMotion.SV_HEAD_R;
import static jp.vstone.RobotLib.CSotaMotion.SV_HEAD_Y;
import static jp.vstone.RobotLib.CSotaMotion.SV_L_ELBOW;
import static jp.vstone.RobotLib.CSotaMotion.SV_L_SHOULDER;
import static jp.vstone.RobotLib.CSotaMotion.SV_R_ELBOW;
import static jp.vstone.RobotLib.CSotaMotion.SV_R_SHOULDER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import jp.vstone.RobotLib.CRobotPose;
import jp.vstone.RobotLib.CRobotUtil;

public class MotionController implements Controller {
  private static final Logger LOG = Log.getLogger(MotionController.class);
  public static final Byte[] SV_ALL = new Byte[] { SV_BODY_Y, SV_L_SHOULDER, SV_L_ELBOW, SV_R_SHOULDER, SV_R_ELBOW,
      SV_HEAD_Y, SV_HEAD_P, SV_HEAD_R, };
  public static CRobotPose pose;

  @Override
  public void executeAction(String action, Map<String, String> data) {
    Thread thread = new Thread(new Runnable(){
      @Override
      public void run() {
        RobotAPI.motion.LockServoHandle(SV_ALL);
        switch (action) {
        case "manual":
          setServo(data, true);
          break;
        default:
          setServo(data, false);
          break;
        }
        //RobotAPI.motion.UnLockServoHandle();
      }
    });

    thread.run();
  }

  private void setServo(Map<String, String> data, boolean limit) {
    List<Byte> idsList = new ArrayList<Byte>();
    List<Short> posList = new ArrayList<Short>();

    Byte key;
    Short val;
    for (Map.Entry<String, String> entry : data.entrySet()) {
      LOG.info("key: " + entry.getKey() + ", value: " + entry.getValue());
      try {
        Integer.parseInt(entry.getKey());
        Integer.parseInt(entry.getValue());
      } catch (NumberFormatException e) {
        continue;
      }

      key = Byte.valueOf(entry.getKey());
      val = Short.valueOf(entry.getValue());
      switch (key) {
      case SV_BODY_Y:
      case SV_L_SHOULDER:
      case SV_L_ELBOW:
      case SV_R_SHOULDER:
      case SV_R_ELBOW:
      case SV_HEAD_Y:
      case SV_HEAD_P:
      case SV_HEAD_R:
        idsList.add(key);
        if(limit) {
          posList.add(limitAngle(key, val));
        } else {
          posList.add(val);
        }
        break;
      }
    }

    Byte[] ids = idsList.toArray(new Byte[idsList.size()]);
    Short[] pos = posList.toArray(new Short[posList.size()]);

    pose = new CRobotPose();
    pose.SetPose(ids, pos);
    RobotAPI.motion.play(pose, 500);
    CRobotUtil.wait(500);
  }

//曲げすぎて故障しないように制限をかけている
  private Short limitAngle(Byte key, Short val) {
    switch(key) {
      case SV_BODY_Y://体全体を回転
        if (val < -1000) val = -1000;
        if (val >  1000) val =  1000;
        break;
      case SV_L_SHOULDER: //左手をあげる
        if (val <  -500) val =  -500;
        if (val >  500) val =   500;
        break;
      case SV_L_ELBOW://左手の肘を曲げる
        if (val < -1000) val = -1000;
        if (val >   800) val =   800;
        break;
      case SV_R_SHOULDER://右手をあげる(マイナスで上にあげる)
        if (val <     -500) val =  500;
        if (val >   500) val =   500;
        break;
      case SV_R_ELBOW://右手の肘を曲げる
        if (val <  -800) val =  -800;
        if (val >  1000) val =  1000;
        break;
      case SV_HEAD_Y://頭を横に向ける
        if (val <  -500) val =  -500;
        if (val >   500) val =   500;
        break;
      case SV_HEAD_P://頭を上に向ける
        if (val <  -300) val =  -300;
        if (val >    50) val =    50;
        break;
      case SV_HEAD_R://頭を横にかしげる
        if (val <  -200) val =  -200;
        if (val >   200) val =   200;
        break;
    }
    return val;
  }
}
