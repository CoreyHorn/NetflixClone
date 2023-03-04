package com.example.netflixClone.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflixClone.data.MovieRepository
import com.example.netflixClone.data.local.database.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state.asStateFlow().onStart {
        viewModelScope.launch { movieRepository.movies }
    }

}

sealed interface MainState {
    object Loading: MainState
    data class Error(val throwable: Throwable) : MainState
    data class Success(val data: List<Movie>) : MainState

}