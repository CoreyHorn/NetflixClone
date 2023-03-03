package com.example.netflixClone.data.remote.network

import com.example.netflixClone.data.local.database.Movie

data class NetworkMovie(
    val title: String,
    val imageUrl: String,
    val percentFinished: Float = 0f,
    val isNetflix: Boolean = false,
    val isTopTen: Boolean = false
)

fun NetworkMovie.toLocalMovie(): Movie {
    return Movie(
        this.title,
        this.imageUrl,
        this.percentFinished,
        this.isNetflix,
        this.isTopTen
    )
}