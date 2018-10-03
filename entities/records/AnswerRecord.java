package at.fhooe.studymate.entities.records;

import com.google.gson.annotations.SerializedName;

import at.fhooe.studymate.entities.blocking.ConditionBundle;

/**
 * Data entity representing the answer to one question by one participant.
 * Send to server this way
 */
public class AnswerRecord extends Record {
  @SerializedName("questionnaire")
  private String questionnaire;

  @SerializedName("question")
  private String question;

  @SerializedName("answer")
  private String answer;

  public AnswerRecord(long timestamp, String participant, ConditionBundle conditions, String questionnaire,
                      String question, String answer) {
    //Sorry for this long super thing...
    super(timestamp, participant, conditions.getApp().getId(), conditions.getTask().getId(),
        conditions.getInstruction() != null ? conditions.getInstruction().getId() : null,
        conditions.getCustomIv() != null ? conditions.getCustomIv().getId() : null,
        conditions.getRepetition() != null && !conditions.getRepetition().isEmpty() ?
            conditions.getRepetition() : null);
    this.questionnaire = questionnaire;
    this.question = question;
    this.answer = answer;
  }

  public AnswerRecord(long timestamp, String participant, String questionnaire, String question, String answer) {
    super(timestamp, participant, null, null, null, null, null);
    this.questionnaire = questionnaire;
    this.question = question;
    this.answer = answer;
  }

  public String getQuestionnaire() {
    return questionnaire;
  }

  public String getQuestion() {
    return question;
  }

  public String getAnswer() {
    return answer;
  }

  public String asLogLine(String delimiter) {
    StringBuilder logLine = new StringBuilder();
    logLine.append(getTimestamp());
    logLine.append(delimiter);
    logLine.append(getParticipant());
    logLine.append(delimiter);
    if (getTask() != null) logLine.append(getTask());
    logLine.append(delimiter);
    if (getApp() != null) logLine.append(getApp());
    logLine.append(delimiter);
    if (getInstruction() != null) logLine.append(getInstruction());
    logLine.append(delimiter);
    if (getCustomIv() != null) logLine.append(getCustomIv());
    logLine.append(delimiter);
    if (getRep() != null && !getRep().isEmpty()) logLine.append(getRep());
    logLine.append(delimiter);
    logLine.append(getQuestionnaire());
    logLine.append(delimiter);
    logLine.append(getQuestion());
    logLine.append(delimiter);
    logLine.append(getAnswer());
    return logLine.toString();
  }
}
