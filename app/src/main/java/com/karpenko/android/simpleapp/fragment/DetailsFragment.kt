package com.karpenko.android.simpleapp.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.karpenko.android.simpleapp.R
import com.karpenko.android.simpleapp.adapter.ForecastAdapter
import com.karpenko.android.simpleapp.collectWhenStarted
import com.karpenko.android.simpleapp.databinding.FragmentDetailsBinding
import com.karpenko.android.simpleapp.viewmodel.ForecastViewModel
import com.karpenko.android.model.Forecast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.bumptech.glide.request.target.Target

const val DETAILS_SCREEN_TAG = "details_screen"
private const val DAY_NUMBER = "day_number"

fun getDetailsFragmentInstance(id: Int) =
    DetailsFragment().apply {
        arguments = Bundle().apply {
            putInt(DAY_NUMBER, id)
        }
    }

class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    private val viewModel by sharedViewModel<ForecastViewModel>()

    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    /**
     * Callback that invokes master screen [ForecastAdapter.notifyDataSetChanged] in order to invalidate just downloaded pictures
     */
    var onDownloadTriggered: (() -> (Unit))? = null
    private val placeHolder by lazy {
        ResourcesCompat.getDrawable(
            binding.root.resources,
            R.drawable.ic_launcher_background,
            binding.root.context.theme
        )
    }

    private var shouldShowErrorFragment = false

    override fun inflateProvider(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectWhenStarted(viewModel.viewState) {
            if (it !is ForecastViewModel.ViewState.Loaded) return@collectWhenStarted

            it.data.find { day -> day.day == requireArguments().getInt(DAY_NUMBER) }?.let { day ->
                updateUi(day)
            } ?: Toast.makeText(
                requireContext(),
                "No day with given day number found",
                Toast.LENGTH_SHORT
            ).show().also {
                dismiss()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(day: Forecast) {
        with(binding) {
            toolbar.title = getString(R.string.fragment_details_toolbar_label, day.day)
            tvDesc.text = day.description
            tvSunrise.text = LocalTime.ofSecondOfDay(day.sunrise)
                .format(formatter)
            tvSunset.text = LocalTime.ofSecondOfDay(day.sunset)
                .format(formatter)
            tvRain.text = DecimalFormat("#.#%").format(day.chanceRain)
            tvHigh.text = "${day.high} °C"
            tvLow.text = "${day.low} °C"

            loadImage(day.imageSrc, true)

            btnDownload.setOnClickListener {
                shouldShowErrorFragment = true
                loadImage(day.imageSrc, false)
            }
        }
    }

    private fun loadImage(src: String, fromCache: Boolean) {
        with(binding) {
            Glide.with(root)
                .load(src)
                .placeholder(placeHolder)
                .error(placeHolder)
                .checkBtnDownloadVisibility()
                .onlyRetrieveFromCache(fromCache)
                .into(iv)
        }
    }

    private fun RequestBuilder<Drawable>.checkBtnDownloadVisibility(): RequestBuilder<Drawable> {
        return this.listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                binding.btnDownload.isVisible = true
                if (shouldShowErrorFragment) {
                    getErrorFragmentInstance("Image load failed \nCheck your internet connection...").show(
                        parentFragmentManager
                    )
                    shouldShowErrorFragment = false
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.btnDownload.isVisible = false
                onDownloadTriggered?.invoke()
                return false
            }
        })
    }
}