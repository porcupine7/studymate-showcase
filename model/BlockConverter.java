package at.fhooe.studymate.model;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.studymate.entities.ivs.App;
import at.fhooe.studymate.entities.blocking.Block;
import at.fhooe.studymate.entities.ivs.CustomIv;
import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.entities.ivs.IndependentVariable;
import at.fhooe.studymate.entities.ivs.Instruction;
import at.fhooe.studymate.entities.blocking.RawConditionBundle;
import at.fhooe.studymate.entities.ivs.Task;
import at.fhooe.studymate.entities.blocking.ConditionBundle;

/**
 * Class to convert a list of blocks with ids, array of array of {@link RawConditionBundle}, to
 * blocks with objects, list of {@link Block}, which contains a list of {@link ConditionBundle}
 * Then block at specified index can be accessed.
 */
public class BlockConverter {
  private final List<Block> blocks;

  /**
   * @param experiment          experimental design
   * @param rawConditionBundles conditions with ids
   * @param fieldRep
   */
  public BlockConverter(ExperimentInfo experiment, RawConditionBundle[][] rawConditionBundles, String fieldRep) {
    blocks = new ArrayList<>();
    for (RawConditionBundle[] block : rawConditionBundles) {
      Block curBlock = new Block();
      for (RawConditionBundle rawBundle : block) {
        curBlock.addConditionBundle(convertRawBundle(experiment, rawBundle, fieldRep));
      }
      blocks.add(curBlock);
    }
  }

  private List<ConditionBundle> convertRawBundleWithReps(ExperimentInfo exp, RawConditionBundle rawConditions) {
    List<ConditionBundle> conditionBundles = new ArrayList<>();
    Task task = (Task) IndependentVariable.findIndependentVariable(rawConditions.getTaskId(), exp.getTasks());
    App app = (App) IndependentVariable.findIndependentVariable(rawConditions.getAppId(), exp.getApps());
    CustomIv customIv = (CustomIv) IndependentVariable.findIndependentVariable(rawConditions.getCustomIvId(), exp.getCustomIvs());
    Instruction instruction = (Instruction) IndependentVariable.findIndependentVariable(rawConditions.getInstructionId(), exp.getInstructions());
    int nrOfRepetitions = rawConditions.getNrOfRepetitions();
    for (int rep = 1; rep <= nrOfRepetitions; rep++) {
      String fillerZero = rep < 10 ? "0" : "";
      String repId = "R" + fillerZero + rep;
      conditionBundles.add(new ConditionBundle(task, app, instruction, customIv, repId, nrOfRepetitions));
    }
    return conditionBundles;
  }

  private ConditionBundle convertRawBundle(ExperimentInfo exp, RawConditionBundle rawConditions, String fieldRep) {
    Task task = (Task) IndependentVariable.findIndependentVariable(rawConditions.getTaskId(), exp.getTasks());
    App app = (App) IndependentVariable.findIndependentVariable(rawConditions.getAppId(), exp.getApps());
    CustomIv customIv = (CustomIv) IndependentVariable.findIndependentVariable(rawConditions.getCustomIvId(), exp.getCustomIvs());
    Instruction instruction = (Instruction) IndependentVariable.findIndependentVariable(rawConditions.getInstructionId(), exp.getInstructions());
    return new ConditionBundle(task, app, instruction, customIv, fieldRep, 0);
  }


  /**
   * @param index of block
   * @return block with conditions as objects
   */
  public Block getBlock(int index) {
    return blocks.get(index);
  }

}
