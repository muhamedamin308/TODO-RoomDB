package com.example.roomtodo.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomtodo.data.model.Task
import com.example.roomtodo.databinding.TodosItemBinding

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>() {

    inner class TasksViewHolder(
        private val binding: TodosItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Task) {
            binding.apply {
                titleTv.text = todo.title
                descriptionTv.text = todo.description
                completeTask.setOnCheckedChangeListener(null)
                completeTask.isChecked = todo.isCompleted
                completeTask.setOnCheckedChangeListener { _, isChecked ->
                    if (todo.isCompleted != isChecked) {
                        todo.isCompleted = isChecked
                        onItemClicked?.invoke(todo)
                    }
                }
                currentTaskLl.setOnClickListener {
                    val action =
                        AllTasksFragmentDirections.actionAllTasksFragmentToAddNewTaskFragment(todo)
                    itemView.findNavController().navigate(action)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    }

    private var differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder =
        TasksViewHolder(
            TodosItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val todo = differ.currentList[position]
        holder.bind(todo)
    }

    var onItemClicked: ((Task) -> Unit)? = null

    fun setTask(task: List<Task>) {
        differ.submitList(task)
    }

    fun getTask(position: Int): Task {
        return differ.currentList[position]
    }
}
