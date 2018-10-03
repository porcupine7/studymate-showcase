package at.fhooe.studymate.entities.records;

import com.google.gson.annotations.SerializedName;

import at.fhooe.studymate.entities.blocking.ConditionBundle;

/**
 * Data entity presents the quantitative measures of participants' performance
 * This class is used to send the measures as json to server
 */
public class DvRecord extends Record {
  @SerializedName("success")
  private int success;

  @SerializedName("time")
  private long time;

  @SerializedName("errors")
  private int errors;

  private DvRecord(long timestamp, String participantId, String taskId, String appId, String instructionId,
                   String customIvId, String repId, int success, long time, int errors) {
    super(timestamp, participantId, taskId, appId, instructionId, customIvId, repId);
    this.success = success;
    this.time = time;
    this.errors = errors;
  }

  public DvRecord() {
  }

  public static DvRecord createDvRecord(long timestamp, String participantId,
                                        ConditionBundle conditionBundle, int success, long timeOnTask, int errors) {
    String taskId = conditionBundle.getTask() != null ? conditionBundle.getTask().getId() : null;
    String appId = conditionBundle.getApp() != null ? conditionBundle.getApp().getId() : null;
    String instructionId = conditionBundle.getInstruction() != null ? conditionBundle.getInstruction().getId() : null;
    String customIvId = conditionBundle.getCustomIv() != null ? conditionBundle.getCustomIv().getId() : null;
    boolean hasReps = conditionBundle.getRepetition() != null && !conditionBundle.getRepetition().isEmpty();
    //When there is an empty rep, also null is set, so that its not sent to server
    String repId = hasReps ? conditionBundle.getRepetition() : null;
    return new DvRecord(timestamp, participantId, taskId, appId, instructionId, customIvId, repId, success, timeOnTask, errors);
  }
}
