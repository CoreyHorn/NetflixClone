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

package com.example.netflixClone.data

import com.example.netflixClone.data.local.database.Movie
import com.example.netflixClone.data.local.database.MovieDao
import com.example.netflixClone.data.remote.network.MovieApi
import com.example.netflixClone.data.remote.network.MovieResponse
import com.example.netflixClone.data.remote.network.toLocalMovie
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

interface MovieRepository {
    val movies: Flow<List<Movie>>

    suspend fun add(title: String, imageUrl: String)
    suspend fun fetchMovies(): Response<List<MovieResponse>>
    suspend fun fetchHeaderMovie(): Response<MovieResponse>
}

class DefaultMovieRepository @Inject constructor(
    private val movieDao: MovieDao, @Named("FakeMovieService") private val movieService: MovieApi
) : MovieRepository {

    override val movies: Flow<List<Movie>> = movieDao.getMovies()

    override suspend fun add(title: String, imageUrl: String) {
        movieDao.insertMovie(Movie(title, imageUrl))
    }

    override suspend fun fetchMovies(): Response<List<MovieResponse>> {
        val response = movieService.getMovies()

        // Cache movies locally if none exist
        if (response.isSuccessful) {
            coroutineScope {
                movieDao.getMovies().collect { cachedMovies ->
                    if (cachedMovies.isEmpty())
                        movieDao.insertMovies(response.body()?.map { it.toLocalMovie() }!!)

                }
            }
        }

        return response
    }

    override suspend fun fetchHeaderMovie(): Response<MovieResponse> {
        return movieService.getHeaderMovie()
    }
}
