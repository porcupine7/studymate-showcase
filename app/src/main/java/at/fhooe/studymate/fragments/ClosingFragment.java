package at.fhooe.studymate.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.activities.MainActivity;
import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.model.Manager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment showing the closing message.
 */
public class ClosingFragment extends Fragment {
    @BindView(R.id.btn_ok)
    Button okBtn;
    @BindView(R.id.txt_message)
    TextView msgTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        ButterKnife.bind(this, rootView);
        ExperimentInfo experiment = Manager.INSTANCE.getDataProvider(getActivity()).getExperiment();
        msgTxt.setText(experiment.getClosingMsg());
        msgTxt.setMovementMethod(new ScrollingMovementMethod());
        okBtn.setText("Next");
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return rootView;
    }

}
