package com.example.netflixClone.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.netflixClone.data.MovieRepository
import com.example.netflixClone.data.local.database.Movie
import com.example.netflixClone.data.remote.network.toLocalMovie
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
        viewModelScope.launch {
            val headerMovieResponse = movieRepository.fetchHeaderMovie()
            if (headerMovieResponse.isSuccessful) {
                _state.update {
                    MainState.Success(
                        it.movies,
                        headerMovieResponse.body()!!.toLocalMovie()
                    )
                }
            } else {
                _state.update {
                    MainState.Error(null, _state.value.movies, _state.value.headerMovie)
                }
            }
        }
        viewModelScope.launch {
            movieRepository.movies.map { movies ->
                MainState.Success(movies, _state.value.headerMovie)
            }.catch {
                MainState.Error(it)
            }.collect { result ->
                _state.update { result }
            }
        }
    }
}

sealed interface MainState {
    val movies: List<Movie>?
    val headerMovie: Movie?

    object Loading : MainState {
        override val movies: List<Movie>? = null
        override val headerMovie: Movie? = null
    }

    data class Error(
        val throwable: Throwable?,
        override val movies: List<Movie>? = null,
        override val headerMovie: Movie? = null
    ) : MainState

    data class Success(override val movies: List<Movie>?, override val headerMovie: Movie?) :
        MainState

}