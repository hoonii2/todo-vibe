package com.example.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTodoBinding

class TodoAdapter(
    private val todos: List<Todo>,
    private val onToggle: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todos[position]
        holder.binding.apply {
            checkbox.isChecked = todo.completed
            tvTitle.text = todo.title
            tvTitle.alpha = if (todo.completed) 0.5f else 1.0f

            checkbox.setOnClickListener { onToggle(todo) }
            btnDelete.setOnClickListener { onDelete(todo) }
        }
    }

    override fun getItemCount() = todos.size
}
