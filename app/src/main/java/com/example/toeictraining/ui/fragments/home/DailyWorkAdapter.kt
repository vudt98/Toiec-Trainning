package com.example.toeictraining.ui.fragments.home

import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseRecyclerAdapter
import com.example.toeictraining.base.BaseViewHolder
import com.example.toeictraining.data.model.DailyWork
import kotlinx.android.synthetic.main.item_topic_review.view.*

class DailyWorkAdapter(
    callback: DailyWorkDiffUtilCallback
) : BaseRecyclerAdapter<DailyWork, DailyWorkAdapter.DailyWorkViewHolder>(callback) {

    var onItemClick: (DailyWork) -> Unit = {}

    override fun getItemLayoutResource(viewType: Int): Int = R.layout.item_topic_review

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWorkViewHolder =
        DailyWorkViewHolder(getItemView(parent, viewType), onItemClick)

    override fun onBindViewHolder(holder: DailyWorkViewHolder, position: Int) {
        holder.onBindData(position, getItem(position))
    }

    class DailyWorkViewHolder(
        itemView: View,
        private val onItemClick: (DailyWork) -> Unit
    ) : BaseViewHolder<DailyWork>(itemView) {

        override fun onBindData(itemPosition: Int, itemData: DailyWork) {
            super.onBindData(itemPosition, itemData)
            itemView.checkedTextTopicReview.apply {
                text = Html.fromHtml(itemData.content)
                isChecked = itemData.isDone == true
            }
        }

        override fun onItemClickListener(itemData: DailyWork) = onItemClick(itemData)
    }

    class DailyWorkDiffUtilCallback : DiffUtil.ItemCallback<DailyWork>() {
        override fun areItemsTheSame(oldItem: DailyWork, newItem: DailyWork): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: DailyWork, newItem: DailyWork): Boolean =
            oldItem == newItem
    }
}
