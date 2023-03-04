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

package com.example.netflixClone.data.di

import com.example.netflixClone.data.DefaultMovieRepository
import com.example.netflixClone.data.MovieRepository
import com.example.netflixClone.data.local.database.Movie
import com.example.netflixClone.data.remote.network.NetworkMovie
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsMovieRepository(
        movieRepository: DefaultMovieRepository
    ): MovieRepository
}

class FakeMovieRepository @Inject constructor() : MovieRepository {
    override val movies: Flow<List<Movie>> = flowOf(fakeMovies)

    override suspend fun add(title: String, imageUrl: String) {
        throw NotImplementedError()
    }

    override suspend fun fetchMovies(): Response<List<NetworkMovie>> {
        throw NotImplementedError()
    }
}

val fakeMovies = listOf(
    Movie("Star Trek Picard", "https://image.tmdb.org/t/p/original/nIlAKIrLKxOeoEnc0Urb65yNCp.jpg"),
    Movie("Ant-Man and the Wasp: Quantumania", "https://image.tmdb.org/t/p/original/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg"),
    Movie("Carnival Row", "https://image.tmdb.org/t/p/original/jyhxT10e2z9IDsKoIQDKhyxSQJt.jpg"),
    Movie("Outer Banks", "https://image.tmdb.org/t/p/original/ovDgO2LPfwdVRfvScAqo9aMiIW.jpg"),
    Movie("The Consultant", "https://image.tmdb.org/t/p/original/thqmhjLaMf2n0Ebl7oLRjH6VD15.jpg"),
    Movie("The Last of Us", "https://image.tmdb.org/t/p/original/uKvVjHNqB5VmOrdxqAt2F7J78ED.jpg"),
    Movie("Knock at the Cabin", "https://image.tmdb.org/t/p/original/dm06L9pxDOL9jNSK4Cb6y139rrG.jpg"),
    Movie("80 for Brady", "https://image.tmdb.org/t/p/original/jixBLmH4gQuTKTenZr89egvqZbW.jpg")
)
