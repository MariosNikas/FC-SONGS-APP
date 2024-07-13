package com.example.app.presentation.teamSelection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun TeamCard(
    modifier: Modifier = Modifier,
    videoName: String,
    imageUrl: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Column(
        modifier = modifier
            .aspectRatio(3 / 2.6f)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color.LightGray)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color.Black else Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onSelected()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = videoName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(0.8f)
        )
        Text(
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp),
            text = videoName
        )
    }

}