package at.fhooe.studymate.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Data entity representing a question inside a questionnaire
 *
 * @see Questionnaire
 */
public class Question {
  @SerializedName("question")
  private String question;

  @SerializedName("id")
  private String id;

  @SerializedName("type")
  private String type;

  @SerializedName("answers")
  private List<String> answers;

  @SerializedName("likertSteps")
  private int likertSize;

  public Question(){
  }

  /**
   * @return question to answer
   */
  public String getQuestion() {
    return question;
  }

  /**
   * @return question's id(qXX)
   */
  public String getId(){
    return id;
  }

  /**
   * @return type of question defining possible answer[text,radio,checkbox,likert]
   */
  public String getType() {
    return type;
  }

  /**
   * @return options to answer as simple list of strings
   */
  public List<String> getAnswers() {
    return answers;
  }

  /**
   * @return steps of likert scale(only when type is likert)
   */
  public int getLikertSize() {
    return likertSize;
  }
}
