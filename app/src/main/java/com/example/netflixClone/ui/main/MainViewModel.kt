package com.example.netflixClone.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflixClone.data.MovieRepository
import com.example.netflixClone.data.local.database.Movie
import com.example.netflixClone.ui.main.MainState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state.asStateFlow().onStart {
            viewModelScope.launch { movieRepository.fetchMovies() }
        }

    init {
        viewModelScope.launch {
            movieRepository.movies.map(::Success)
                .catch { MainState.Error(it) }
                .collect { result ->
                    _state.update { result }
                }
        }
    }

}

sealed interface MainState {
    object Loading : MainState
    data class Error(val throwable: Throwable) : MainState
    data class Success(val data: List<Movie>) : MainState

}