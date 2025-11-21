package com.example.backend.service

import com.example.backend.model.*
import com.example.backend.repository.TodoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Service
class TodoService(private val repository: TodoRepository) {

    fun getAllTodos(): Flux<Todo> = Flux.fromIterable(repository.findAll())

    fun getTodoById(id: String): Mono<Todo> =
        Mono.justOrEmpty(repository.findById(id))

    fun createTodo(request: CreateTodoRequest): Mono<Todo> {
        val todo = Todo(
            title = request.title,
            description = request.description,
            userId = request.userId
        )
        return Mono.just(repository.save(todo))
    }

    fun updateTodo(id: String, request: UpdateTodoRequest): Mono<Todo> =
        Mono.justOrEmpty(repository.findById(id))
            .map { existing ->
                val updated = existing.copy(
                    title = request.title ?: existing.title,
                    description = request.description ?: existing.description,
                    completed = request.completed ?: existing.completed,
                    updatedAt = Instant.now()
                )
                repository.save(updated)
            }

    fun deleteTodo(id: String): Mono<Boolean> =
        Mono.just(repository.deleteById(id))

    fun getStats(): Mono<TodoStats> {
        val total = repository.count()
        val completed = repository.countCompleted()
        val pending = total - completed
        val completionRate = if (total > 0) completed.toDouble() / total else 0.0

        return Mono.just(
            TodoStats(
                total = total,
                completed = completed,
                pending = pending,
                completionRate = completionRate
            )
        )
    }
}
