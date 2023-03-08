package com.example.netflixClone.data.remote.network

import com.example.netflixClone.data.local.database.Category
import com.example.netflixClone.data.local.database.Movie
import com.google.gson.annotations.SerializedName

data class NetworkMovie(
    val title: String,
    val imageUrl: String,
    val percentFinished: Float = 0f,
    val isNetflixOnly: Boolean = false,
    val categories: List<NetworkCategory> = emptyList()
)

fun List<NetworkMovie>.toCategoryMap(): Map<Category, List<Movie>> {
    val result = mutableMapOf<Category, List<Movie>>()
    // Go through movies
    // If category keys don't exist create them with current movie
    // else add current movie to category keys
    this.forEach { movie: NetworkMovie ->
        val localMovie = movie.toLocalMovie()
        movie.categories.forEach {
            val localCategory = it.toLocalCategory()
            if (result.keys.contains(localCategory)) {
                val currentMovies = result[localCategory]!!
                result[localCategory] = currentMovies + listOf(localMovie)
            } else {
                result[localCategory] = listOf(localMovie)
            }
        }
    }

    return result
}

fun List<NetworkMovie>.getMoviesWithoutCategory(): List<Movie> {
    val result = mutableListOf<Movie>()
    this.forEach {
        if (it.categories.isEmpty()) {
            result.add(it.toLocalMovie())
        }
    }
    return result
}

fun NetworkMovie.toLocalMovie(): Movie {
    return Movie(
        movieTitle = this.title,
        imageUrl = this.imageUrl,
        percentFinished = this.percentFinished,
        isNetflixOnly = this.isNetflixOnly
    )
}

enum class NetworkCategory(val value: String) {

    @SerializedName("becauseYouWatched")
    BecauseYouWatched("becauseYouWatched"),

    @SerializedName("trendingNow")
    TrendingNow("trendingNow"),

    @SerializedName("popularOnNetflix")
    PopularOnNetflix("popularOnNetflix"),

    @SerializedName("newReleases")
    NewReleases("newReleases"),

    @SerializedName("documentaries")
    Documentaries("documentaries"),

    @SerializedName("casualViewing")
    CasualViewing("casualViewing"),

    @SerializedName("movies")
    Movies("movies"),

    @SerializedName("tvShows")
    TvShows("tvShows")


    //Continue Watching & Netflix Only will be calculated based on Movie properties.
}

fun NetworkCategory.toLocalCategory(): Category {
    return Category(this.value)
}