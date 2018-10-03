package at.fhooe.studymate.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.Question;

/**
 * Participant can select multiple options
 */
public class QuestionCheckboxView extends LinearLayout implements QuestionView{

  private List<CheckBox> checkBoxes;

  public QuestionCheckboxView(Context context, Question question) {
    super(context);
    init(question);
  }

  private void init(Question question) {
    checkBoxes = new ArrayList<>();
    inflate(getContext(), R.layout.view_question_checkbox, this);
    TextView txtQuestion = (TextView) findViewById(R.id.txt_question);
    txtQuestion.setText(question.getQuestion());
    LinearLayout answersContainer = (LinearLayout) findViewById(R.id.container_checkbox_answers);
    for (String answer : question.getAnswers()) {
      CheckBox checkBox = new CheckBox(getContext());
      checkBox.setText(answer);
      checkBoxes.add(checkBox);
      answersContainer.addView(checkBox);
    }
  }

  /**
   * @return all answers concatenated by |
   */
  @NonNull
  public String getAnswer() {
    StringBuilder answer = new StringBuilder();
    answer.append("");
    for (CheckBox checkBox : checkBoxes) {
      if (checkBox.isChecked()) {
        answer.append(checkBox.getText().toString());
        answer.append("|");
      }
    }
    if (answer.length() > 0) answer.deleteCharAt(answer.length() - 1);
    return answer.toString();
  }
}
