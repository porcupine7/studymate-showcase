package at.fhooe.studymate.entities.blocking;

import java.util.ArrayList;
import java.util.List;

/**
 * Data entity representing a block participants can be assigned to
 */
public class Block {
  private final List<ConditionBundle> orderedConditionBundles;

  public Block() {
    orderedConditionBundles = new ArrayList<>();
  }

  public void addConditionBundle(ConditionBundle conditionBundle) {
    orderedConditionBundles.add(conditionBundle);
  }

  public void addConditionBundleList(List<ConditionBundle> bundles) {
    orderedConditionBundles.addAll(bundles);
  }

  public List<ConditionBundle> getOrderedConditionBundles() {
    return orderedConditionBundles;
  }
}
