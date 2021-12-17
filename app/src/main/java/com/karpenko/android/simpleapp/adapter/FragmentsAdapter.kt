package com.karpenko.android.simpleapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.karpenko.android.simpleapp.fragment.UPCOMING_FRAGMENT_POSITION
import com.karpenko.android.simpleapp.fragment.getRecyclerViewFragmentInstance
import com.karpenko.android.model.FragmentType

class FragmentsAdapter(act: AppCompatActivity) :
    FragmentStateAdapter(act) {
    override fun getItemCount() = FragmentType.values().size

    override fun createFragment(position: Int) =
        getRecyclerViewFragmentInstance(
            if (position == UPCOMING_FRAGMENT_POSITION) {
                FragmentType.UPCOMING
            } else {
                FragmentType.HOTTEST
            }
        )
}