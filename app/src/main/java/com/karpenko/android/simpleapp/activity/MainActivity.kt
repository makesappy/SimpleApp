package com.karpenko.android.simpleapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.karpenko.android.simpleapp.R
import com.karpenko.android.simpleapp.adapter.FragmentsAdapter
import com.karpenko.android.simpleapp.databinding.ActivityMainBinding
import com.karpenko.android.simpleapp.fragment.UPCOMING_FRAGMENT_POSITION
import com.karpenko.android.simpleapp.viewmodel.ForecastViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = checkNotNull(_binding)
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private val viewModel by viewModel<ForecastViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init tabs for pager
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = if (pos == UPCOMING_FRAGMENT_POSITION) {
                getString(R.string.fragment_upcoming_label)
            } else {
                getString(R.string.fragment_hottest_label)
            }
        }
        binding.viewPager.adapter = FragmentsAdapter(this)
        tabLayoutMediator.attach()

        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                if (it is ForecastViewModel.ViewState.Loaded) {
                    it.data.find { day -> day.day == 1 }?.let { day ->
                        binding.toolbar.title =
                            getString(R.string.activity_main_toolbar_label, day.description)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
        _binding = null
    }
}
