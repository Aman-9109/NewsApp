package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.example.network.model.Article
import com.example.network.model.NewsResponse
import com.example.utils.StateHolder
import com.example.utils.newsCategory
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val viewModel: HomeScreenViewModel = hiltViewModel()

    val newsResult by viewModel.newsResponse.collectAsState()

    val searchResult by viewModel.searchNews.collectAsState()


    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        var selected by remember { mutableStateOf("") }

        var search by remember { mutableStateOf("") }




        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            shape = RoundedCornerShape(100.dp),
            placeholder = {
                Text(
                    text = "Search Latest News",
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.W300)
                )
            },
            trailingIcon = {
                IconButton(onClick = {  viewModel.viewModelScope.launch {
                    viewModel.searchNews(search)
                } }, enabled = if (search.trim().isNullOrEmpty()) false else true) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )

                }
            },
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(15.dp))

        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(newsCategory, key = { it }) {


                LazyRowComponent(
                    category = it,
                    onClick = { selected = it },
                    selected = selected == it
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
//
        if (search.trim().isNullOrEmpty()) {
            when (newsResult) {
                is StateHolder.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = (newsResult as StateHolder.Error).error)
                            Text(text = "Reload")
                        }
                    }
                }

                is StateHolder.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StateHolder.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items((newsResult as StateHolder.Success<NewsResponse>).data.articles) { article ->
                                article?.let {
                                    TopHeadLinesComponent(news = it)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            when (searchResult) {
                is StateHolder.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = (newsResult as StateHolder.Error).error)
                            Text(text = "Reload")
                        }
                    }
                }

                is StateHolder.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StateHolder.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items((searchResult as StateHolder.Success<NewsResponse>).data.articles) { article ->
                                article?.let {
                                    TopHeadLinesComponent(news = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


private fun formatDateString(dateString: String?): String {
    if (dateString.isNullOrEmpty()) {
        return "Unknown"
    }

    val temporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(dateString)
    val instant = Instant.from(temporalAccessor)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    val dayOfWeek =
        dateTime.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)
    val dayOfMonth = dateTime.dayOfMonth
    val month = dateTime.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)
    val year = dateTime.year

    return "$dayOfWeek, $dayOfMonth $month $year"
}

@Composable
fun TopHeadLinesComponent(modifier: Modifier = Modifier, news: Article) {
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color.Black)
            .fillMaxWidth()
            .height(150.dp)

    ) {
        AsyncImage(
            model = news.urlToImage ?: "", alpha = 0.5f,
            contentDescription = "news image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = news.description ?: "Unknown",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            ), maxLines = 2,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(15.dp)
        )


        Text(
            text = news.author ?: "Unknown",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(15.dp)
        )
        Text(
            text = formatDateString(news.publishedAt),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(15.dp)
        )
    }
}

@Composable

fun LazyRowComponent(
    modifier: Modifier = Modifier,
    category: String = "Sports",
    selected: Boolean = false,
    onClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .wrapContentSize()
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color.Red.copy(alpha = 0.6f) else Color.White)
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
            .clickable { onClick(category) }
    ) {
        Text(
            text = category,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W600, color = Color.Black
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

