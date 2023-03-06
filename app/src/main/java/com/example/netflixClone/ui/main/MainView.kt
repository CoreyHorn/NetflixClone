package com.example.netflixClone.ui.main

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.netflixClone.data.di.fakeMovies
import com.example.netflixClone.data.local.database.Movie
import com.example.netflixClone.data.remote.network.CategoryResponse
import com.example.netflixClone.data.remote.network.toLocalMovie
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(
    viewModel: MainViewModel = hiltViewModel(),
    statusBarHeight: MutableState<Int> = mutableStateOf(107)
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState<MainState>(
        initialValue = MainState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.state.collect {
                Log.d("stuff", it.toString())
                value = it
            }
        }
    }


    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var currentlySelectedMovie by remember { mutableStateOf(fakeMovies.first()) }

    val coroutineScope = rememberCoroutineScope()

    if (state is MainState.Success) {
        ModalBottomSheetLayout(sheetState = bottomSheetState, sheetContent = {
            MovieDetailBottomSheet(currentlySelectedMovie) {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }
        }) {
            ContentView(statusBarHeight, state.headerMovie, state.categories?: emptyList()) {
                val isNewMovie = it != currentlySelectedMovie
                currentlySelectedMovie = it

                val transform = if (!isNewMovie && bottomSheetState.isVisible) {
                    suspend { bottomSheetState.hide() }
                } else {
                    suspend { bottomSheetState.show() }
                }

                coroutineScope.launch { transform() }
            }
        }
    } else Text("Loading or Error") // TODO: Could create a good loading state or error handling


}

@Composable
private fun ContentView(topPadding: MutableState<Int>, headerMovie: Movie?, categories: List<CategoryResponse>, onMovieClick: (Movie) -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        LazyColumn(Modifier.background(Color.Black)) {
            item {
                MovieHeader(headerMovie)
            }
            nestedCategoryList(categories, onMovieClick)
        }
        AppBar(topPadding)
        BottomBar(Modifier.align(Alignment.BottomCenter))
    }
}

@Preview()
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HorizontalMovieList(movies: List<Movie> = fakeMovies, onMovieClick: (Movie) -> Unit = {}) {
    LazyRow {
        items(movies.size) { index ->
            MovieCard(movies[index], onMovieClick)
        }
    }
}

@Preview()
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MovieListTitle(text: String = "Popular on Netflix") {
    Text(
        text,
        fontSize = 17.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White,
        modifier = Modifier.padding(8.dp)
    )
}

fun LazyListScope.nestedCategoryList(categories: List<CategoryResponse>, onMovieClick: (Movie) -> Unit) {
    items(categories) { category ->
        MovieListTitle(category.title)
        HorizontalMovieList(category.movies.map { it.toLocalMovie() }, onMovieClick)
    }
}