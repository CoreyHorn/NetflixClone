package com.example.netflixClone.data.remote.network

import com.example.netflixClone.data.local.database.Movie

data class MainResponse(
    val header: MovieResponse,
    val categories: List<CategoryResponse>
)

data class CategoryResponse(
    val title: String,
    val movies: List<MovieResponse>
)

data class MovieResponse(
    val title: String,
    val imageUrl: String,
    val percentFinished: Float = 0f,
    val isNetflix: Boolean = false,
    val isTopTen: Boolean = false
)

fun MovieResponse.toLocalMovie(): Movie {
    return Movie(
        this.title,
        this.imageUrl,
        this.percentFinished,
        this.isNetflix,
        this.isTopTen
    )
}