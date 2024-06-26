package com.example.roomtodo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomtodo.databinding.FragmentAllTasksBinding
import com.example.roomtodo.ui.viewmodel.TaskViewModel


class AllTasksFragment : Fragment() {
    private lateinit var binding: FragmentAllTasksBinding
    private lateinit var viewModel: TaskViewModel
    private val tasksAdapter by lazy { TasksAdapter() }
    private var liveFilter = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            val action = AllTasksFragmentDirections.actionAllTasksFragmentToAddNewTaskFragment(null)
            findNavController().navigate(action)
        }
        initRecyclerView()
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.allTasks.observe(viewLifecycleOwner) {
            tasksAdapter.setTask(it)
        }
        binding.filterTasks.setOnClickListener {
            liveFilter = !liveFilter
            if (liveFilter) {
                viewModel.allCompletedTasks.observe(viewLifecycleOwner) {
                    tasksAdapter.setTask(it)
                }
            } else {
                viewModel.allTasks.observe(viewLifecycleOwner) {
                    tasksAdapter.setTask(it)
                }
            }
        }

        tasksAdapter.onItemClicked = {
            viewModel.completeTask(it)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteTask(tasksAdapter.getTask(position))
            }
        }
        ).attachToRecyclerView(binding.recyclerView)

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tasksAdapter
        }
    }
}