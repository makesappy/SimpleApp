package com.karpenko.android.simpleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var addedToBackStack = false

    private var _binding: VB? = null
    protected val binding: VB get() = checkNotNull(_binding)

    protected abstract fun inflateProvider(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateProvider(inflater, container)
        return binding.root
    }

    fun show(
        manager: FragmentManager,
        tag: String? = null,
        containerResId: Int = android.R.id.content,
        addToBackStack: Boolean = true
    ) {
        manager.commit {
            // Check if is not already displayed
            if (manager.findFragmentByTag(tag) == null) {
                if (addToBackStack) {
                    addToBackStack(tag)
                    replace(containerResId, this@BaseFragment, tag)
                    addedToBackStack = true
                } else {
                    add(containerResId, this@BaseFragment, tag)
                }
            }
        }
    }

    fun dismiss() {
        if (addedToBackStack) {
            parentFragmentManager.popBackStack()
            addedToBackStack = false
        } else {
            parentFragmentManager.commit {
                remove(this@BaseFragment)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}