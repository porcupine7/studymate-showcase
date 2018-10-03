package at.fhooe.studymate.entities.ivs;

import com.google.gson.annotations.SerializedName;

import at.fhooe.studymate.entities.ivs.IndependentVariable;

/**
 * Instruction, how to do the task, e.g. while walking. Is shown below task description.
 */
public class Instruction extends IndependentVariable {
  @SerializedName("instruction")
  private String instruction;

  public Instruction() {
    //Needed for gson
  }

  public String getInstruction() {
    return instruction;
  }
}

