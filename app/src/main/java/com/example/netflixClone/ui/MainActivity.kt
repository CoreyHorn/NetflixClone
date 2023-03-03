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

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.netflixClone.R
import com.example.netflixClone.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Stores the status bar height. Necessary because we are drawing under the status bar
     * but want the AppBar directly below where the status bar normally ends.
     */
    private var statusBarHeight: MutableState<Int> = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                MainNavigation(statusBarHeight)
            }
        }

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


