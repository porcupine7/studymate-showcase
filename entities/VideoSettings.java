package at.fhooe.studymate.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Settings of video(enabled, microphone used, quality levels)
 * @see ExperimentInfo
 */
public class VideoSettings {
  @SerializedName("enabled")
  private boolean enabled;

  @SerializedName("mic")
  private boolean useMic;

  @SerializedName("quality")
  private String quality;

  public VideoSettings() {
  }

  public boolean isEnabled() {
    return enabled;
  }

  public boolean usesMic() {
    return useMic;
  }

  public String getQuality() {
    return quality;
  }

  public int getBitrate() {
    switch (getQuality()) {
      case "low":
        return 512 * 1000;
      case "medium":
        return 768 * 1000;
      case "high":
        return 1024 * 1000;
      default:
        return 512*1000;
    }
  }

  public int getFrameRate(){
    return getQuality().equals("high") ? 60 : 30;
  }

  public String asJson() {
    return new Gson().toJson(this);
  }

  public static VideoSettings fromJson(String json) {
    return new Gson().fromJson(json, VideoSettings.class);
  }
}
