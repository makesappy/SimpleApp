package com.karpenko.android.simpleapp

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 * Collects [Flow] values only within period of time, when lifecycle on [Fragment] screen is active
 */
fun <T> Fragment.collectWhenStarted(
    flow: Flow<T>,
    func: (T) -> Unit
) =
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        flow.collect {
            func(it)
        }
    }