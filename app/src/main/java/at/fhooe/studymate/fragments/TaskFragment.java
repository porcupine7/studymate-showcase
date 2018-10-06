package at.fhooe.studymate.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fhooe.studymate.R;
import at.fhooe.studymate.databinding.FragmentTaskBinding;
import at.fhooe.studymate.entities.TaskModel;
import at.fhooe.studymate.entities.ivs.Task;
import at.fhooe.studymate.entities.blocking.ConditionBundle;


/**
 * Fragment presenting one task.
 */
public class TaskFragment extends Fragment {

    FragmentTaskBinding binding;

    private ConditionBundle curConditionBundle;

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
        inflater.inflate(R.layout.fragment_task, container, false);

        binding = FragmentTaskBinding.inflate(inflater);

        if (loadTaskLater && curConditionBundle != null) {
            loadTask(curConditionBundle, false);
            loadTaskLater = false;
        }
        return binding.getRoot();
    }

    public void loadTask(ConditionBundle conditionBundle, boolean running) {
        curConditionBundle = conditionBundle;
        if (callback == null) {
            //A bit hacky, Check needed as can be called to early from activity!
            loadTaskLater = true;
            return;
        }
        Task task = curConditionBundle.getTask();
        TaskModel model = new TaskModel();
        model.setCallback(callback);
        model.setTitle(task.getName());
        model.setDescription(task.getDescription());
        model.setInstruction(curConditionBundle.getFormattedInstruction());
        binding.setModel(model);
    }

    public interface Callback {
        void startTask();

        void concedeTask();
    }
}
