package at.fhooe.studymate.model;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import at.fhooe.studymate.interfaces.ILogRecorder;

/**
 * Implementation of ILogRecorder
 */
public class LocalLogRecorder implements ILogRecorder {
  private File loggingFile;
  private BufferedWriter buf;
  private String header;

  public LocalLogRecorder() {
  }

  @Override
  public void init(File file, String header) {
    loggingFile = file;
    this.header = header;
  }

  @Override
  public void saveLogLine(String logLine) {
    //Less performance, when opening, closing all the time, but more reliable!
    openStream();
    if (buf == null) {
      return;
    }
    try {
      // BufferedWriter for performance, true to set append to file flag
      buf.append(logLine);
      buf.newLine();
      buf.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    closeStream();
  }

  private void openStream() {
    try {
      boolean fileWasCreated = loggingFile.createNewFile();
      if (loggingFile==null) {
        Log.e(LocalLogRecorder.class.getSimpleName(), "Logging File is now null!! Did you go into lock screen?");
        return;
      }
      buf = new BufferedWriter(new FileWriter(loggingFile, true));
      if (fileWasCreated) {
        buf.append(header);
        buf.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void closeStream() {
    if (buf != null) {
      try {
        buf.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
