package com.example.backend.model

import java.time.Instant
import java.util.UUID

data class Todo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String? = null,
    val completed: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val userId: String
)

data class CreateTodoRequest(
    val title: String,
    val description: String? = null,
    val userId: String
)

data class UpdateTodoRequest(
    val title: String? = null,
    val description: String? = null,
    val completed: Boolean? = null
)

data class TodoStats(
    val total: Int,
    val completed: Int,
    val pending: Int,
    val completionRate: Double
)
