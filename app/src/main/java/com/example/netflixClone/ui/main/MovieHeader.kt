package com.example.netflixClone.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.netflixClone.R

@Preview
@Composable
fun MovieHeader() {
    Box(modifier = Modifier.wrapContentHeight()) {
        AsyncImage(
            model = "https://m.media-amazon.com/images/M/MV5BY2JhZTRlYzYtZmI1OS00NTRhLWFjNGYtNzI1ODJmNmZhZGU1XkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_Ratio1.0000_AL_.jpg",
            contentDescription = "Lost Poster",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        // Contains the tags and buttons at the bottom of the movie image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .align(Alignment.BottomCenter)
        ) {
            HeaderTags()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                TextButton(id = R.drawable.ic_baseline_add_24, "My List")
                PlayButton()
                TextButton(id = R.drawable.ic_outline_info_24, text = "Info")
            }
        }
    }
}

@Composable()
fun TextButton(@DrawableRes id: Int, text: String, contentDescription: String = text) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painterResource(id = id),
                contentDescription = contentDescription
            )

            Text(text = text, color = Color.White, fontSize = 8.sp)
        }
    }
}

@Composable()
fun PlayButton() {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        contentPadding = PaddingValues(
            start = 12.dp, end = 12.dp, top
            = 0
                .dp
        )
    ) {
        Image(
            painterResource(id = R.drawable.ic_baseline_play_arrow_24),
            contentDescription = "Play button icon",
            colorFilter = ColorFilter.tint(Color.Black)
        )
        Text(
            text = "Play",
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp),
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview
@Composable
fun HeaderTags() {
    // Tags
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProvideTextStyle(value = TextStyle(fontSize = 12.sp, color = Color.White)) {
            Text("Explosive")
            CircleSeparator()
            Text("Exciting")
            CircleSeparator()
            Text("Action Thriller")
            CircleSeparator()
            Text("Heist Movie")
        }
    }
}

@Preview
@Composable
fun CircleSeparator() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(8.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    )
}
