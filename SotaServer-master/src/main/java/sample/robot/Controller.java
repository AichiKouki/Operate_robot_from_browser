package sample.robot;

import java.util.Map;

public interface Controller {
	//script.jsの中の、操作の種類actionとmanualを選択した際の手足の座標が入ったdata
	//MotionControllerのexecuteActionを呼び出す
  public void executeAction(String action, Map<String, String> data);  		  
}