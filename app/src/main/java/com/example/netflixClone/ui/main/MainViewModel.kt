package com.example.netflixClone.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflixClone.data.MovieRepository
import com.example.netflixClone.data.local.database.CategoryWithMovies
import com.example.netflixClone.data.local.database.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    private val dataDebounce = 200L

    /**
     * Convert back to use The repository
     */
    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    @OptIn(FlowPreview::class)
    val state = _state.asStateFlow().onStart {
        // Listen to header movie
        viewModelScope.launch {
            movieRepository.headerMovie.debounce(dataDebounce).collect { movie ->
                _state.update { MainState.Success(it.categories, movie) }
            }
        }

        // Listen to movies coming from Database
        viewModelScope.launch {
            movieRepository.movies.debounce(dataDebounce)
                .collect { categories ->
                    _state.update {
                        MainState.Success(categories, it.headerMovie)
                    }
                }
        }

        // Request movies from the network - possible error
        viewModelScope.launch {
            val fetchRequest = movieRepository.fetchMovies()
            if (!fetchRequest.isSuccessful)
                _state.update {
                    MainState.Error(Throwable(fetchRequest.errorBody().toString()), it.categories, it.headerMovie)
                }
        }
    }
}

sealed interface MainState {
    val categories: List<CategoryWithMovies>?
    val headerMovie: Movie?

    object Loading : MainState {
        override val categories: List<CategoryWithMovies>? = null
        override val headerMovie: Movie? = null
    }

    data class Error(
        val throwable: Throwable?,
        override val categories: List<CategoryWithMovies>? = null,
        override val headerMovie: Movie? = null
    ) : MainState

    data class Success(
        override val categories: List<CategoryWithMovies>?,
        override val headerMovie: Movie?
    ) :
        MainState

}