package at.fhooe.studymate.views;

import android.content.Context;
import android.view.View;

import at.fhooe.studymate.entities.Question;

/**
 * Util class to create QuestionViews from a question
 *
 * @see Question
 * @see QuestionView
 */
public class QuestionViewFactory {

  /**
   * Creates the correct QuestionView depending on the question's type
   *
   * @param question data entity parsed from JSON
   * @return has to be a view implementing the interface
   */
  public static QuestionView createQuestionView(Context context, Question question) {
    String questionType = question.getType();
    QuestionView questionView = null;
    if ("radio".equals(questionType)) {
      questionView = new QuestionRadioView(context, question);
    } else if ("checkbox".equals(questionType)) {
      questionView = new QuestionCheckboxView(context, question);
    } else if ("text".equals(questionType)) {
      questionView = new QuestionTextView(context, question);
    } else if ("likert".equals(questionType)) {
      questionView = new QuestionLikertView(context, question);
    }
    if(questionView!=null) ((View) questionView).setTag(question.getId());
    return questionView;
  }

}
