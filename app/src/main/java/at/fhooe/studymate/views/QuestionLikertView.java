package at.fhooe.studymate.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import at.fhooe.studymate.R;
import at.fhooe.studymate.entities.Question;

/**
 * Participant can respond on a scale.
 */
public class QuestionLikertView extends LinearLayout implements QuestionView {

  private SeekBar seekBar;

  public QuestionLikertView(Context context, Question question) {
    super(context);
    init(question);
  }

  /**
   * Inits likert scale with correct number of steps. Default is set to the middle.
   *
   * @param question likertSize defines number of steps and answer[0] is left label, while answer[1]
   *                 is right label.
   */
  private void init(Question question) {
    inflate(getContext(), R.layout.view_question_likert, this);
    TextView txtQuestion = (TextView) findViewById(R.id.txt_question);
    txtQuestion.setText(question.getQuestion());

    if (question.getAnswers() != null && question.getAnswers().size() >= 2) {
      TextView txtLikertLeft = (TextView) findViewById(R.id.txt_likert_left);
      txtLikertLeft.setText(question.getAnswers().get(0));
      TextView txtLikertRight = (TextView) findViewById(R.id.txt_likert_right);
      txtLikertRight.setText(question.getAnswers().get(1));
    }

    seekBar = (SeekBar) findViewById(R.id.answer_seekbar);
    int maxSize = question.getLikertSize() > 0 ? question.getLikertSize() - 1 : 4;
    seekBar.setMax(maxSize);
    seekBar.setProgress(maxSize / 2);
  }

  /**
   * @return position on scale(number as string) = from 1 to likertSize
   */
  @NonNull
  public String getAnswer() {
    StringBuilder answer = new StringBuilder();
    answer.append(seekBar.getProgress() + 1);
    return answer.toString();
  }
}
