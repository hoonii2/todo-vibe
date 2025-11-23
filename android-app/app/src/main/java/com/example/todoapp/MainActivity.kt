package com.example.todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TodoAdapter
    private val todos = mutableListOf<Todo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupAddButton()
        loadTodos()
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter(
            todos = todos,
            onToggle = { todo -> toggleTodo(todo) },
            onDelete = { todo -> deleteTodo(todo) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupAddButton() {
        binding.btnAdd.setOnClickListener {
            val title = binding.editTodo.text.toString().trim()
            if (title.isNotEmpty()) {
                addTodo(title)
                binding.editTodo.text.clear()
            }
        }
    }

    private fun loadTodos() {
        lifecycleScope.launch {
            try {
                val result = ApiClient.api.getTodos()
                todos.clear()
                todos.addAll(result)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                showError("Failed to load todos: ${e.message}")
            }
        }
    }

    private fun addTodo(title: String) {
        lifecycleScope.launch {
            try {
                val newTodo = ApiClient.api.createTodo(CreateTodoRequest(title))
                todos.add(0, newTodo)
                adapter.notifyItemInserted(0)
                binding.recyclerView.scrollToPosition(0)
            } catch (e: Exception) {
                showError("Failed to add todo: ${e.message}")
            }
        }
    }

    private fun toggleTodo(todo: Todo) {
        lifecycleScope.launch {
            try {
                val updated = ApiClient.api.updateTodo(todo.id, UpdateTodoRequest(!todo.completed))
                val index = todos.indexOfFirst { it.id == todo.id }
                if (index >= 0) {
                    todos[index] = updated
                    adapter.notifyItemChanged(index)
                }
            } catch (e: Exception) {
                showError("Failed to update todo: ${e.message}")
            }
        }
    }

    private fun deleteTodo(todo: Todo) {
        lifecycleScope.launch {
            try {
                ApiClient.api.deleteTodo(todo.id)
                val index = todos.indexOfFirst { it.id == todo.id }
                if (index >= 0) {
                    todos.removeAt(index)
                    adapter.notifyItemRemoved(index)
                }
            } catch (e: Exception) {
                showError("Failed to delete todo: ${e.message}")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
