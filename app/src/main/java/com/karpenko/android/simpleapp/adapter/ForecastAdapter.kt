package com.karpenko.android.simpleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.karpenko.android.simpleapp.R
import com.karpenko.android.simpleapp.adapter.ForecastAdapter.DayViewHolder
import com.karpenko.android.simpleapp.databinding.ItemForecastBinding
import com.karpenko.android.model.Forecast
import com.bumptech.glide.Glide

class ForecastAdapter(
    private val onClick: (Int, ForecastAdapter) -> (Unit)
) : RecyclerView.Adapter<DayViewHolder>() {

    var dataList = listOf<Forecast>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var decoration: DividerItemDecoration? = null

    private val _onClick: (Int) -> (Unit) = { onClick(it, this) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DayViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), _onClick
        )

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val decor = decoration
            ?: DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL).also {
                ResourcesCompat.getDrawable(
                    recyclerView.context.resources,
                    R.drawable.divider,
                    recyclerView.context.theme
                )?.let { drw ->
                    it.setDrawable(drw)
                }
            }
        recyclerView.addItemDecoration(decor)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        decoration?.let { recyclerView.removeItemDecoration(it) }
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) =
        holder.bind(dataList[position])

    override fun getItemCount() = dataList.size

    class DayViewHolder(
        private val binding: ItemForecastBinding,
        private val onClick: (Int) -> (Unit)
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Forecast) {
            with(binding) {
                Glide.with(root)
                    .load(data.imageSrc)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .onlyRetrieveFromCache(true)
                    .into(iv)
                tvTitle.text = binding.root.context.getString(
                    R.string.item_forecast_day_description_label,
                    data.day,
                    data.description
                )
                root.setOnClickListener {
                    onClick(data.day)
                }
            }
        }
    }
}