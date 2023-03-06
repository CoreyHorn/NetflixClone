package com.example.netflixClone.data.remote.network

import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {

    @GET("/main")
    suspend fun getMain(): Response<MainResponse>

    @GET("/movies")
    suspend fun getMovies(): Response<List<MovieResponse>>

    @GET("/headerMovie")
    suspend fun getHeaderMovie(): Response<MovieResponse>
}

class FakeMovieService : MovieApi {

    override suspend fun getMain(): Response<MainResponse> {
        return Response.success(fakeMainResponse)
    }

    override suspend fun getMovies(): Response<List<MovieResponse>> {
        return Response.success(fakeMovieResponses)
    }

    override suspend fun getHeaderMovie(): Response<MovieResponse> {
        return Response.success(fakeHeaderMovie)
    }
}

val fakeHeaderMovie = MovieResponse(
    "2 Guns",
    "https://m.media-amazon.com/images/M/MV5BY2JhZTRlYzYtZmI1OS00NTRhLWFjNGYtNzI1ODJmNmZhZGU1XkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_Ratio1.0000_AL_.jpg"
)

val fakeMovieResponses = listOf(
    MovieResponse(
        "Star Trek Picard",
        "https://image.tmdb.org/t/p/original/nIlAKIrLKxOeoEnc0Urb65yNCp.jpg"
    ),
    MovieResponse(
        "Ant-Man and the Wasp: Quantumania",
        "https://image.tmdb.org/t/p/original/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg"
    ),
    MovieResponse(
        "Carnival Row",
        "https://image.tmdb.org/t/p/original/jyhxT10e2z9IDsKoIQDKhyxSQJt.jpg"
    ),
    MovieResponse(
        "Outer Banks",
        "https://image.tmdb.org/t/p/original/ovDgO2LPfwdVRfvScAqo9aMiIW.jpg"
    ),
    MovieResponse(
        "The Consultant",
        "https://image.tmdb.org/t/p/original/thqmhjLaMf2n0Ebl7oLRjH6VD15.jpg"
    ),
    MovieResponse(
        "The Last of Us",
        "https://image.tmdb.org/t/p/original/uKvVjHNqB5VmOrdxqAt2F7J78ED.jpg"
    ),
    MovieResponse(
        "Knock at the Cabin",
        "https://image.tmdb.org/t/p/original/dm06L9pxDOL9jNSK4Cb6y139rrG.jpg"
    ),
    MovieResponse(
        "80 for Brady",
        "https://image.tmdb.org/t/p/original/jixBLmH4gQuTKTenZr89egvqZbW.jpg"
    )
)

val fakeCategories = listOf(
    CategoryResponse(
        "Because you watched...",
        fakeMovieResponses.shuffled()
    ),
    CategoryResponse(
        "Continue Watching for Corey",
        fakeMovieResponses.shuffled()
    ),
    CategoryResponse(
        "Trending Now",
        fakeMovieResponses.shuffled()
    ),
    CategoryResponse(
        "New Releases",
        fakeMovieResponses.shuffled()
    ),
    CategoryResponse(
        "Only on Netflix",
        fakeMovieResponses.shuffled()
    )
)

val fakeMainResponse = MainResponse(
    fakeHeaderMovie,
    fakeCategories
)