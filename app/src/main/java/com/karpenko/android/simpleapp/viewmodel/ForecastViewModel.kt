package com.karpenko.android.simpleapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karpenko.android.model.Forecast
import com.karpenko.android.model.Result
import com.karpenko.android.usecase.FetchForecastUseCase
import com.karpenko.android.usecase.ObserveForecastUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val observeForecastUseCase: ObserveForecastUseCase,
    private val fetchForecastUseCase: FetchForecastUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: StateFlow<ViewState> = _viewState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchForecastUseCase()
            observeForecastUseCase().collect {
                when (it) {
                    is Result.Success -> _viewState.value = ViewState.Loaded(it.data)
                    is Result.Error -> _viewState.value = ViewState.Error(it.error.message)
                }
            }
        }
    }

    suspend fun refresh() = fetchForecastUseCase()

    /**
     * Represents all common view states
     */
    sealed class ViewState {
        /**
         * Always initial state, followed by [Loaded] or [Error]
         */
        object Loading : ViewState()

        /**
         * Terminal state with obtained [data]
         */
        data class Loaded(val data: List<Forecast>) : ViewState()

        /**
         * Error state with error [msg] to show in popup
         */
        data class Error(val msg: String) : ViewState()
    }
}