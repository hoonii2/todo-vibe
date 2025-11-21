package com.example.backend.controller

import com.example.backend.model.*
import com.example.backend.service.TodoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = ["*"]) // For development, restrict in production
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun getAllTodos(): Flux<Todo> = todoService.getAllTodos()

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: String): Mono<Todo> =
        todoService.getTodoById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Todo not found with id: $id")))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@RequestBody request: CreateTodoRequest): Mono<Todo> =
        todoService.createTodo(request)

    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: String,
        @RequestBody request: UpdateTodoRequest
    ): Mono<Todo> =
        todoService.updateTodo(id, request)
            .switchIfEmpty(Mono.error(NotFoundException("Todo not found with id: $id")))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable id: String): Mono<Void> =
        todoService.deleteTodo(id)
            .flatMap { deleted ->
                if (deleted) Mono.empty()
                else Mono.error(NotFoundException("Todo not found with id: $id"))
            }

    @GetMapping("/stats")
    fun getStats(): Mono<TodoStats> = todoService.getStats()
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(message: String) : RuntimeException(message)
