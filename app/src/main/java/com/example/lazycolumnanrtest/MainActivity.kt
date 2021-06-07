package com.example.lazycolumnanrtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.* // ktlint-disable no-wildcard-imports
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lazycolumnanrtest.ui.theme.LazyColumnAnrTestTheme
import kotlin.reflect.KSuspendFunction0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumnAnrTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LazyColumnContainer()
                }
            }
        }
    }
}

data class Item(val name: String, val title: String)

@Composable
fun LazyColumnContainer() {
    val items = (0 until 100).map { Item("Index:$it", "Title:$it") }

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            state = listState
        ) {
            itemsIndexed(items) { rowIndex, rowItem ->
                suspend fun scrollEffect() {
                    val index = if (rowIndex > 0) rowIndex - 1 else rowIndex
                    val offset = if (rowIndex > 0) 400 else 0
                    listState.animateScrollToItem(
                        index, offset
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        rowItem.let {
                            CardContent(
                                name = it.name,
                                title = it.title,
                                onFocused = ::scrollEffect
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardContent(
    name: String,
    title: String,
    onFocused: KSuspendFunction0<Unit>,
) {
    var isFocused by remember { mutableStateOf(false) }
    LaunchedEffect(isFocused) { if (isFocused) onFocused() }

    Column(
        modifier = Modifier
            .height(300.dp)
            .width(240.dp)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .border(
                    5.dp,
                    if (isFocused) Color.Black else Color.Transparent,
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .focusable(true)
                .background(Color.Blue)
                .height(100.dp)
                .width(100.dp)
        )
        Text(text = name, style = TextStyle(Color.Black))
        Text(title)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LazyColumnAnrTestTheme {
        LazyColumnContainer()
    }
}
