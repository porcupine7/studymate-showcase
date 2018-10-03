package at.fhooe.studymate.entities.records;

import com.google.gson.annotations.SerializedName;

/**
 * Super class to send measures and answers to server
 */
public class Record {
  @SerializedName("timestamp")
  private long timestamp;

  @SerializedName("participant")
  private String participant;

  @SerializedName("task")
  private String task;

  @SerializedName("app")
  private String app;

  @SerializedName("instruction")
  private String instruction;

  @SerializedName("civ")
  private String customIv;

  @SerializedName("rep")
  private String rep;

  Record(){
  }

  public Record(long timestamp, String participant, String task, String app, String instruction, String customIv, String rep) {
    this.timestamp = timestamp;
    this.participant = participant;
    this.task = task;
    this.app = app;
    this.instruction = instruction;
    this.customIv = customIv;
    this.rep = rep;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getParticipant() {
    return participant;
  }

  public String getTask() {
    return task;
  }

  public String getApp() {
    return app;
  }

  public String getInstruction() {
    return instruction;
  }

  public String getCustomIv() {
    return customIv;
  }

  public String getRep() {
    return rep;
  }
}
