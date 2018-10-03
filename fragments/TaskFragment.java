package at.fhooe.studymate.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.ivs.Task;
import at.fhooe.studymate.entities.blocking.ConditionBundle;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment presenting one task.
 */
public class TaskFragment extends Fragment {
  @Bind(R.id.btn_start)
  Button startBtn;

  @Bind(R.id.btn_concede)
  Button concedeBtn;

  @Bind(R.id.txt_task_title)
  TextView taskTitleTxt;

  @Bind(R.id.txt_task_description)
  TextView taskDescriptionTxt;

  @Bind(R.id.txt_use_app)
  TextView useAppTxt;

  @Bind(R.id.txt_running)
  TextView runningTxt;

  private ConditionBundle curConditionBundle;
  private boolean taskRunning;
  private boolean loadTaskLater;
  private Callback callback;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(false);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onAttach(Activity activity) {
    //New onAttach is not called on older devices. Oh Android Boy!
    super.onAttach(activity);
    if (activity instanceof Callback) {
      callback = (Callback) activity;
    } else {
      throw new IllegalStateException("Activity not allowed to use TaskFragment: Implement Callback");
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_task, container, false);
    ButterKnife.bind(this, rootView);
    taskDescriptionTxt.setMovementMethod(new ScrollingMovementMethod());
    if (loadTaskLater && curConditionBundle != null) {
      loadTask(curConditionBundle, taskRunning);
      loadTaskLater = false;
    }
    return rootView;
  }

  @OnClick(R.id.btn_start)
  void startedTask() {
    setRunning(true);
    if (callback != null) {
      callback.startTask();
    }
    //TODO here you should start the notification and navevent started should be here, notification
    //should send task_finished
    //EventBus.getDefault().post(NavEvent.TASK_FINISHED);
  }

  @OnClick(R.id.btn_concede)
  void concededTask() {
    setRunning(false);
    callback.concedeTask();
  }

  public void loadTask(ConditionBundle conditionBundle, boolean running) {
    curConditionBundle = conditionBundle;
    taskRunning = running;
    if (taskTitleTxt == null) {
      loadTaskLater = true;
      return;
    }
    Task task = conditionBundle.getTask();
    taskTitleTxt.setText(task.getName());
    taskDescriptionTxt.setText(task.getDescription());
    useAppTxt.setText(conditionBundle.getFormattedInstruction());
    setRunning(taskRunning);
  }

  private void setRunning(boolean running) {
    if (running) {
      startBtn.setText(R.string.go_back_btn);
      runningTxt.setVisibility(View.VISIBLE);
      concedeBtn.setVisibility(View.VISIBLE);
    } else {
      startBtn.setText(R.string.start_btn);
      runningTxt.setVisibility(View.INVISIBLE);
      concedeBtn.setVisibility(View.INVISIBLE);
    }
  }

  public interface Callback {
    void startTask();

    void concedeTask();
  }
}
