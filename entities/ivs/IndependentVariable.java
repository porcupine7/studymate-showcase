package at.fhooe.studymate.entities.ivs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Abstract class with members general to all independent variables(id, name)
 */
public abstract class IndependentVariable {
  @SerializedName("id")
  private String id;

  @SerializedName("name")
  private String name;


  public IndependentVariable(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public IndependentVariable() {
    //Needed for gson
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  /**
   * Find variables by id
   *
   * @param id of iv
   * @param ivs list of ivs
   * @return the Independent Variable as object
   */
  public static IndependentVariable findIndependentVariable(String id, List<? extends IndependentVariable> ivs) {
    for (IndependentVariable iv : ivs) {
      if (id.equals(iv.getId())) return iv;
    }
    return null;
  }
}
