package at.fhooe.studymate.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.ParticipantInfo;
import at.fhooe.studymate.interfaces.IDataManager;
import at.fhooe.studymate.model.Manager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Just for field studies:  Fragment to enter participant ID, shows cur rep
 */
public class EnterFragment extends Fragment {

    public static final String EXP_URL = "assets/experiment.json?par=%1$s&bl=0";

    public static final String DEFAULT_PARTICIPANT_ID = "P01";
    public static final String FIRST_REP = "R01";

    @BindView(R.id.edit_par)
    EditText editPar;

    @BindView(R.id.txt_cur_rep)
    TextView txtCurRep;

    private Callback callback;

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
        View rootView = inflater.inflate(R.layout.fragment_enter, container, false);
        ButterKnife.bind(this, rootView);
        IDataManager dataProvider = Manager.INSTANCE.getDataProvider(getActivity());
        ParticipantInfo oldPar = dataProvider.getParticipant();
        editPar.setText(DEFAULT_PARTICIPANT_ID);
        if (oldPar != null) {
            editPar.setText(oldPar.getParticipantId());
        }
        if (dataProvider.getFieldRep() == null || dataProvider.getFieldRep().isEmpty()) {
            dataProvider.cacheFieldRep(FIRST_REP);
        }
        txtCurRep.setText(dataProvider.getFieldRep());
        return rootView;
    }

    @OnClick(R.id.btn_ok)
    public void okClicked(View view) {
        String pid = editPar.getText().toString();
        if (!pid.isEmpty()) {
            String url = String.format(EXP_URL, pid);
            callback.urlSet(url);
        }
    }

    public interface Callback {
        void urlSet(String url);
    }
}
