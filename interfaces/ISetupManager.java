package at.fhooe.studymate.interfaces;

import at.fhooe.studymate.events.SetupEvent;

/**
 * Manages the setup process
 */
public interface ISetupManager {
  /**
   * Extract experimental id and URL from String on QR code
   *
   * @param qrCodeData String encoded in QR code
   */
  void parseQrCode(String qrCodeData);

  /**
   * Do the next step in the setup process(could be loadExperiment, loadParticipant etc)
   */
  void loadNext();

  /**
   * Load experimental design from parsed URL
   */
  void loadExperiment();

  /**
   * Load information about participant
   */
  void loadParticipant();

  /**
   * Check if apps are installed on device
   */
  void checkForApps();

  /**
   * Check if the StudyMate accessibility service is enabled
   */
  void checkForAccServices();

  /**
   * Add performed setupEvent to an internal list
   *
   * @param setupEvent performed setupEvent
   */
  void addSetupEvent(SetupEvent setupEvent);


  /**
   * Checks if the performed setup events match the required events
   *
   * @return whether all required setup events were performed
   */
  boolean isSetupDone();

}
