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

package com.example.netflixClone.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.netflixClone.ui.main.MainView

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    /**
     * Stores the status bar height. Necessary because we are drawing under the status bar
     * but want the AppBar directly below where the status bar normally ends.
     */
    val statusBarHeight: MutableState<Int> = remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainView(statusBarHeight = statusBarHeight) }
        // TODO: Add more destinations
    }
}
