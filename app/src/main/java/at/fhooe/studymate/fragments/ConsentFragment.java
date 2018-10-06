package at.fhooe.studymate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.events.NavEvent;
import at.fhooe.studymate.model.Manager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment showing consent message enabling the participant to accept or decline.
 */
public class ConsentFragment extends Fragment {
    @BindView(R.id.txt_message)
    TextView msgTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO check ButterKnife.unbind(this);
        ButterKnife.bind(this, view);
        ExperimentInfo experiment = Manager.INSTANCE.getDataProvider(getActivity()).getExperiment();
        msgTxt.setText(experiment.getConsentMsg());
        msgTxt.setMovementMethod(new ScrollingMovementMethod());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consent, container, false);
    }

    @OnClick(R.id.btn_accept)
    void accepted(View view) {
        EventBus.getDefault().post(NavEvent.CONSENT_ACCEPTED);
    }

    @OnClick(R.id.btn_decline)
    void declined(View view) {
        EventBus.getDefault().post(NavEvent.CONSENT_DECLINED);
    }

}
