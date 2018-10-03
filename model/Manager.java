package at.fhooe.studymate.model;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;

import at.fhooe.studymate.interfaces.IDataManager;

/**
 * Singleton to access common model objects like experimental design from anywhere
 */
public enum Manager {
  INSTANCE;

  private Context context;
  private IDataManager dataManager;

  /**
   * Called in the beginning, and when the Manager object is destroyed somehow, called for
   * reinitialization
   *
   * @param context needed to access application specific stuff, like internal storage
   */
  public void init(Context context) {
    this.context = context;
    dataManager = new DataManager(context);
  }

  public void incrementRepCount(Context context) {
    IDataManager dataProvider = getDataProvider(context);
    String fieldRep = dataProvider.getFieldRep();
    int index = Integer.valueOf(fieldRep.substring(1));
    index++;
    String fillerZero = index < 10 ? "0" : "";
    String newRep = "R" + fillerZero + index;
    dataProvider.cacheFieldRep(newRep);
  }

  /**
   * @param contextForInit for reinitializing singleton, when it was destroyed somehow, e.g. with GC
   * @return object to manage data
   */
  public IDataManager getDataProvider(@NonNull Context contextForInit) {
    if (this.context == null) {
      init(contextForInit);
    }
    return dataManager;
  }
}
