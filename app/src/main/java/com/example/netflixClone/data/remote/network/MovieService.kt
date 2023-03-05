package com.example.netflixClone.data.remote.network

import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {
    @GET("/movies")
    suspend fun getMovies(): Response<List<NetworkMovie>>

    @GET("/headerMovie")
    suspend fun getHeaderMovie(): Response<NetworkMovie>
}

class FakeMovieService : MovieApi {
    override suspend fun getMovies(): Response<List<NetworkMovie>> {
        return Response.success(fakeNetworkMovies)
    }

    override suspend fun getHeaderMovie(): Response<NetworkMovie> {
        return Response.success(fakeHeaderMovie)
    }
}

val fakeHeaderMovie = NetworkMovie(
    "2 Guns",
    "https://m.media-amazon.com/images/M/MV5BY2JhZTRlYzYtZmI1OS00NTRhLWFjNGYtNzI1ODJmNmZhZGU1XkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_Ratio1.0000_AL_.jpg"
)

val fakeNetworkMovies = listOf(
    NetworkMovie(
        "Star Trek Picard",
        "https://image.tmdb.org/t/p/original/nIlAKIrLKxOeoEnc0Urb65yNCp.jpg"
    ),
    NetworkMovie(
        "Ant-Man and the Wasp: Quantumania",
        "https://image.tmdb.org/t/p/original/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg"
    ),
    NetworkMovie(
        "Carnival Row",
        "https://image.tmdb.org/t/p/original/jyhxT10e2z9IDsKoIQDKhyxSQJt.jpg"
    ),
    NetworkMovie(
        "Outer Banks",
        "https://image.tmdb.org/t/p/original/ovDgO2LPfwdVRfvScAqo9aMiIW.jpg"
    ),
    NetworkMovie(
        "The Consultant",
        "https://image.tmdb.org/t/p/original/thqmhjLaMf2n0Ebl7oLRjH6VD15.jpg"
    ),
    NetworkMovie(
        "The Last of Us",
        "https://image.tmdb.org/t/p/original/uKvVjHNqB5VmOrdxqAt2F7J78ED.jpg"
    ),
    NetworkMovie(
        "Knock at the Cabin",
        "https://image.tmdb.org/t/p/original/dm06L9pxDOL9jNSK4Cb6y139rrG.jpg"
    ),
    NetworkMovie(
        "80 for Brady",
        "https://image.tmdb.org/t/p/original/jixBLmH4gQuTKTenZr89egvqZbW.jpg"
    )
)