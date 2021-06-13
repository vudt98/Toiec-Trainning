package com.example.toeictraining.ui.fragments.vocabulary

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseRecyclerAdapter
import com.example.toeictraining.base.BaseViewHolder
import com.example.toeictraining.base.entity.Topic
import com.example.toeictraining.utils.Constants
import kotlinx.android.synthetic.main.item_topic.view.*

class TopicAdapter(
    callback: TopicDiffUtilCallback
) : BaseRecyclerAdapter<Topic, TopicAdapter.TopicViewHolder>(callback) {

    var onItemClick: (Topic) -> Unit = {}

    override fun getItemLayoutResource(viewType: Int): Int = R.layout.item_topic

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TopicViewHolder(
        itemView = getItemView(parent, viewType),
        onItemClick = onItemClick
    )

    class TopicViewHolder(
        itemView: View,
        private val onItemClick: (Topic) -> Unit
    ) : BaseViewHolder<Topic>(itemView) {

        override fun onBindData(topic: Topic) = with(itemView) {
            super.onBindData(topic)

            progressTopic.apply {
                max = topic.total
                progress = topic.master
                secondaryProgress = topic.total - topic.newWord
            }

            textNameTopic?.text = topic.name
            textLastTime?.text = topic.lastTime ?: Constants.DEFAULT_LAST_TIME
        }

        override fun onItemClickListener(itemData: Topic) = onItemClick(itemData)
    }

    class TopicDiffUtilCallback : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean =
            oldItem == newItem
    }
}
