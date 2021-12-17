package com.karpenko.android.simpleapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.karpenko.android.simpleapp.databinding.FragmentErrorBinding

private const val ARG_MESSAGE = "message"

fun getErrorFragmentInstance(message: String?): ErrorFragment {
    return ErrorFragment().apply {
        arguments = Bundle().apply {
            putString(ARG_MESSAGE, message ?: "General error")
        }
    }
}

class ErrorFragment : BaseFragment<FragmentErrorBinding>() {

    override fun inflateProvider(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentErrorBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvError.text = requireArguments().getString(ARG_MESSAGE)
        binding.btnOk.setOnClickListener { dismiss() }
    }
}