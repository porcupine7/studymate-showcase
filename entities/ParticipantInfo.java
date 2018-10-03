package at.fhooe.studymate.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Data entity containing information about a participant(id, block)
 */
public class ParticipantInfo {

  @SerializedName("participantId")
  private String participantId;

  @SerializedName("block")
  private int blockId;

  public ParticipantInfo() {
  }

  public ParticipantInfo(String participantId, int blockId) {
    this.participantId = participantId;
    this.blockId = blockId;
  }

  /**
   * @return unique participant id(PXX)
   */
  public String getParticipantId() {
    return participantId;
  }

  /**
   * @return block participant is assigned to(0-??)
   */
  public int getBlockIndex() {
    return blockId;
  }

  public String asJson() {
    return new Gson().toJson(this);
  }
}
