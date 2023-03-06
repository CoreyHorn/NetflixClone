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

const val movieIdColumn = "movieId"
const val categoryIdColumn = "categoryId"

data class CategoryWithMovies(
    @Embedded val category: Category,
    @Relation(
        parentColumn = categoryIdColumn,
        entityColumn = movieIdColumn,
        associateBy = Junction(MovieCategoryCrossRef::class)
    )
    val movies: List<Movie>
)

data class MovieWithCategories(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = movieIdColumn,
        entityColumn = categoryIdColumn,
        associateBy = Junction(MovieCategoryCrossRef::class)
    )
    val categories: List<Category>
)

// Represents Movie and Category's many to many relationship
@Entity(indices = [Index("categoryId"), Index("movieId")], primaryKeys = ["categoryId", "movieId"])
data class MovieCategoryCrossRef @JvmOverloads constructor(
    val categoryId: Long = 0L,
    val movieId: Long = 0L
)

//@Entity(primaryKeys = ["categoryId, movieId"], indices = [Index(value = ["categoryId", "movieId"])])
//class MovieCategoryCrossRef(val categoryId: Long = 0,
//                            val movieId: Long = 0L)

@Entity
data class Category @JvmOverloads constructor(
    val categoryTitle: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = 69
}

//@Entity()
//data class Category(
//    val title: String
//) {
//    @PrimaryKey(autoGenerate = true)
//    var categoryId: Long = 0L
//}

@Entity
data class Movie @JvmOverloads constructor(
    val title: String,
    val imageUrl: String,
    val percentFinished: Float = 0f,
    val isNetflixOnly: Boolean = false,
    val isTopTen: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    var movieId: Long = 0L
) {
}

@Dao
interface MovieDao {

//    @Query(
//        "SELECT * FROM category " +
//        "JOIN movie ON category.categoryId = movie.categoryId"
//    )
//    fun getCategoriesWithMoviesTwo(): Map<Category, Movie>

    @Transaction
    @Query("SELECT * FROM Category")
    fun getCategoriesWithMovies(): Flow<List<CategoryWithMovies>>

    @Insert
    fun insertCategoryRelationship(movie: Movie, category: Category)

//    @Transaction
//    @Query("SELECT * FROM Movie")
//    fun getMoviesWithCategories(): Flow<List<MovieWithCategories>>
//
//    @Transaction
//    @Query("SELECT * FROM Movie WHERE movieId = :movieId")
//    fun getMovieWithCategories(movieId: Long): Flow<MovieWithCategories>

    @Query("SELECT * FROM movie")
    fun getMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE isNetflixOnly = true")
    fun getNetflixExclusives(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE percentFinished > 0")
    fun getInProgress(): Flow<List<Movie>>

    @Query("SELECT * FROM movie ORDER BY RANDOM() LIMIT 1")
    fun getRandomMovie(): Movie

    @Insert
    suspend fun insertMovie(item: Movie)

    @Insert
    suspend fun insertMovies(movies: List<Movie>)
}
