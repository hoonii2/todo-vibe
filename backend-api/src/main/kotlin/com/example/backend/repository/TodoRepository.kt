package com.example.backend.repository

import com.example.backend.model.Todo
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class TodoRepository {
    private val store = ConcurrentHashMap<String, Todo>()

    fun findAll(): List<Todo> = store.values.toList()

    fun findById(id: String): Todo? = store[id]

    fun save(todo: Todo): Todo {
        store[todo.id] = todo
        return todo
    }

    fun deleteById(id: String): Boolean {
        return store.remove(id) != null
    }

    fun count(): Int = store.size

    fun countCompleted(): Int = store.values.count { it.completed }
}
