package at.fhooe.studymate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.fhooe.studymate.R;
import at.fhooe.studymate.activities.QuestionnaireActivity;
import at.fhooe.studymate.entities.Question;
import at.fhooe.studymate.entities.Questionnaire;
import at.fhooe.studymate.views.QuestionView;
import at.fhooe.studymate.views.QuestionViewFactory;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment presenting questions of one questionnaire
 */
public class QuestionnaireFragment extends Fragment {
  private Questionnaire questionnaire;
  private List<QuestionView> questionViewList;

  @Bind(R.id.container_questions)
  ViewGroup questionsContainer;
  private Callback callback;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    callback = (Callback) getActivity();
    questionViewList = new ArrayList<>();
    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_questionnaire, container, false);
    ButterKnife.bind(this, rootView);

    String questionnaireJson = getArguments().getString(QuestionnaireActivity.EXTRA_JSON_QUESTIONNAIRE);
    questionnaire = new Gson().fromJson(questionnaireJson, Questionnaire.class);
    if (questionnaire == null) {
      //Should not happen
      Toast.makeText(getActivity(), "Questionnaire malformed!!", Toast.LENGTH_SHORT).show();
      return null;
    }
    addQuestionViews(questionnaire.getQuestions());
    return rootView;
  }

  /**
   * Creates QuestionView for every question and adds it to parent view
   *
   * @param questions Questions from json
   */
  private void addQuestionViews(List<Question> questions) {
    for (Question question : questions) {
      QuestionView questionView = QuestionViewFactory.createQuestionView(getActivity(), question);
      if (questionView != null) {
        questionsContainer.addView((View) questionView);
        questionViewList.add(questionView);
      }
    }
  }

  /**
   * When participants submits his response, all responses are gathered and send to QuestionnaireActivity
   */
  @OnClick(R.id.btn_done_questions)
  public void questionsDone() {
    Map<String, String> questionToAnswerMap = new HashMap<>();
    for (QuestionView questionView : questionViewList) {
      String questionId = (String) ((View) questionView).getTag();
      String answer = questionView.getAnswer();
      questionToAnswerMap.put(questionId, answer);
    }
    callback.questionsDone(questionnaire.getId(), questionToAnswerMap);
  }

  @Deprecated
  private boolean loadAssets(String assetFileName) {
    try {
      InputStream inputStream = getActivity().getAssets().open(assetFileName);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      questionnaire = new Gson().fromJson(reader, Questionnaire.class);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * QuestionFragment should only be created with valid questionnaire json
   * @param questionnaireJson from exp json
   * @return a fragment
   */
  public static QuestionnaireFragment newInstance(String questionnaireJson) {
    Bundle args = new Bundle();
    args.putString(QuestionnaireActivity.EXTRA_JSON_QUESTIONNAIRE, questionnaireJson);
    QuestionnaireFragment fragment = new QuestionnaireFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public interface Callback {
    /**
     * Send answers to the questions to activity
     *
     * @param questionnaireId Id of questionnaire
     * @param questionToAnswerMap Id of question to answer
     */
    void questionsDone(String questionnaireId, Map<String, String> questionToAnswerMap);
  }
}
