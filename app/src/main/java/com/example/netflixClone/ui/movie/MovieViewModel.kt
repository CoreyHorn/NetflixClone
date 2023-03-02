/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.netflixClone.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.netflixClone.data.MovieRepository
import com.example.netflixClone.data.local.database.Movie
import com.example.netflixClone.data.remote.FakeMovieService
import com.example.netflixClone.data.remote.MovieApi
import com.example.netflixClone.ui.movie.MovieUiState.Error
import com.example.netflixClone.ui.movie.MovieUiState.Loading
import com.example.netflixClone.ui.movie.MovieUiState.Success
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    @Named("FakeMovieService") private val movieApi: MovieApi
) : ViewModel() {

    val uiState: StateFlow<MovieUiState> = movieRepository
        .movies.map(::Success)
        .catch { Error(it) }
        .onStart { viewModelScope.launch { movieApi.getMovies() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addMovie(name: String) {
        viewModelScope.launch {
            movieRepository.add(name, "")
        }
    }
}

sealed interface MovieUiState {
    object Loading : MovieUiState
    data class Error(val throwable: Throwable) : MovieUiState
    data class Success(val data: List<Movie>) : MovieUiState
}
