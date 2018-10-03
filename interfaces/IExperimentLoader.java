package at.fhooe.studymate.interfaces;

/**
 * Interface to load experiment from remote or local location
 */
public interface IExperimentLoader {

  /**
   * Load experimental design on URI, locally as well as remotely
   *
   * @param uri of experimental design(can start with assets:// or http://)
   */
  void requestExperiment(String uri);
}
