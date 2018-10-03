package at.fhooe.studymate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.events.NavEvent;
import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.model.Manager;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Fragment showing a welcome message
 */
public class WelcomeFragment extends Fragment {
  @Bind(R.id.txt_message)
  TextView msgTxt;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
    ButterKnife.bind(this, rootView);
    ExperimentInfo experiment = Manager.INSTANCE.getDataProvider(getActivity()).getExperiment();
    msgTxt.setText(experiment.getWelcomeMsg());
    msgTxt.setMovementMethod(new ScrollingMovementMethod());
    return rootView;
  }

  @OnClick(R.id.btn_ok)
  void ok() {
    EventBus.getDefault().post(NavEvent.WELCOME_OK);
  }

}
