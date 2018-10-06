package at.fhooe.studymate.fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import at.fhooe.studymate.R;
import at.fhooe.studymate.activities.TaskActivity;
import at.fhooe.studymate.entities.blocking.Block;
import at.fhooe.studymate.entities.ExperimentInfo;
import at.fhooe.studymate.entities.ParticipantInfo;
import at.fhooe.studymate.entities.Questionnaire;
import at.fhooe.studymate.entities.blocking.RawConditionBundle;
import at.fhooe.studymate.entities.ivs.Task;
import at.fhooe.studymate.entities.blocking.ConditionBundle;
import at.fhooe.studymate.model.BlockConverter;

/**
 * Class to act upon TaskFragment and model classes
 */
public class TaskPresenter {
  private final ExperimentInfo experiment;
  /**
   * The block defines which conditions participant get in which order
   */
  private final Block block;
  /**
   * Index is counted with the progression of trials, increment by one after one trial is done
   */
  private int trialIndex;
  /**
   * Whether participant currently performs a task, otherwise he reads the task description
   */
  private boolean taskIsRunning;

  /**
   * Constructor to create the task presenter
   * @param experiment received from server
   * @param participantInfo ID and block of participant
   * @param fieldRep
   */
  public TaskPresenter(ExperimentInfo experiment, ParticipantInfo participantInfo, String fieldRep) {
    this.experiment = experiment;
    RawConditionBundle[][] rawConditionBundles = experiment.getBlocks();
    BlockConverter blockConverter = new BlockConverter(experiment, rawConditionBundles, fieldRep);
    int blockIndex = participantInfo.getBlockIndex();
    block = blockConverter.getBlock(blockIndex);

    trialIndex = -1;
    taskIsRunning = false;
  }

  /**
   * Load old state, so that fragment can look the same after app went to background
   *
   * @param savedState Contains saved index and whether task was running
   */
  public void loadState(Bundle savedState) {
    trialIndex = savedState.getInt("trialIndex");
    taskIsRunning = savedState.getBoolean("taskRunning");
  }

  /**
   * Save trial index and whether task is running
   *
   * @param state Android's Bundle
   */
  public void saveState(Bundle state) {
    state.putInt("trialIndex", trialIndex);
    state.putBoolean("taskRunning", taskIsRunning);
  }

  /**
   * @return package name of current app in trial
   */
  public String getCurApp() {
    return getCurTrial().getApp().getPackageName();
  }

  /**
   * @return true, when another trial is available. When false experiment is finished
   */
  public boolean loadNextTrial() {
    trialIndex++;
    taskIsRunning = false;
    boolean trialAvailable =
        trialIndex < block.getOrderedConditionBundles().size();
    return trialAvailable;
  }

  /**
   * Called when task is started
   */
  public void taskStarted() {
    taskIsRunning = true;
  }

  /**
   * Called when task was finished or conceded
   */
  public void onTaskEnded() {
    taskIsRunning = false;
  }

  public Task getCurTask() {
    return getCurTrial().getTask();
  }

  /**
   * Use first {@link #hasTaskFollowUpQuestionnaire()}
   * @return the follow up questionnaire
   */
  public Questionnaire getFollowUpQuestionnaire() {
    String questionnaireId = getCurTrial().getTask().getFollowUpQuestionnaireId();
    return experiment.findQuestionnaireById(questionnaireId);
  }

  /**
   * @return whether this current task has a followUpQuestionnaire
   */
  public boolean hasTaskFollowUpQuestionnaire() {
    return getFollowUpQuestionnaire() != null;
  }

  /**
   * @return whether participant sees a questionnaire after all the tasks
   */
  public boolean hasPostExpQuestionnaire() {
    return getPostExpQuestionnaire() != null;
  }

  /**
   * Use first {@link #hasPostExpQuestionnaire()}
   * @return the post exp questionnaire
   */
  public Questionnaire getPostExpQuestionnaire() {
    return experiment.getPostExpQuestionnaire();
  }

  /**
   * @return trial or condition bundle currently loaded
   */
  public ConditionBundle getCurTrial() {
    return block.getOrderedConditionBundles().get(trialIndex);
  }

  /**
   * @return whether participant is currently performing the task
   */
  public boolean isTaskRunning() {
    return taskIsRunning;
  }

  /**
   * Identifier for the notification, a new notification with this id will overwrite the old one
   */
  public static final int NOTIFICATION_ID = 0;

  /**
   * Sticky jask notification is shown to enable the participant to finish task
   *
   * @param task is needed to show correct name and short summary of task
   */
  public static void showTaskNotification(Context context, Task task) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    builder.setContentTitle(task.getName());
    builder.setContentText(task.getShortDescription());
    builder.setSmallIcon(R.drawable.check_circle);
    builder.addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.help_circle,
        context.getString(R.string.help_notification), createTaskIntent(context, false)));
    builder.addAction(new android.support.v4.app.NotificationCompat.Action(R.drawable.check_circle,
        context.getString(R.string.finish_notification), createTaskIntent(context, true)));
    builder.setAutoCancel(false);
    builder.setOngoing(true);
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(NOTIFICATION_ID, builder.build());
  }

  /**
   * Is triggered when notification is clicked
   *
   * @param finished true: finishes task and opens next| false: just show description again
   * @return opens activity with delay
   */
  private static PendingIntent createTaskIntent(Context context, boolean finished) {
    Intent finishedIntent = new Intent(context, TaskActivity.class);
    finishedIntent.putExtra(TaskActivity.KEY_TASK_FINISHED, finished);
    //Different request code for the intents, otherwise it is just overwritten
    int requestCode = finished ? 0 : 1;
    return PendingIntent.getActivity(context, requestCode, finishedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
  }
}
