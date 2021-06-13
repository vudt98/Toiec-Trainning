package com.example.toeictraining.ui.fragments.test.history

import android.app.Activity
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toeictraining.R
import com.example.toeictraining.base.entity.Exam
import com.example.toeictraining.ui.fragments.test.result.ResultTestFragment
import com.example.toeictraining.ui.main.MainActivity
import com.example.toeictraining.utils.DateUtil
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter(
    var activity: Activity,
    var exams: List<Exam>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    companion object {
        val TAG = HistoryAdapter::class.java.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.item_history,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return exams.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exam = exams[position]
        holder.textIdExam.text = activity.getString(R.string.id_exam).plus(exam.id.toString())
        holder.textPart.text = activity.getString(R.string.part).plus(exam.part)
        holder.textTotalTime.text =
            activity.getString(R.string.test_time_total)
                .plus(DateUtil.secondsToStringTime(exam.time))
        holder.textTotalScore.text =
            activity.getString(R.string.total_score).plus(": ").plus(exam.score)
        holder.itemView.setOnClickListener {
            (activity as MainActivity).openFragment(
                ResultTestFragment(exam.id),
                true
            )
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textIdExam: TextView = view.text_index_question
        var textTotalTime: TextView = view.text_result
        var textPart: TextView = view.text_correct_answer
        var textTotalScore: TextView = view.text_my_answer
    }
}