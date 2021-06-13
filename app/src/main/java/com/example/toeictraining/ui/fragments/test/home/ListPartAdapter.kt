package com.example.toeictraining.ui.fragments.test.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.toeictraining.R
import com.example.toeictraining.ui.fragments.test.start_test.StartTestFragment
import com.example.toeictraining.ui.main.MainActivity
import kotlinx.android.synthetic.main.item_recyclerview_image.view.*

class ListPartAdapter(
    private var context: Activity,
    private var resources: Array<Int>
) : RecyclerView.Adapter<ListPartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_recyclerview_image,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return resources.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textPart.text = (position + 1).toString()
        when (position) {
            0 -> {
                holder.iconTypePart.setImageResource(R.drawable.sound_24_primarycolor)
                holder.desPart.text = context.getString(R.string.des_part_1)
            }
            1 -> {
                holder.iconTypePart.setImageResource(R.drawable.sound_24_primarycolor)
                holder.desPart.text = context.getString(R.string.des_part_2)
            }
            2 -> {
                holder.iconTypePart.setImageResource(R.drawable.sound_24_primarycolor)
                holder.desPart.text = context.getString(R.string.des_part_3)
            }
            3 -> {
                holder.iconTypePart.setImageResource(R.drawable.sound_24_primarycolor)
                holder.desPart.text = context.getString(R.string.des_part_4)
            }
            4 -> {
                holder.iconTypePart.setImageResource(R.drawable.read_24dp_primarycolor)
                holder.desPart.text = context.getString(R.string.des_part_5)
            }
            5 -> {
                holder.iconTypePart.setImageResource(R.drawable.read_24dp_primarycolor)
                holder.desPart.text = context.getString(R.string.des_part_6)
            }
            6 -> {
                holder.iconTypePart.setImageResource(R.drawable.read_24dp_primarycolor)
                holder.desPart.text = context.getString(R.string.des_part_7)
            }
        }
        holder.itemView.setOnClickListener {
            (context as MainActivity).openFragment(
                StartTestFragment(position + 1),
                true
            )
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iconTypePart: ImageView = view.icon_type_part
        var textPart: TextView = view.text_part
        var desPart: TextView = view.des_part
    }
}