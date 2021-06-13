package com.example.toeictraining.ui.fragments.test.do_test

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toeictraining.R
import com.example.toeictraining.ui.main.MainActivity
import kotlinx.android.synthetic.main.item_recyclerview_question_normal.view.*

class ListQuestionAdapter(
    private var activity: MainActivity,
    private var questionsStatus: MutableList<QuestionStatus>,
    private var viewModel: DoTestViewModel
) : RecyclerView.Adapter<ListQuestionAdapter.ViewHolder>() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(viewType, null, false))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemViewType(position: Int): Int {
        return when (questionsStatus[position].status) {
            QuestionStatus.Status.MAIN -> R.layout.item_recyclerview_question_main
            QuestionStatus.Status.DONE -> R.layout.item_recyclerview_question_done
            else -> R.layout.item_recyclerview_question_normal
        }
    }

    override fun getItemCount(): Int {
        return questionsStatus.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questionsStatus[position]
        if (question.status == QuestionStatus.Status.MAIN) {
            viewModel.indexMain.postValue(position)
            recyclerView.apply {
                (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    position,
                    (Resources.getSystem().displayMetrics.widthPixels - (Resources.getSystem().displayMetrics.density * 60).toInt()) / 2
                )
            }
        }
        holder.textQuestion.text = question.index.toString()
        holder.itemView.setOnClickListener {
            activity.showLoadingDialog()
            viewModel.indexMain.value?.let {
                if (questionsStatus[it].answer.isNotBlank()) {
                    questionsStatus[it].status =
                        QuestionStatus.Status.DONE // load lại status
                } else {
                    questionsStatus[it].status =
                        QuestionStatus.Status.NOT_DONE // load lại status
                }
            }
            question.status = QuestionStatus.Status.MAIN
            notifyDataSetChanged()
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textQuestion: TextView = view.text_index_question
    }
}