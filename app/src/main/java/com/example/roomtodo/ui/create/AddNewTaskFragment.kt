package com.example.roomtodo.ui.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roomtodo.R
import com.example.roomtodo.data.model.Task
import com.example.roomtodo.databinding.FragmentAddNewTaskBinding
import com.example.roomtodo.ui.viewmodel.TaskViewModel

class AddNewTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddNewTaskBinding
    private lateinit var viewModel: TaskViewModel
    private val args by navArgs<AddNewTaskFragmentArgs>()
    private var currentTask: Task? = null
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentTask = args.currentTask
        Log.d("AddNewTaskFragmentTAG", "Current Task: $currentTask")
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewTaskBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]


        if (currentTask != null) {
            binding.etTitle.setText(currentTask?.title)
            binding.etDesc.setText(currentTask?.description)
            isEditing = true
            binding.addNewTask.text = "Update Task"
        } else {
            isEditing = false
            binding.addNewTask.text = "Add New Task"
        }

        binding.addNewTask.setOnClickListener {
            addNewTask()
        }
        return binding.root
    }

    private fun addNewTask() {
        val title = binding.etTitle.text.toString()
        val description = binding.etDesc.text.toString()

        if (validateInput(title, description)) {
            if (isEditing) {
                val updatedTask = Task(
                    currentTask?.id!!,
                    title,
                    description,
                    currentTask?.isCompleted!!
                )
                viewModel.updateTask(updatedTask)
                Toast.makeText(requireContext(), "Task Updated", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addNewTaskFragment_to_allTasksFragment)
            } else {
                viewModel.addTask(
                    Task(
                        0,
                        title,
                        description,
                        false
                    )
                )
                Toast.makeText(requireContext(), "Task Added", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addNewTaskFragment_to_allTasksFragment)
            }
        } else
            Toast.makeText(
                requireContext(),
                "Please enter title and description",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun validateInput(title: String, description: String): Boolean =
        !(TextUtils.isEmpty(title) && TextUtils.isEmpty(description))
}