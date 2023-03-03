package com.example.netflixClone.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.netflixClone.data.di.fakeMovies
import com.example.netflixClone.data.local.database.Movie
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainView(statusBarHeight: MutableState<Int>) {
    val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var currentlySelectedMovie by remember { mutableStateOf(fakeMovies.first()) }

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(sheetState = bottomSheetState, sheetContent = {
        MovieDetailBottomSheet(currentlySelectedMovie) {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }
    }) {
        ContentView(statusBarHeight) {
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
}

@Composable
private fun ContentView(topPadding: MutableState<Int>, onMovieClick: (Movie) -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        LazyColumn(Modifier.background(Color.Black)) {
            item {
                MovieHeader()
            }
            nestedCategoryList(onMovieClick)
        }
        AppBar(topPadding)
        BottomBar(Modifier.align(Alignment.BottomCenter))
    }
}

@Preview()
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HorizontalMovieList(movies: List<Movie> = fakeMovies, onMovieClick: (Movie) -> Unit = {}) {
    LazyRow() {
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
        text, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, modifier = Modifier.padding(8.dp)
    )
}

@Preview()
@Composable
fun CategoryList(headerItem: Unit = MovieHeader(), onMovieClick: (Movie) -> Unit = {}) {
    LazyColumn(
        Modifier.background(Color.Black)
    ) {
        item {
            headerItem
        }
        items(20) {
            MovieListTitle()
            HorizontalMovieList(fakeMovies, onMovieClick)
        }
    }
}

fun LazyListScope.nestedCategoryList(onMovieClick: (Movie) -> Unit) {
    items(10) {
        MovieListTitle()
        HorizontalMovieList(fakeMovies, onMovieClick)
    }
}