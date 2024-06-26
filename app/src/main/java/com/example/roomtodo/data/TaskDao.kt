package com.example.roomtodo.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.roomtodo.data.model.Task


@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table WHERE isCompleted = 1")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("UPDATE task_table SET isCompleted = NOT isCompleted WHERE id = :taskId")
    suspend fun completeTask(taskId: Int)

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<List<Task>>

}