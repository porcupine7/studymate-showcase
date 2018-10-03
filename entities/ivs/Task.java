package at.fhooe.studymate.entities.ivs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Task, WHAT the participant has to do. Can have a followup questionnaire
 */
public class Task extends IndependentVariable {
  public static final int SHORT_SIZE = 50;

  @SerializedName("description")
  private String description;

  @SerializedName("followUpQuestionnaire")
  private String followUpQuestionnaireId;

  public Task() {
  }

  public String getDescription() {
    return description;
  }

  /**
   * @return short task summary for notification
   */
  public String getShortDescription() {
    return description.length() < SHORT_SIZE ? description : description.substring(0, SHORT_SIZE);
  }

  public String getFollowUpQuestionnaireId() {
    return followUpQuestionnaireId;
  }

}
