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

package com.example.netflixClone.ui.main

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.netflixClone.R
import com.example.netflixClone.data.di.fakeMovies
import com.example.netflixClone.data.local.database.Movie
import dagger.hilt.android.AndroidEntryPoint
import com.example.netflixClone.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Stores the status bar height. Necessary because we are drawing under the status bar
     * but want the AppBar directly below where the status bar normally ends.
     */
    private var statusBarHeight: MutableState<Int> = mutableStateOf(0)

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
                var currentlySelectedMovie by remember { mutableStateOf(fakeMovies.first()) }

                val coroutineScope = rememberCoroutineScope()

                ModalBottomSheetLayout(sheetState = bottomSheetState, sheetContent = { MovieDetailBottomSheet(currentlySelectedMovie) }) {
                    RootView(statusBarHeight) {
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
        }

        val systemUiController =

        configureSystemBars(this, window.decorView.findViewById(android.R.id.content))
    }

    private fun configureSystemBars(activity: Activity, view: View) {
        activity.apply {

            // Tells system this window will be responsible for drawing the system bar backgrounds
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.bottom_bar_gray)

            WindowCompat.setDecorFitsSystemWindows(window, false)

            ViewCompat.setOnApplyWindowInsetsListener(view) { root, windowInset ->
                val inset = windowInset.getInsets(WindowInsetsCompat.Type.systemBars())
                root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    leftMargin = inset.left
                    bottomMargin = inset.bottom
                    rightMargin = inset.right

                    // Tells our view to start at the top of the screen instead of under the status bar.
                    topMargin = 0

                    // Stores the status bar height for aligning views
                    statusBarHeight.value = inset.top
                }
                WindowInsetsCompat.CONSUMED
            }
        }
    }
}

@Composable
fun RootView(topPadding: MutableState<Int>, onMovieClick: (Movie) -> Unit = {}) {
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


