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

package com.example.netflixClone.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class Movie(
    @PrimaryKey
    val title: String,
    val imageUrl: String,
    val percentFinished: Float = 0f,
    val isNetflix: Boolean = false,
    val isTopTen: Boolean = false
)

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie ORDER BY title DESC LIMIT 10")
    fun getMovies(): Flow<List<Movie>>

    @Insert
    suspend fun insertMovie(item: Movie)

    @Insert
    suspend fun insertMovies(movies: List<Movie>)
}
