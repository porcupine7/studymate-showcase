package at.fhooe.studymate.entities.ivs;

import com.google.gson.annotations.SerializedName;

/**
 * Data entity representing a custom independent variable
 */
public class CustomIv extends IndependentVariable {
  @SerializedName("key")
  private String key;

  @SerializedName("value")
  private String value;

  public CustomIv() {
    //Needed for gson
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
