package at.fhooe.studymate.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import at.fhooe.studymate.entities.blocking.RawConditionBundle;
import at.fhooe.studymate.entities.ivs.App;
import at.fhooe.studymate.entities.ivs.CustomIv;
import at.fhooe.studymate.entities.ivs.Instruction;
import at.fhooe.studymate.entities.ivs.Task;

/**
 * Data entity representing the entirety of an experiment, directly parsed from the received JSON
 */
public class ExperimentInfo {

  @SerializedName("id")
  private String id;

  @SerializedName("general")
  private GeneralInfo general;

  @SerializedName("apps")
  private List<App> apps;

  @SerializedName("tasks")
  private List<Task> tasks;

  @SerializedName("custom_ivs")
  private List<CustomIv> customIvs;

  @SerializedName("instructions")
  private List<Instruction> instructions;

  @SerializedName("video")
  private VideoSettings videoSettings;

  @SerializedName("blocks")
  private RawConditionBundle[][] blocks;

  @SerializedName("questionnaires")
  private List<Questionnaire> questionnaires;

  public ExperimentInfo() {
    //to prevent null pointer exception
    general = new GeneralInfo();
    videoSettings = new VideoSettings();
  }

  public String getName() {
    return general.getName();
  }

  public String getConsentMsg() {
    return general.getConsentMsg();
  }

  public String getWelcomeMsg() {
    return general.getWelcomeMsg();
  }

  public String getClosingMsg() {
    return general.getClosingMsg();
  }

  @NonNull
  public List<Task> getTasks() {
    return tasks;
  }

  @NonNull
  public List<App> getApps() {
    return apps;
  }

  @NonNull
  public List<Instruction> getInstructions() {
    return instructions;
  }

  @NonNull
  public List<CustomIv> getCustomIvs() {
    return customIvs;
  }

  public String getId() {
    return id;
  }

  public RawConditionBundle[][] getBlocks() {
    return blocks;
  }

  @Nullable
  public List<Questionnaire> getQuestionnaires() {
    return questionnaires;
  }

  @Nullable
  public Questionnaire findQuestionnaireById(String id) {
    if (getQuestionnaires() == null) return null;
    for (Questionnaire questionnaire : getQuestionnaires()) {
      if (questionnaire.getId().equals(id)) return questionnaire;
    }
    return null;
  }

  @Nullable
  public Questionnaire getPreExpQuestionnaire() {
    return findQuestionnaireById(general.getPreExpQuestionnaireId());
  }

  public boolean hasPreExpQuestionnaire() {
    return general.getPreExpQuestionnaireId() != null && !general.getPreExpQuestionnaireId().isEmpty();
  }

  @Nullable
  public Questionnaire getPostExpQuestionnaire() {
    return findQuestionnaireById(general.getPostExpQuestionnaireId());
  }

  @Nullable
  public VideoSettings getVideoSettings() {
    return videoSettings;
  }

  public String asJson() {
    return new Gson().toJson(this);
  }
}
