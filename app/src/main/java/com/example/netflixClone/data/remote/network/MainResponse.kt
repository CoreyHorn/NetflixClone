package com.example.netflixClone.data.remote.network

import android.util.Log
import com.example.netflixClone.data.local.database.LocalMovie
import com.example.netflixClone.data.local.database.Movie
import com.google.gson.annotations.SerializedName

data class NetworkMovie(
    val title: String,
    val imageUrl: String,
    val percentFinished: Float = 0f,
    val isNetflixOnly: Boolean = false,
    val categories: List<Category> = emptyList()
)

fun NetworkMovie.toLocalMovie(): Movie {
    Log.d("stuff", "should be attempting to convert to local movie")
    return Movie(
        this.title,
        this.imageUrl,
        this.percentFinished,
        this.isNetflixOnly
    )
}

fun NetworkMovie.toLocalMovieNew(): LocalMovie {
    return LocalMovie(
        title = this.title,
        imageUrl = this.imageUrl,
        percentFinished = this.percentFinished,
        isNetflixOnly = this.isNetflixOnly
    )
}

enum class Category(val value: String) {

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