package com.example.locationproject.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationproject.MainActivity
import com.example.locationproject.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var viewBinding: FragmentHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyViewModel = HistoryViewModel((activity as MainActivity).getDatabase())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHistoryBinding.inflate(layoutInflater)
        initLayoutList()
        initObservable()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.fetchListLocation()
    }

    private fun initLayoutList(){
        historyAdapter = HistoryAdapter()
        viewBinding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun initObservable() {
        historyViewModel.locationLiveData.observe(viewLifecycleOwner) {
            historyAdapter.setListLocation(it)
        }
    }
}