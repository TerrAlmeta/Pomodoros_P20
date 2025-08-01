package com.example.pomodoros

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksComparator()) {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, listener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onDoubleClick(task: Task)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskItemView: TextView = itemView.findViewById(R.id.textView)
        private val cyclesTextView: TextView = itemView.findViewById(R.id.cycles_text_view)
        private var lastClickTime: Long = 0

        fun bind(task: Task, listener: OnItemClickListener?) {
            taskItemView.text = task.name
            cyclesTextView.text = task.cycles.toString()
            val background = itemView.background as GradientDrawable
            if (task.isSelected) {
                val color = Color.parseColor(task.color)
                background.setColor(color)
                taskItemView.setTextColor(Color.parseColor("#344d91"))
                cyclesTextView.setTextColor(Color.parseColor("#344d91"))
                background.setStroke(4, Color.parseColor("#344d91"))
            } else {
                background.setColor(Color.TRANSPARENT)
                if (task.color.isNotEmpty()) {
                    val color = Color.parseColor(task.color)
                    taskItemView.setTextColor(color)
                    cyclesTextView.setTextColor(color)
                    background.setStroke(4, color)
                }
            }
            itemView.setOnClickListener {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < 500) {
                    listener?.onDoubleClick(task)
                } else {
                    listener?.onItemClick(task)
                }
                lastClickTime = clickTime
            }
        }

        companion object {
            fun create(parent: ViewGroup): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return TaskViewHolder(view)
            }
        }
    }

    class TasksComparator : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
