package at.fhooe.studymate.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Data entity representing a questionnaire. Is saved as as list in experiment.
 * @see ExperimentInfo
 */
public class Questionnaire {
  @SerializedName("name")
  private String name;

  @SerializedName("id")
  private String id;

  @SerializedName("questions")
  private List<Question> questions;

  public Questionnaire() {
  }

  /**
   * @return of questionnaire
   */
  public String getName() {
    return name;
  }

  /**
   * @return ID of questionnaire(QXX)
   */
  public String getId() {
    return id;
  }

  /**
   * @return list of questions in the questionnaire
   */
  public List<Question> getQuestions() {
    return questions;
  }

  public String asJson() {
    return new Gson().toJson(this);
  }
}
