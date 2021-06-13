package com.example.toeictraining.ui.fragments.reminder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseRecyclerAdapter
import com.example.toeictraining.base.BaseViewHolder
import com.example.toeictraining.base.entity.Topic
import kotlinx.android.synthetic.main.item_topic_review.view.*

class RemindTopicAdapter(
    callback: ReviewDiffUtilCallback
) : BaseRecyclerAdapter<Topic, RemindTopicAdapter.ReviewViewHolder>(callback) {

    var onTopicSelected = { _: Topic -> }

    private val onItemClick = { position: Int ->
        onTopicSelected(getItem(position))
        notifyItemChanged(position)
    }

    override fun getItemLayoutResource(viewType: Int): Int = R.layout.item_topic_review

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReviewViewHolder(getItemView(parent, viewType), onItemClick)

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.onBindData(position, getItem(position))
    }

    class ReviewViewHolder(
        itemView: View,
        private val onItemClick: (Int) -> Unit
    ) : BaseViewHolder<Topic>(itemView) {

        override fun onBindData(itemPosition: Int, itemData: Topic) {
            super.onBindData(itemPosition, itemData)
            itemView.checkedTextTopicReview.apply {
                text = itemData.name
                isChecked = itemData.remind == Topic.IS_REMINDED
            }
        }

        override fun onItemClickListener(itemData: Topic) = with(itemData) {
            remind = if (remind == Topic.IS_REMINDED) Topic.IS_NOT_REMINDED else Topic.IS_REMINDED
            onItemClick(itemPosition)
        }
    }

    class ReviewDiffUtilCallback : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean = oldItem === newItem
        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean =
            oldItem == newItem
    }
}
