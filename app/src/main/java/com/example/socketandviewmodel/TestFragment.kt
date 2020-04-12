package com.example.socketandviewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class TestFragment : Fragment() {

    private lateinit var viewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(),ViewModelProvider.NewInstanceFactory()).get(ShareViewModel::class.java)
        viewModel.valueChange(mutableListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.getChangedValue().observe(viewLifecycleOwner, Observer { stocks ->
//            val stockNoList = showStocks.map { stock -> stock.values.toList()[0] }
//            changedStocks = stocks.filter { stockNoList.contains(it) }.toMutableList()
//            if (changedStocks.isNotEmpty()) {
//                changedStocks.forEach {
//                    rvStock.adapter?.notifyItemChanged(stockNoList.indexOf(it))
//                }
//            }
        }
        )


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }
}
