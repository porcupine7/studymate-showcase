package at.fhooe.studymate.interfaces;

import java.io.File;

/**
 * Persist logging data(csv)
 */
public interface ILogRecorder {

  /**
   * Init csv file with correct header
   *
   * @param file where data is written
   * @param header csv header
   */
  void init(File file, String header);

  /**
   * Append the line to the file. Line has to conform to the header, there is no extra check.
   *
   * @param logLine csv line representing a record
   */
  void saveLogLine(String logLine);

}
