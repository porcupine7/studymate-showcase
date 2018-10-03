package at.fhooe.studymate.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.Question;

/**
 * Participant can select one option of multiple ones
 */
public class QuestionRadioView extends LinearLayout implements QuestionView {

  private RadioGroup radioGroup;

  public QuestionRadioView(Context context, Question question) {
    super(context);
    init(question);
  }

  private void init(Question question) {
    inflate(getContext(), R.layout.view_question_radio, this);
    TextView txtQuestion = (TextView) findViewById(R.id.txt_question);
    txtQuestion.setText(question.getQuestion());
    radioGroup = (RadioGroup) findViewById(R.id.answer_radio_group);
    for (String answer : question.getAnswers()) {
      RadioButton answerBtn = new RadioButton(getContext());
      answerBtn.setText(answer);
      radioGroup.addView(answerBtn);
    }
  }

  /**
   * @return Text of selected answer, empty when none was selected
   */
  @NonNull
  public String getAnswer() {
    StringBuilder answer = new StringBuilder();
    answer.append("");
    int selectedId = radioGroup.getCheckedRadioButtonId();
    RadioButton selectedBtn = (RadioButton) radioGroup.findViewById(selectedId);
    if (selectedBtn != null) answer.append(selectedBtn.getText());
    return answer.toString();
  }
}
