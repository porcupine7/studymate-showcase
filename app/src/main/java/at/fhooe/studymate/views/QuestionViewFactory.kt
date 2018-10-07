package at.fhooe.studymate.views

import android.content.Context
import android.view.View

import at.fhooe.studymate.entities.Question

/**
 * Util class to create QuestionViews from a question
 *
 * @see Question
 *
 * @see QuestionView
 */
object QuestionViewFactory {

    /**
     * Creates the correct QuestionView depending on the question's type
     *
     * @param question data entity parsed from JSON
     * @return has to be a view implementing the interface
     */
    fun createQuestionView(context: Context, question: Question): QuestionView? {
        val questionType = question.type
        var questionView: QuestionView? = null
        when (questionType) {
            "radio" -> questionView = QuestionRadioView(context, question)
            "checkbox" -> questionView = QuestionCheckboxView(context, question)
            "text" -> questionView = QuestionTextView(context, question)
            "likert" -> questionView = QuestionLikertView(context, question)
        }
        if (questionView != null) (questionView as View).tag = question.id
        return questionView
    }
}
