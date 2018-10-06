package at.fhooe.studymate.entities.blocking;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import at.fhooe.studymate.entities.ivs.App;
import at.fhooe.studymate.entities.ivs.CustomIv;
import at.fhooe.studymate.entities.ivs.Instruction;
import at.fhooe.studymate.entities.ivs.Task;

/**
 * Condition bundle with objects as members. Converted from {@link RawConditionBundle}
 */
public class ConditionBundle {
  private App app;
  private Task task;
  private Instruction instruction;
  private CustomIv customIv;
  private String repetition;
  private int totalReps;

  public ConditionBundle(@NonNull Task task, @NonNull App app, @Nullable Instruction instruction,
                         @Nullable CustomIv customIv, @Nullable String repetition, int totalReps) {
    this.app = app;
    this.task = task;
    this.instruction = instruction;
    this.customIv = customIv;
    this.repetition = repetition;
    this.totalReps = totalReps;
  }

  public App getApp() {
    return app;
  }

  public Task getTask() {
    return task;
  }

  @Nullable
  public Instruction getInstruction() {
    return instruction;
  }

  @Nullable
  public CustomIv getCustomIv() {
    return customIv;
  }

  /**
   * @return as String starting with R (the id)
   */
  public String getRepetition() {
    return repetition;
  }

  /**
   * @return total number of repetitions, Beware: 0 means participant does it once and 2 means
   * he does it twice(sorry for this weirdness)
   */
  public int getTotalReps() {
    return totalReps;
  }

  /**
   * @return the index of the repetition (without R). -1 when there are no repetitions
   */
  public int getRepIndex() {
    if (hasReps()) {
      return Integer.parseInt(repetition.substring(1));
    }
    return -1;
  }

  /**
   * @return true when repetitions are enabled, false when there are none
   */
  public boolean hasReps() {
    return repetition != null && !repetition.isEmpty();
  }

  /**
   * @return instruction replacing placeholders with current conditions
   */
  public String getFormattedInstruction() {
    if (instruction == null) return "";
    String rawInstruction = instruction.getInstruction();
    String formattedWithApps = rawInstruction.replaceAll("\\$\\{app\\}", getApp().getName());
    String formattedWithRep = formattedWithApps.replaceAll("\\$\\{rep\\}", repetition);
    String curCustom = getCustomIv() != null ? getCustomIv().getName() : "??";
    String formattedWithCustom = formattedWithRep.replaceAll("\\$\\{custom\\}", curCustom);
    return formattedWithCustom;
  }


  public String asJson() {
    return new Gson().toJson(this);
  }

  public static ConditionBundle fromJson(String json) {
    return new Gson().fromJson(json, ConditionBundle.class);
  }

  /**
   * @return simple String representation(used in filename)
   */
  public String asSimpleString() {
    StringBuilder builder = new StringBuilder();
    builder.append(task.getId());
    builder.append("_");
    builder.append(app.getId());
    if (instruction != null) {
      builder.append("_");
      builder.append(instruction.getId());
    }
    if (customIv != null) {
      builder.append("_");
      builder.append(customIv.getId());
    }
    if (hasReps()) {
      builder.append("_");
      builder.append(repetition);
    }
    return builder.toString();
  }

  /**
   * Create a part of the log line, shown between timestamp and participant and the measures
   *
   * @param delimiter separation character between ids
   * @return String where ids are separated
   */
  public String asLogLinePart(String delimiter) {
    StringBuilder builder = new StringBuilder();
    builder.append(task.getId());
    builder.append(delimiter);
    builder.append(app.getId());
    builder.append(delimiter);
    if (instruction != null) {
      builder.append(instruction.getId());
    }
    builder.append(delimiter);
    if (customIv != null) {
      builder.append(customIv.getId());
    }
    builder.append(delimiter);
    if (hasReps()) {
      builder.append(repetition);
    }
    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ConditionBundle conditionBundle = (ConditionBundle) o;

    return asSimpleString().equals(conditionBundle.asSimpleString());
  }

  @Override
  public int hashCode() {
    return asSimpleString().hashCode();
  }
}

