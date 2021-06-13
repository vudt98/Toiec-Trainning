package com.example.toeictraining.ui.fragments.home

import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseRecyclerAdapter
import com.example.toeictraining.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_analysis_result_part.view.*

class RecentResultAdapter(
    callback: RecentResultDiffUtilCallback
) : BaseRecyclerAdapter<Int, RecentResultAdapter.RecentResultViewHolder>(callback) {

    var onItemClick: (Int) -> Unit = {}

    override fun getItemLayoutResource(viewType: Int): Int = R.layout.item_analysis_result_part

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentResultViewHolder =
        RecentResultViewHolder(getItemView(parent, viewType), onItemClick)

    override fun onBindViewHolder(holder: RecentResultViewHolder, position: Int) {
        holder.onBindData(position, getItem(position))
    }

    class RecentResultViewHolder(
        itemView: View,
        private val onItemClick: (Int) -> Unit
    ) : BaseViewHolder<Int>(itemView) {

        override fun onBindData(itemPosition: Int, itemData: Int) {
            super.onBindData(itemPosition, itemData)
            itemView.apply {
                textPartName.text =
                    context.resources.getString(R.string.title_part, itemPosition + 1)
                progressBar.apply {
                    max = 100
                    progress = itemData
                }
            }
            onBindTextPartLevel(itemData)
        }

        private fun onBindTextPartLevel(itemData: Int) {
            val resultLevels = itemView.resources.getStringArray(R.array.resultLevels)
            itemView.textPartEvaluationResult?.text = Html.fromHtml(
                when (itemData) {
                    in 0..49 -> resultLevels[0]
                    in 50..79 -> resultLevels[1]
                    in 80..94 -> resultLevels[2]
                    else -> resultLevels[3]
                }
            )
        }

        override fun onItemClickListener(itemData: Int) = onItemClick(itemPosition)
    }

    class RecentResultDiffUtilCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem == newItem
    }
}
