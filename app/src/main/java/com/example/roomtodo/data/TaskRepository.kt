package com.example.roomtodo.data

import com.example.roomtodo.data.model.Task

class TaskRepository(
    private val taskDao: TaskDao
) {
    val allTasks = taskDao.getAllTasks()
    val allCompletedTasks = taskDao.getCompletedTasks()

    suspend fun addNewTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun completeTask(task: Task) {
        taskDao.completeTask(task.id)
    }

}