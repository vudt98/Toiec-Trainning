package com.example.toeictraining.ui.fragments.test.result

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.toeictraining.R
import com.example.toeictraining.ui.main.MainActivity
import kotlinx.android.synthetic.main.dialog_show_question.*
import kotlinx.android.synthetic.main.do_test_fragment.image_question
import kotlinx.android.synthetic.main.do_test_fragment.text_a
import kotlinx.android.synthetic.main.do_test_fragment.text_b
import kotlinx.android.synthetic.main.do_test_fragment.text_c
import kotlinx.android.synthetic.main.do_test_fragment.text_d
import kotlinx.android.synthetic.main.do_test_fragment.text_question_content
import kotlinx.android.synthetic.main.do_test_fragment.text_script
import kotlinx.android.synthetic.main.item_result.view.*

class ResultRecyclerViewAdapter(
    var activity: Activity,
    var results: MutableList<Result>
) : RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder>() {

    private var showQuestionDialog: Dialog? = null
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        val TAG: String = ResultRecyclerViewAdapter::class.java.name
    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.item_result,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        val question = result.question
        holder.textIndexQuestion.text =
            activity.getString(R.string.index_question).plus(position + 1)
        holder.textResult.text = activity.getString(R.string.result).plus(
            if (result.myAnswer == result.question.correctAnswer) activity.getString(R.string.right)
            else activity.getString(R.string.failed)
        )
        holder.itemView.setOnClickListener {
            (activity as MainActivity).showLoadingDialog()
            if (showQuestionDialog == null) {
                showQuestionDialog = Dialog(activity)
            }
            showQuestionDialog?.apply {
                setContentView(R.layout.dialog_show_question)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                );
                setCanceledOnTouchOutside(true)
                //index question
                text_index_question.text =
                    context.getString(R.string.index_question).plus(position + 1)
                //script
                text_script.visibility = View.GONE
                question.script?.let { script ->
                    text_script.visibility = View.VISIBLE
                    text_script.text = script
                }
                //content
                text_question_content.visibility = View.GONE
                question.content?.let { content ->
                    text_question_content.visibility = View.VISIBLE
                    text_question_content.text = content
                }
                //audio
                question.soundLink?.let {
                    mediaPlayer?.stop()
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setAudioAttributes(
                        AudioAttributes.Builder().setContentType(
                            AudioAttributes.CONTENT_TYPE_MUSIC
                        ).build()
                    )
                    mediaPlayer?.setDataSource(context, Uri.parse(question.soundLink))
                    mediaPlayer?.prepare()
                    mediaPlayer?.setOnPreparedListener {
                        mediaPlayer?.start()
                        (activity as MainActivity).cancelLoadingDialog()
                    }
                }
                //image
                image_question.visibility = View.GONE
                question.imageLink?.let { link ->
                    image_question.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(link)
                        .fitCenter()
                        .into(image_question)
                }
                //answer
                text_a.text = context.getString(R.string.a).plus(". ").plus(question.a)
                text_b.text = context.getString(R.string.b).plus(". ").plus(question.b)
                text_c.text = context.getString(R.string.c).plus(". ").plus(question.c)
                text_d.visibility = View.GONE
                question.d?.let { d ->
                    text_d.visibility = View.VISIBLE
                    text_d.text = context.getString(R.string.d).plus(". ").plus(d)
                }
                //draw answers
                when (result.myAnswer) {
                    context.getString(R.string.a) -> {
                        text_a.setBackgroundColor(Color.parseColor("#886A08"))
                        text_a.setTextColor(Color.WHITE)
                    }
                    context.getString(R.string.b) -> {
                        text_b.setBackgroundColor(Color.parseColor("#886A08"))
                        text_b.setTextColor(Color.WHITE)
                    }
                    context.getString(R.string.c) -> {
                        text_c.setBackgroundColor(Color.parseColor("#886A08"))
                        text_c.setTextColor(Color.WHITE)
                    }
                    context.getString(R.string.d) -> {
                        text_d.setBackgroundColor(Color.parseColor("#886A08"))
                        text_d.setTextColor(Color.WHITE)
                    }
                }
                when (result.question.correctAnswer) {
                    context.getString(R.string.a) -> {
                        text_a.setBackgroundColor(Color.GREEN)
                        text_a.setTextColor(Color.WHITE)
                    }
                    context.getString(R.string.b) -> {
                        text_b.setBackgroundColor(Color.GREEN)
                        text_b.setTextColor(Color.WHITE)
                    }
                    context.getString(R.string.c) -> {
                        text_c.setBackgroundColor(Color.GREEN)
                        text_c.setTextColor(Color.WHITE)
                    }
                    context.getString(R.string.d) -> {
                        text_d.setBackgroundColor(Color.GREEN)
                        text_d.setTextColor(Color.WHITE)
                    }
                }
                /*khi có link sound, không cancelloadingdialog, đợi loading xong sound,
                cancelloadingdialog bên mediaPlayer*/
                if (question.soundLink == null) {
                    (activity as MainActivity).cancelLoadingDialog()
                }

                setOnCancelListener {
                    mediaPlayer?.stop()
                }
                show()
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textIndexQuestion: TextView = view.text_index_question
        val textResult: TextView = view.text_result
    }
}