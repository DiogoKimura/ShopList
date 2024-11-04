package com.kimurashin.shoplist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Words
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kimurashin.shoplist.model.ShopItemEntity
import com.kimurashin.shoplist.ui.theme.ShopListTheme
import com.kimurashin.shoplist.util.Converter
import dagger.hilt.android.AndroidEntryPoint

enum class Action {
    CHECKBOX, DELETE, ADD, NONE
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShopListScreen(innerPadding)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListScreen(
    innerPadding: PaddingValues,
    viewModel: MainViewModel = viewModel()
) {
    val state = viewModel.allItems.collectAsState(initial = emptyList())

    var newItemLabel by rememberSaveable { mutableStateOf("") }
    var isLoadingImage by rememberSaveable { mutableStateOf(false) }

    var itemChangedIndex by remember { mutableIntStateOf(-1) }
    var itemChangedAction by remember { mutableStateOf(Action.NONE) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            it?.let {
                viewModel.addItem(newItemLabel, Converter().fromBitmap(it))
                newItemLabel = ""
            }
            isLoadingImage = false
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(title = { Text(text = "Lista de compras") })

        InputSection(
            newItemLabel,
            onNewItemChanged = { newItemLabel = it },
            onAddItemClick = { withPicture ->
                if (newItemLabel.isNotBlank()) {
                    if (withPicture) pickMedia.launch(null)
                    else {
                        viewModel.addItem(newItemLabel)
                        newItemLabel = ""
                    }
                }
            },
            false, //state.value.isLoading,
            itemChangedAction
        )

        ShopListSection(
            innerPadding,
            state,
            onItemCheckedChanged = { index, item ->
                itemChangedIndex = index
                itemChangedAction = Action.CHECKBOX
                viewModel.updateItem(item)
            },
            onItemClick = { index, item ->
                itemChangedIndex = index
                itemChangedAction = Action.DELETE
                viewModel.removeItem(item)
            },
            false, //state.value.isLoading,
            Pair(itemChangedIndex, itemChangedAction)
        )
    }
}

@Composable
fun LoadingProgressBar(
    color: Color,
    isLoading: Boolean
) {
    if (isLoading) {
        Spacer(modifier = Modifier.width(16.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = color
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun ShopListSection(
    innerPadding: PaddingValues,
    state: State<List<ShopItemEntity>>,
    onItemCheckedChanged: (Int, ShopItemEntity) -> Unit,
    onItemClick: (Int, ShopItemEntity) -> Unit,
    isLoading: Boolean,
    itemChanged: Pair<Int, Action>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = innerPadding.calculateBottomPadding()),
    ) {
        itemsIndexed(state.value) { index, item ->
            ShopItemCard(
                item,
                { onItemCheckedChanged(index, item) },
                { onItemClick(index, item) },
                isLoading && itemChanged.first == index,
                itemChanged.second
            )
        }
    }
}

@Composable
fun ShopItemCard(
    item: ShopItemEntity,
    onItemCheckedChanged: () -> Unit,
    onIconButtonClick: () -> Unit,
    isLoading: Boolean,
    action: Action
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .graphicsLayer(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1F
            )
    ) {
        Column {
            item.image?.let {
                Image(
                    bitmap = Converter().toBitmap(it).asImageBitmap(),
                    contentDescription = item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isLoading && action == Action.CHECKBOX) {
                    LoadingProgressBar(Color.Gray, isLoading)
                } else {
                    Checkbox(
                        checked = item.checked,
                        onCheckedChange = { onItemCheckedChanged() },
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }

                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None
                )

                IconButton(onClick = { onIconButtonClick() }) {
                    if (isLoading && action == Action.DELETE) {
                        LoadingProgressBar(Color.Gray, isLoading)
                    } else {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun InputSection(
    newValue: String,
    onNewItemChanged: (String) -> Unit,
    onAddItemClick: (Boolean) -> Unit,
    isLoading: Boolean,
    action: Action
) {
    Column {
        PlaceholderTextField(
            value = newValue,
            onValueChanged = { onNewItemChanged.invoke(it) },
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = "Novo item",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onAddItemClick.invoke(true) }) {
                if (isLoading && action == Action.ADD) {
                    LoadingProgressBar(Color.White, isLoading)
                } else {
                    Text(text = "Adicionar com foto")
                }
            }

            Button(onClick = { onAddItemClick.invoke(false) }) {
                if (isLoading && action == Action.ADD) {
                    LoadingProgressBar(Color.White, isLoading)
                } else {
                    Text(text = "Adicionar")
                }
            }
        }
    }

}

@Composable
fun PlaceholderTextField(
    modifier: Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    textStyle: TextStyle,
    placeholder: String
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(
                width = 2.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChanged,
            textStyle = textStyle,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .focusRequester(FocusRequester()),
            keyboardOptions = KeyboardOptions(capitalization = Words)
        )

        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = textStyle.copy(color = Color.Gray),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopListPreview() {
    ShopListTheme {
        ShopListScreen(PaddingValues(16.dp))
    }
}