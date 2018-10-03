package at.fhooe.studymate.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.activities.MainActivity;
import at.fhooe.studymate.events.NavEvent;
import at.fhooe.studymate.events.SetupEvent;
import at.fhooe.studymate.interfaces.ISetupManager;
import at.fhooe.studymate.model.SetupManager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Fragment showing the status of the setup process
 */
public class SetupFragment extends Fragment {
  @Bind(R.id.txt_qrcode)
  TextView qrcodeTxt;
  @Bind(R.id.txt_participant)
  TextView participantTxt;
  @Bind(R.id.txt_experiment)
  TextView experimentTxt;
  @Bind(R.id.txt_app)
  TextView appTxt;
  @Bind(R.id.txt_accessibility)
  TextView accessibilityTxt;

  @Bind(R.id.container_setup_hint)
  ViewGroup hintContainer;

  @Bind(R.id.txt_hint)
  TextView hintTxt;

  @Bind(R.id.btn_hint)
  Button hintBtn;

  @Bind(R.id.btn_retry)
  Button retryBtn;

  private Callback callback;
  private ISetupManager setupManager;


  @SuppressWarnings("deprecation")
  @Override
  public void onAttach(Activity activity) {
    //New onAttach is not called on older devices. Oh Android Boy!
    super.onAttach(activity);
    if (activity instanceof Callback) {
      callback = (Callback) activity;
    } else {
      throw new IllegalStateException("Activity not allowed to use SetupFragment: Implement Callback");
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
    ButterKnife.bind(this, rootView);
    EventBus.getDefault().register(this);
    setupManager = new SetupManager(getActivity());
    //When initiated immediately parse loaded QR code and start setup process
    setupManager.parseQrCode(callback.getQrCodeData());
    return rootView;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  /**
   * What the hint does, depends on the error in the setup process. E.g. when StudyMate Accessibility
   * service is not activated, the settings are shown and it can be activated
   *
   * @param view the clicked button.
   */
  @OnClick(R.id.btn_hint)
  public void hintClicked(View view) {
    if (SetupEvent.QR_CODE_FAIL.equals(view.getTag())) {
      getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
      getActivity().finish();
    }else if(SetupEvent.EXPERIMENT_FAIL.equals(view.getTag())) {
      Intent settingsIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.settings");
      getActivity().startActivity(settingsIntent);
    }else if(SetupEvent.APP_FAIL.equals(view.getTag())) {
      Intent storeIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.vending");
      getActivity().startActivity(storeIntent);
    }else if(SetupEvent.ACCESSIBILITY_FAIL.equals(view.getTag())) {
      Intent settingsIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.settings");
      getActivity().startActivity(settingsIntent);
    }
  }

  @OnClick(R.id.btn_retry)
  public void retryClicked(View view) {
    hintContainer.setVisibility(View.INVISIBLE);
    setupManager.loadNext();
  }

  /**
   * Receives Setup events and acts accordingly
   * @param setupEvent sent by {@link SetupManager} via EventBus
   */
  public void onEventMainThread(@NonNull SetupEvent setupEvent) {
    setupManager.addSetupEvent(setupEvent);

    if (setupEvent.equals(SetupEvent.QR_CODE_DONE)) {
      elementDone(qrcodeTxt, R.string.setup_qrcode_done);
      experimentTxt.setVisibility(View.VISIBLE);
      setupManager.loadExperiment();
    } else if (setupEvent.equals(SetupEvent.QR_CODE_FAIL)) {
      qrcodeTxt.setText(R.string.setup_qrcode_fail);
      hintContainer.setVisibility(View.VISIBLE);
      hintTxt.setText(R.string.setup_qrcode_hint_txt);
      hintBtn.setText(R.string.setup_qrcode_hint_btn);
      hintBtn.setTag(SetupEvent.QR_CODE_FAIL);
      retryBtn.setEnabled(false);
    }

    if (setupEvent.equals(SetupEvent.EXPERIMENT_DONE)) {
      elementDone(experimentTxt, R.string.setup_experiment_done);
      participantTxt.setVisibility(View.VISIBLE);
      setupManager.loadParticipant();
    } else if (setupEvent.equals(SetupEvent.EXPERIMENT_FAIL)) {
      experimentTxt.setText(R.string.setup_experiment_fail);
      hintContainer.setVisibility(View.VISIBLE);
      hintTxt.setText(R.string.setup_experiment_hint_txt);
      hintBtn.setText(R.string.setup_experiment_hint_btn);
      hintBtn.setTag(SetupEvent.EXPERIMENT_FAIL);
    }

    if (setupEvent.equals(SetupEvent.PARTICIPANT_DONE)) {
      elementDone(participantTxt, R.string.setup_participant_done);
      appTxt.setVisibility(View.VISIBLE);
      setupManager.checkForApps();
    } else if (setupEvent.equals(SetupEvent.PARTICIPANT_FAIL)) {
      participantTxt.setText(R.string.setup_participant_fail);
    }

    if (setupEvent.equals(SetupEvent.APP_DONE)) {
      elementDone(appTxt, R.string.setup_app_done);
      accessibilityTxt.setVisibility(View.VISIBLE);
      setupManager.checkForAccServices();
    } else if (setupEvent.equals(SetupEvent.APP_FAIL)) {
      appTxt.setText(R.string.setup_app_fail);
      hintContainer.setVisibility(View.VISIBLE);
      hintTxt.setText(R.string.setup_app_hint_txt);
      hintBtn.setText(R.string.setup_app_hint_btn);
      hintBtn.setTag(SetupEvent.APP_FAIL);
    }

    if (setupEvent.equals(SetupEvent.ACCESSIBILITY_DONE)) {
      elementDone(accessibilityTxt, R.string.setup_accessibility_done);
    } else if (setupEvent.equals(SetupEvent.ACCESSIBILITY_FAIL)) {
      accessibilityTxt.setText(R.string.setup_accessibility_fail);
      hintContainer.setVisibility(View.VISIBLE);
      hintTxt.setText(R.string.setup_accessibility_hint_txt);
      hintBtn.setText(R.string.setup_accessibility_hint_btn);
      hintBtn.setTag(SetupEvent.ACCESSIBILITY_FAIL);
    }

    if (setupManager.isSetupDone()) {
      //When all required setup steps are done, main activity is notified
      EventBus.getDefault().post(NavEvent.SETUP_DONE);
    }
  }

  /**
   * Change appearance of setup element after setup step is finished successfully
   *
   * @param txt view which changes
   * @param stringRes Text shown if setup step is done
   */
  private void elementDone(TextView txt, int stringRes) {
    txt.setText(stringRes);
    txt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
  }

  public interface Callback {
    /**
     * @return contents of qr code, provided by Activity
     */
    String getQrCodeData();
  }
}
