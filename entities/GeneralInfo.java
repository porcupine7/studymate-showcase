package at.fhooe.studymate.entities;

import com.google.gson.annotations.SerializedName;

/**
 * General information about the experiment(name, introductive screens, questionnaire ids before and
 * after experiment)
 * @see ExperimentInfo
 */
public class GeneralInfo {
  @SerializedName("name")
  private String name;

  @SerializedName("consentMsg")
  private String consentMsg;

  @SerializedName("welcomeMsg")
  private String welcomeMsg;

  @SerializedName("closingMsg")
  private String closingMsg;

  @SerializedName("preExpQuestionnaire")
  private String preExpQuestionnaireId;

  @SerializedName("postExpQuestionnaire")
  private String postExpQuestionnaireId;

  public GeneralInfo(){
  }

  public String getName() {
    return name;
  }

  public String getConsentMsg() {
    return consentMsg;
  }

  public String getWelcomeMsg() {
    return welcomeMsg;
  }

  public String getClosingMsg() {
    return closingMsg;
  }

  public String getPreExpQuestionnaireId() {
    return preExpQuestionnaireId;
  }

  public String getPostExpQuestionnaireId() {
    return postExpQuestionnaireId;
  }
}
