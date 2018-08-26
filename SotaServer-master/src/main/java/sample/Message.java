package sample;

import java.util.Map;

public class Message {
  public String controller;
  public String action;
  public Map<String, String> data;
  public String speech;
    
  public Message(String controller, String action, Map<String, String> data,String speech) {
    this.controller = controller;
    this.action     = action;
    this.data       = data;
    this.speech  = speech;
  }
}