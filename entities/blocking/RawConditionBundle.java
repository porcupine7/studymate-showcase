package at.fhooe.studymate.entities.blocking;

import com.google.gson.annotations.SerializedName;

/**
 * Condition bundle with ids as members
 * This is parsed from received json, but not used as model class
 * Convert it to {@link ConditionBundle} to use it.
 */
public class RawConditionBundle {
  @SerializedName(value = "task")
  private String taskId;
  @SerializedName(value = "app")
  private String appId;
  @SerializedName(value = "instruction")
  private String instructionId;
  @SerializedName(value = "custom_iv")
  private String customIvId;
  @SerializedName(value = "reps")
  private int nrOfRepetitions;

  public RawConditionBundle(){
  }

  public String getTaskId() {
    return taskId;
  }

  public String getAppId() {
    return appId;
  }

  public String getInstructionId() {
    return instructionId;
  }

  public String getCustomIvId() {
    return customIvId;
  }

  public int getNrOfRepetitions() {
    return nrOfRepetitions;
  }
}
