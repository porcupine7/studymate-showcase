package at.fhooe.studymate.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Map;

import at.fhhagenberg.mint.automate.loggingclient.javacore.action.EventAction;
import at.fhhagenberg.mint.automate.loggingclient.javacore.kernel.KernelBase;
import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.blocking.ConditionBundle;
import at.fhooe.studymate.fragments.QuestionnaireFragment;
import at.fhooe.studymate.logging.events.AnswersTransmissionEvent;

/**
 * Activity to present questionnaires
 */
public class QuestionnaireActivity extends AppCompatActivity implements QuestionnaireFragment.Callback {
  public static final int REQUEST_CODE = 121;
  public static final int POST_EXP_REQUEST_CODE = 122;

  public static final String EXTRA_JSON_QUESTIONNAIRE = "questionnaire";
  public static final String EXTRA_PARTICIPANT_ID = "participant_id";
  public static final String EXTRA_JSON_CONDITIONS = "conditions";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    String questionnaireJson = getIntent().getStringExtra(EXTRA_JSON_QUESTIONNAIRE);
    //Host QuestionnaireFragment, which builds questionnaire from json
    getFragmentManager().beginTransaction().replace(R.id.container,
        QuestionnaireFragment.newInstance(questionnaireJson)).commit();
  }

  @Override
  public void questionsDone(String questionnaireId, Map<String, String> questionToAnswerMap) {
    String participantId = getIntent().getStringExtra(EXTRA_PARTICIPANT_ID);
    String conditionBundle = getIntent().getStringExtra(EXTRA_JSON_CONDITIONS);
    ConditionBundle conditions = conditionBundle != null ? ConditionBundle.fromJson(conditionBundle) : null;
    AnswersTransmissionEvent answerEvent = new AnswersTransmissionEvent(System.currentTimeMillis(),
        participantId, conditions, questionnaireId, questionToAnswerMap);
    try {
      new EventAction(KernelBase.getKernel(), answerEvent).execute();
    } catch (Exception e) {
      Log.e(QuestionnaireActivity.class.getSimpleName(), "Answers not uploaded: Kernel not available!!");
    }
    Intent returnIntent = new Intent();
    setResult(Activity.RESULT_OK, returnIntent);
    finish();
  }
}
