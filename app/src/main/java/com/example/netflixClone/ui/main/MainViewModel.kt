package com.example.netflixClone.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflixClone.data.local.database.Movie
import com.example.netflixClone.data.remote.network.CategoryResponse
import com.example.netflixClone.data.remote.network.MovieApi
import com.example.netflixClone.data.remote.network.toLocalMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("FakeMovieService") movieService: MovieApi
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state.asStateFlow().onStart {
        viewModelScope.launch {
            val mainResponse = movieService.getMain()
            if (mainResponse.isSuccessful) {
                _state.value = MainState.Success(mainResponse.body()?.categories!!, mainResponse.body()?.header?.toLocalMovie())
            } else { _state.value = MainState.Error(null, _state.value.categories, _state.value.headerMovie) }
        }
    }
}

sealed interface MainState {
    val categories: List<CategoryResponse>?
    val headerMovie: Movie?

    object Loading : MainState {
        override val categories: List<CategoryResponse>? = null
        override val headerMovie: Movie? = null
    }

    data class Error(
        val throwable: Throwable?,
        override val categories: List<CategoryResponse>? = null,
        override val headerMovie: Movie? = null
    ) : MainState

    data class Success(override val categories: List<CategoryResponse>?, override val headerMovie: Movie?) :
        MainState

}