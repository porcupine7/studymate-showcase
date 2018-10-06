package at.fhooe.studymate.activities;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.journeyapps.barcodescanner.CaptureActivity;

import at.fhooe.studymate.R;

/**
 * Activity showing a camera screen to scan a QR code
 */
public class QrCodeActivity extends CaptureActivity {

  public static final String DUMMY_DATA = "assets/experiment.json?par=P00&bl=0";
  public static final String DUMMY_KEY = "dummy_data";

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.qr_code_menu, menu);
    return true;
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    if (item.getItemId() == R.id.qr_skip_item) {
      //just for debug
      //TODO doesnt even work anymore
      Intent resultIntent = new Intent();
      resultIntent.putExtra(DUMMY_KEY, DUMMY_DATA);
      setResult(Activity.RESULT_OK, resultIntent);
      finish();
      return true;
    }
    return super.onMenuItemSelected(featureId, item);
  }
}
