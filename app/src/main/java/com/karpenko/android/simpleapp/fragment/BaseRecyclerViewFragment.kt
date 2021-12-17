package com.karpenko.android.simpleapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import com.karpenko.android.simpleapp.adapter.ForecastAdapter
import com.karpenko.android.simpleapp.collectWhenStarted
import com.karpenko.android.simpleapp.databinding.FragmentRecyclerViewBinding
import com.karpenko.android.simpleapp.viewmodel.ForecastViewModel
import com.karpenko.android.model.FragmentType
import com.karpenko.android.model.Result
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val UPCOMING_FRAGMENT_POSITION = 0
private const val FRAGMENT_TYPE = "fragment_type"

fun getRecyclerViewFragmentInstance(type: FragmentType) =
    RecyclerViewFragment().apply {
        arguments = Bundle().apply {
            putSerializable(FRAGMENT_TYPE, type)
        }
    }

class RecyclerViewFragment : BaseFragment<FragmentRecyclerViewBinding>() {

    private val viewModel by sharedViewModel<ForecastViewModel>()

    override fun inflateProvider(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRecyclerViewBinding.inflate(inflater, container, false)

    @SuppressLint("NotifyDataSetChanged")
    private val adapter = ForecastAdapter { id, adp ->
        val frg = getDetailsFragmentInstance(id)
        frg.show(parentFragmentManager, DETAILS_SCREEN_TAG)
        frg.onDownloadTriggered = {
            adp.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        binding.refresher.setOnRefreshListener {
            viewModel.viewModelScope.launch {
                when (val result = viewModel.refresh()) {
                    is Result.Success -> binding.refresher.isRefreshing = false
                    is Result.Error -> {
                        binding.refresher.isRefreshing = false
                        showError(result.error.message)
                    }
                }
            }
        }

        collectWhenStarted(viewModel.viewState) {
            binding.refresher.isRefreshing = false
            when (it) {
                is ForecastViewModel.ViewState.Loading -> binding.refresher.isRefreshing = true
                is ForecastViewModel.ViewState.Loaded -> {
                    adapter.dataList =
                        when (requireArguments().getSerializable(FRAGMENT_TYPE) as FragmentType) {
                            FragmentType.HOTTEST -> it.data.filter { day -> day.chanceRain < 0.5 }
                                .sortedBy { day -> day.high }.reversed()
                            FragmentType.UPCOMING -> it.data.sortedBy { day -> day.day }
                        }
                }
                is ForecastViewModel.ViewState.Error -> showError(it.msg)
            }
        }
    }

    private fun showError(message: String) {
        getErrorFragmentInstance(message).show(parentFragmentManager)
    }
}