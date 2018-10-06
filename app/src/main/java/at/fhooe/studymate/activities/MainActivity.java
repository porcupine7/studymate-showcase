package at.fhooe.studymate.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.EventListener;

import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.entities.VideoSettings;
import at.fhooe.studymate.events.NavEvent;
import at.fhooe.studymate.R;
import at.fhooe.studymate.fragments.EnterFragment;
import at.fhooe.studymate.fragments.SetupFragment;
import at.fhooe.studymate.interfaces.IDataManager;
import at.fhooe.studymate.model.Manager;

/**
 * Activity showing the introductory screens and the closing screen
 */
public class MainActivity extends AppCompatActivity implements EventListener, SetupFragment.Callback, EnterFragment.Callback {

    private String qrCodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLifecycle().addObserver(new MainLifeCycle());

        Manager.INSTANCE.init(getApplicationContext());
        qrCodeData = QrCodeActivity.DUMMY_DATA;
        getFragmentManager().beginTransaction().replace(R.id.container, new EnterFragment()).commit();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Start TaskActivity after initial Questionnaire was answered (not used, when there is no questionnaire)
        if (requestCode == QuestionnaireActivity.REQUEST_CODE) {
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void urlSet(String url) {
        qrCodeData = url;
        getFragmentManager().beginTransaction().replace(R.id.container, new SetupFragment()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(NavEvent event) {
        switch (event) {
            case SETUP_DONE:
                EventBus.getDefault().unregister(this);
                openIntroQuestionnaireOrFirstTask();
                break;
        }
    }

    private void openIntroQuestionnaireOrFirstTask() {
        ExperimentInfo experiment = Manager.INSTANCE.getDataProvider(getApplicationContext()).getExperiment();
        notifyAutomateThatExpStarted();
        if (experiment.hasPreExpQuestionnaire()) {
            openQuestionnaireActivity(experiment);
        } else {
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void openQuestionnaireActivity(ExperimentInfo experiment) {
        String questionnaireJson = experiment.getPreExpQuestionnaire().asJson();
        Intent intent = new Intent(MainActivity.this, QuestionnaireActivity.class);
        String participantId = Manager.INSTANCE.getDataProvider(getApplicationContext()).getParticipant().getParticipantId();
        intent.putExtra(QuestionnaireActivity.EXTRA_PARTICIPANT_ID, participantId);
        intent.putExtra(QuestionnaireActivity.EXTRA_JSON_QUESTIONNAIRE, questionnaireJson);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle transitionBundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivityForResult(intent, QuestionnaireActivity.REQUEST_CODE, transitionBundle);
        } else {
            startActivityForResult(intent, QuestionnaireActivity.REQUEST_CODE);
        }
    }

    /**
     * Notify automate with intent to initializes some managers
     */
    private void notifyAutomateThatExpStarted() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        //intent.setAction(StudyMateReceiver.ACTION_START_EXPERIMENT);
        IDataManager dataProvider = Manager.INSTANCE.getDataProvider(getApplicationContext());
        VideoSettings videoSettings = dataProvider.getExperiment().getVideoSettings();
        String videoExtra = videoSettings != null ? videoSettings.asJson() : "";
        //intent.putExtra(StudyMateReceiver.EXTRA_VIDEO_SETTINGS, videoExtra);
        //intent.putExtra(StudyMateReceiver.EXTRA_EXP_ID, dataProvider.getExperiment().getId());
        //intent.putExtra(StudyMateReceiver.EXTRA_BASE_URL, dataProvider.getBaseUrl());
        sendBroadcast(intent);
    }

    @Override
    public String getQrCodeData() {
        return qrCodeData;
    }

}
