package at.fhooe.studymate.events;

/**
 * Events emitted during the setup process. Come always in pairs, either a setup step
 * failed or was done.
 */
public enum SetupEvent {
  QR_CODE_DONE, QR_CODE_FAIL,
  APP_DONE, APP_FAIL,
  ACCESSIBILITY_DONE, ACCESSIBILITY_FAIL,
  EXPERIMENT_DONE, EXPERIMENT_FAIL,
  PARTICIPANT_DONE, PARTICIPANT_FAIL
}
