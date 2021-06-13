package com.example.toeictraining.ui.fragments.vocabulary

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseRecyclerAdapter
import com.example.toeictraining.base.BaseViewHolder
import com.example.toeictraining.base.entity.Topic
import com.example.toeictraining.data.model.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(
    callback: CategoryDiffUtilCallback
) : BaseRecyclerAdapter<Category, CategoryAdapter.CategoryViewHolder>(callback) {

    var onTopicClick: (Topic) -> Unit = {}

    private val onItemClick = { position: Int, category: Category ->
        category.isExpanded = !category.isExpanded
        notifyItemChanged(position)
    }

    override fun getItemLayoutResource(viewType: Int): Int = R.layout.item_category

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(
            itemView = getItemView(parent, viewType),
            viewPool = RecyclerView.RecycledViewPool(),
            onItemClick = onItemClick,
            onTopicClick = onTopicClick
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBindData(position, getItem(position))
    }

    class CategoryViewHolder(
        itemView: View,
        viewPool: RecyclerView.RecycledViewPool,
        private val onItemClick: (Int, Category) -> Unit,
        private val onTopicClick: (Topic) -> Unit = {}
    ) : BaseViewHolder<Category>(itemView) {

        private val topicAdapter = TopicAdapter(TopicAdapter.TopicDiffUtilCallback()).apply {
            onItemClick = onTopicClick
        }

        init {
            itemView.recyclerTopics.apply {
                setRecycledViewPool(viewPool)
                adapter = topicAdapter
            }
        }

        override fun onBindData(position: Int, category: Category) {
            super.onBindData(position, category)

            itemView.run {
                relativeCategory?.setBackgroundColor(Color.parseColor(category.color))
                textTitleCategory?.text = category.name
                textTitleTopics?.text = category.topics.joinToString { it.name }

                if (category.isExpanded) {
                    recyclerTopics?.visibility = View.VISIBLE
                    imageExpand?.setImageResource(R.drawable.ic_keyboard_arrow_up)
                } else {
                    recyclerTopics?.visibility = View.GONE
                    imageExpand?.setImageResource(R.drawable.ic_keyboard_arrow_down)
                }
            }
            topicAdapter.submitList(category.topics)
        }

        override fun onItemClickListener(itemData: Category) = onItemClick(itemPosition, itemData)
    }

    class CategoryDiffUtilCallback : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
            oldItem == newItem
    }
}
