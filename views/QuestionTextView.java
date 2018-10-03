package at.fhooe.studymate.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.Question;

/**
 * Participants can answer in an EditText
 */
public class QuestionTextView extends LinearLayout implements QuestionView {

  private EditText answerEditText;

  public QuestionTextView(Context context, Question question) {
    super(context);
    init(question);
  }

  private void init(Question question) {
    inflate(getContext(), R.layout.view_question_text, this);
    TextView txtQuestion = (TextView) findViewById(R.id.txt_question);
    txtQuestion.setText(question.getQuestion());
    answerEditText = (EditText) findViewById(R.id.answer_edit_text);
  }

  /**
   * @return text participant entered in EditText
   */
  @NonNull
  public String getAnswer() {
    StringBuilder answer = new StringBuilder();
    answer.append(answerEditText.getText().toString());
    return answer.toString();
  }
}
