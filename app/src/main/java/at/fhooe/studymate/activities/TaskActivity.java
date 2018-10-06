package at.fhooe.studymate.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.blocking.ConditionBundle;
import at.fhooe.studymate.entities.ivs.CustomIv;
import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.entities.ParticipantInfo;
import at.fhooe.studymate.events.NavEvent;
import at.fhooe.studymate.fragments.TaskFragment;
import at.fhooe.studymate.fragments.ClosingFragment;
import at.fhooe.studymate.interfaces.IDataManager;
import at.fhooe.studymate.model.Manager;
import at.fhooe.studymate.fragments.TaskPresenter;

/**
 * Activity loading task descriptions. In this activity participant can start a task
 */
public class TaskActivity extends AppCompatActivity implements TaskFragment.Callback {
    public static final String KEY_TASK_FINISHED = "task_finished";

    private TaskFragment taskFragment;
    private TaskPresenter taskPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Task");

        EventBus.getDefault().register(this);

        IDataManager dataProvider = Manager.INSTANCE.getDataProvider(getApplicationContext());
        ExperimentInfo exp = dataProvider.getExperiment();
        ParticipantInfo participant = dataProvider.getParticipant();

        taskPresenter = new TaskPresenter(exp, participant, dataProvider.getFieldRep());

        if (savedInstanceState != null) {
            taskPresenter.loadState(savedInstanceState);
        }

        taskFragment = (TaskFragment) getFragmentManager().findFragmentByTag(TaskFragment.class.getSimpleName());
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, taskFragment,
                    TaskFragment.class.getSimpleName()).commit();
            loadNextTaskOrFinish();
        } else {
            taskFragment.loadTask(taskPresenter.getCurTrial(), taskPresenter.isTaskRunning());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        taskPresenter.saveState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean taskFinished = intent.getBooleanExtra(KEY_TASK_FINISHED, false);
        if (taskFinished) {
            notifyAutomateThatTaskEnded(true);
            intent.putExtra(KEY_TASK_FINISHED, false);
            NotificationManager notificationService =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationService.cancel(TaskPresenter.NOTIFICATION_ID);
            taskPresenter.onTaskEnded();
            EventBus.getDefault().post(NavEvent.TASK_FINISHED);
        }
    }

    private void notifyAutomateThatTaskEnded(boolean success) {
        //TODO
        //Intent automateIntent = createNotifyAutomateIntent(StudyMateReceiver.ACTION_END_TASK);
        //automateIntent.putExtra(StudyMateReceiver.EXTRA_SUCCESS, success);
        //sendBroadcast(automateIntent);
    }

    public void loadNextTaskOrFinish() {
        boolean treatmentsAvailable = taskPresenter.loadNextTrial();
        if (treatmentsAvailable) {
            taskFragment.loadTask(taskPresenter.getCurTrial(), taskPresenter.isTaskRunning());
        } else {
            if (taskPresenter.hasPostExpQuestionnaire()) {
                Intent intent = new Intent(TaskActivity.this, QuestionnaireActivity.class);
                String participantId = Manager.INSTANCE.getDataProvider(getApplicationContext()).getParticipant().getParticipantId();
                intent.putExtra(QuestionnaireActivity.EXTRA_PARTICIPANT_ID, participantId);
                intent.putExtra(QuestionnaireActivity.EXTRA_JSON_QUESTIONNAIRE, taskPresenter.getPostExpQuestionnaire().asJson());
                startActivityForResult(intent, QuestionnaireActivity.POST_EXP_REQUEST_CODE);
            } else {
                EventBus.getDefault().post(NavEvent.TRIAL_FINISHED);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QuestionnaireActivity.REQUEST_CODE) {
            loadNextTaskOrFinish();
        }
        if (requestCode == QuestionnaireActivity.POST_EXP_REQUEST_CODE) {
            EventBus.getDefault().post(NavEvent.TRIAL_FINISHED);
        }
    }

    public void onEvent(NavEvent event) {
        switch (event) {
            case TASK_FINISHED:
                ConditionBundle curTreatment = taskPresenter.getCurTrial();
                boolean shouldShowQuest = taskPresenter.hasTaskFollowUpQuestionnaire();
                //Is done to show the questionnaire only once, when there are repetitions!!
                if (shouldShowQuest && curTreatment.hasReps()) {
                    int repIndex = curTreatment.getRepIndex();
                    int totalReps = curTreatment.getTotalReps();
                    if (repIndex < totalReps) shouldShowQuest = false;
                }
                if (shouldShowQuest) {
                    Intent intent = new Intent(TaskActivity.this, QuestionnaireActivity.class);
                    String participantId = Manager.INSTANCE.getDataProvider(getApplicationContext()).getParticipant().getParticipantId();
                    intent.putExtra(QuestionnaireActivity.EXTRA_PARTICIPANT_ID, participantId);
                    intent.putExtra(QuestionnaireActivity.EXTRA_JSON_CONDITIONS, taskPresenter.getCurTrial().asJson());
                    intent.putExtra(QuestionnaireActivity.EXTRA_JSON_QUESTIONNAIRE, taskPresenter.getFollowUpQuestionnaire().asJson());
                    startActivityForResult(intent, QuestionnaireActivity.REQUEST_CODE);
                } else {
                    loadNextTaskOrFinish();
                }
                break;
            case TRIAL_FINISHED:
                Manager.INSTANCE.incrementRepCount(getApplicationContext());
                getFragmentManager().beginTransaction().replace(R.id.container, new ClosingFragment()).commit();
                break;
        }
    }

    @Override
    public void startTask() {
        taskPresenter.taskStarted();

        TaskPresenter.showTaskNotification(getApplicationContext(), taskPresenter.getCurTask());
        String appUnderTest = taskPresenter.getCurApp();
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appUnderTest);
        CustomIv curCustomIv = taskPresenter.getCurTrial().getCustomIv();
        if (curCustomIv != null) {
            launchIntent.putExtra(curCustomIv.getKey(), curCustomIv.getValue());
        }
        startActivity(launchIntent);

        notifyAutomateThatTaskStarted();
    }

    private void notifyAutomateThatTaskStarted() {
        //Intent intent = createNotifyAutomateIntent(StudyMateReceiver.ACTION_START_TASK);
        //sendBroadcast(intent);
    }

    @NonNull
    private Intent createNotifyAutomateIntent(String action) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(action);
        String participantId =
                Manager.INSTANCE.getDataProvider(getApplicationContext()).getParticipant().getParticipantId();
        //intent.putExtra(StudyMateReceiver.EXTRA_PARTICIPANT_ID, participantId);
        String treatment = taskPresenter.getCurTrial().asJson();
        //intent.putExtra(StudyMateReceiver.EXTRA_TRIAL, treatment);
        return intent;
    }

    @Override
    public void concedeTask() {
        NotificationManager notificationService =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationService.cancel(TaskPresenter.NOTIFICATION_ID);
        notifyAutomateThatTaskEnded(false);

        taskPresenter.onTaskEnded();
        loadNextTaskOrFinish();
    }
}
