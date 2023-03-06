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

import android.util.Log
import com.example.netflixClone.data.local.database.*
import com.example.netflixClone.data.remote.network.MovieApi
import com.example.netflixClone.data.remote.network.NetworkMovie
import com.example.netflixClone.data.remote.network.toLocalMovieNew
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

interface MovieRepository {
    val movies: Flow<List<Movie>>

    suspend fun add(title: String, imageUrl: String)

    suspend fun fetchMovies(): Response<List<NetworkMovie>>

    suspend fun getMoviesByCategory(): Flow<List<LocalCategoryWithMovies>>

    suspend fun getHeader(): Movie
}

class DefaultMovieRepository @Inject constructor(
    private val movieDao: MovieDao, @Named("FakeMovieService") private val movieService: MovieApi, private val localMovieDao: LocalMovieDao
) : MovieRepository {

    override val movies: Flow<List<Movie>> = movieDao.getMovies()

    override suspend fun add(title: String, imageUrl: String) {
        movieDao.insertMovie(Movie(title, imageUrl))
    }

    override suspend fun fetchMovies(): Response<List<NetworkMovie>> {
        val response = movieService.getMovies()

        // Cache movies locally if none exist
        if (response.isSuccessful) {

            coroutineScope {
                        val networkMovies = response.body()!!
                        networkMovies.forEach { movie ->
                            Log.d("stuff2", "Adding movie: " + movie)
                            movie.categories.forEach {
                                localMovieDao.insert(LocalCategory(title = it.value), listOf(movie.toLocalMovieNew()))
                            }
                        }
                    }
                }



        return response
    }

    override suspend fun getMoviesByCategory(): Flow<List<LocalCategoryWithMovies>> {
        return localMovieDao.getCategoriesWithMovies()
//        return combineTransform(
//            movieDao.getCategoriesWithMovies(),
//            movieDao.getNetflixExclusives(),
//            movieDao.getInProgress()
//        ) { categories: List<CategoryWithMovies>, exclusives: List<Movie>, inProgress: List<Movie> ->
//            val exclusiveCategory = CategoryWithMovies(Category(categoryTitle = "isNetflixOnly"), exclusives)
//            val inProgressCategory = CategoryWithMovies(Category(categoryTitle = "inProgress"), inProgress)
//            emit(listOf(inProgressCategory, exclusiveCategory) + categories)
//        }
    }

    override suspend fun getHeader(): Movie {
        return movieDao.getRandomMovie()
    }
}
