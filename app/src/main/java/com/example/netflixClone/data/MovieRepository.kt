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

import com.example.netflixClone.data.local.database.*
import com.example.netflixClone.data.remote.network.MovieApi
import com.example.netflixClone.data.remote.network.NetworkMovie
import com.example.netflixClone.data.remote.network.getMoviesWithoutCategory
import com.example.netflixClone.data.remote.network.toCategoryMap
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

interface MovieRepository {
    val movies: Flow<List<CategoryWithMovies>>
    val headerMovie: Flow<Movie?>
    suspend fun fetchMovies(): Response<List<NetworkMovie>>
}

class DefaultMovieRepository @Inject constructor(
    @Named("FakeMovieService") private val movieService: MovieApi, private val movieDao: MovieDao
) : MovieRepository {

    // Combine the default categories with property based filters
    override val movies: Flow<List<CategoryWithMovies>> = combineTransform(
        movieDao.getCategoriesWithMovies(),
        movieDao.getNetflixExclusives(),
        movieDao.getInProgress()
    ) { categories: List<CategoryWithMovies>, exclusives: List<Movie>, inProgress: List<Movie> ->
        val exclusiveCategory =
            CategoryWithMovies(Category(categoryTitle = "isNetflixOnly"), exclusives)
        val inProgressCategory =
            CategoryWithMovies(Category(categoryTitle = "inProgress"), inProgress)
        emit(listOf(inProgressCategory, exclusiveCategory) + categories)
    }

    override val headerMovie: Flow<Movie?> = movieDao.getRandomMovie()

    override suspend fun fetchMovies(): Response<List<NetworkMovie>> {
        val response = movieService.getMovies()

        // Cache movies locally if none exist
        if (response.isSuccessful) {
            coroutineScope {
                if (movieDao.getCategoriesWithMovies().firstOrNull() != null) {
                    movieDao.insertCategoriesWithMovies(response.body()!!.toCategoryMap())
                    movieDao.insertMovies(response.body()!!.getMoviesWithoutCategory())
                }
            }
        }

        return response
    }
}
