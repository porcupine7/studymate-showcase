package at.fhooe.studymate.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.entities.ParticipantInfo;
import at.fhooe.studymate.interfaces.IDataManager;

/**
 * Implementation of IDataManager
 */
public class DataManager implements IDataManager {
  public static final String KEY_PARTICIPANT = "participant";
  public static final String KEY_EXPERIMENT = "experiment";
  public static final String KEY_BASE_URL = "base_url";
  private static final String KEY_FIELD_REP = "field_rep";

  private final SharedPreferences pref;

  private ExperimentInfo experiment;
  private ParticipantInfo participant;

  private String fieldRep;

  private String baseUrl;

  public DataManager(Context context) {
    pref = PreferenceManager.getDefaultSharedPreferences(context);
    experiment = tryToLoadExperiment();
  }

  @Override
  public void cacheExperiment(String experimentJson) {
    pref.edit().putString(KEY_EXPERIMENT, experimentJson).apply();
    experiment = new Gson().fromJson(experimentJson, ExperimentInfo.class);
  }

  @Override
  public void cacheParticipant(ParticipantInfo participantInfo) {
    pref.edit().putString(KEY_PARTICIPANT, participantInfo.asJson()).apply();
    participant = participantInfo;
  }

  @Override
  public void cacheBaseUrl(String baseUrl) {
    pref.edit().putString(KEY_PARTICIPANT, baseUrl).apply();
    this.baseUrl = baseUrl;
  }

  @Override
  public void cacheFieldRep(String fieldRep) {
    pref.edit().putString(KEY_FIELD_REP, fieldRep).apply();
    this.fieldRep = fieldRep;
  }

  @Override
  public String getFieldRep() {
    if (fieldRep == null || fieldRep.isEmpty()) {
      this.fieldRep = pref.getString(KEY_FIELD_REP, "");
    }
    return fieldRep;
  }

  private ExperimentInfo tryToLoadExperiment() {
    String json = pref.getString(KEY_EXPERIMENT, "");
    return new Gson().fromJson(json, ExperimentInfo.class);
  }

  private ParticipantInfo tryToLoadParticipant() {
    String participantJson = pref.getString(KEY_PARTICIPANT, "");
    if (!participantJson.isEmpty()) {
      return new Gson().fromJson(participantJson, ParticipantInfo.class);
    }
    return null;
  }

  private String tryToLoadBaseUrl() {
    return pref.getString(KEY_BASE_URL, "");
  }

  @Override
  public ExperimentInfo getExperiment() {
    if (experiment == null) {
      experiment = tryToLoadExperiment();
    }
    return experiment;
  }

  @Override
  public ParticipantInfo getParticipant() {
    if (participant == null) {
      this.participant = tryToLoadParticipant();
    }
    return participant;
  }

  @Override
  public String getBaseUrl() {
    if (baseUrl == null || baseUrl.isEmpty()) {
      baseUrl = tryToLoadBaseUrl();
    }
    return baseUrl;
  }
}
