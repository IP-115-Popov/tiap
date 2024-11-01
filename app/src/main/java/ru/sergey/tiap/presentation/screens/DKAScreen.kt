package ru.sergey.tiap.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sergey.domain.State
import ru.sergey.tiap.R
import ru.sergey.tiap.viewmodel.DKAScreenViewModel


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DKAScreen(vm : DKAScreenViewModel = viewModel()) {
    val items = vm.DKD.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize(0.9f)) {
            itemsIndexed(items.value) { index, state->
                StateItem(index , state ,vm)
            }
        }
        Row(Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    vm.addState()
                },
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xff004D40),       // цвет текста
                    containerColor = Color(0xff9ed6df)
                ),   // цвет фона
                border = BorderStroke(3.dp, Color.DarkGray)
            ) {
                Text("Add State", fontSize = 25.sp)
            }
            Button(
                onClick = {
                    vm.setDKA()
                },
                Modifier
                    .fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color(0xff004D40),       // цвет текста
                    containerColor = Color(0xff9ed6df)
                ),   // цвет фона
                border = BorderStroke(3.dp, Color.DarkGray)
            ) {
                Text(stringResource(R.string.set_dka), fontSize = 25.sp)
            }
        }
    }
}
@Composable
fun StateItem(index : Int, state: State , vm : DKAScreenViewModel) {
    val stateString = remember {mutableStateOf(state.name)}
    val symbol = remember {mutableStateOf(state.name)}
    val nextStateString = remember {mutableStateOf(state.name)}

    Row {
        Column(modifier = Modifier.weight(1.5f)) {
            Text(text = "Name")
            TextField(
                value = stateString.value,
                textStyle = TextStyle(fontSize=25.sp),
                onValueChange = {
                    newText -> stateString.value = newText
                    //vm.updateItem(index, stateString.value, symbol.value, nextStateString.value)
                    vm.updateItemName(index, stateString.value)
                }
            )
        }
        val keys = remember {
            mutableStateListOf<String>("")
        }
        val values = remember {
            mutableStateListOf<String>("")
        }
       Column(modifier = Modifier.weight(3f)) {
//           keys.forEachIndexed { indexPath, key ->
//                val value = values.getOrElse(indexPath, {""})
//                PathItem(index,indexPath , stateString, key, value, vm)
//           }
           keys.forEachIndexed { indexPath, key ->
                val value = values.getOrElse(indexPath, {""})
               PathItem(index , indexPath, stateString, keys, values, vm)
           }
        }
        Button(
            onClick = {
                val key = ""
                val value = ""
                keys.add(key)
                values.add(value)
                vm.addPathToState(index, key, value)
            },
            Modifier
                .fillMaxSize()
                //.size(30.dp, height = 30.dp)
                .weight(1f),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color(0xff004D40),       // цвет текста
                containerColor = Color(0xff9ed6df)
            ),   // цвет фона
            border = BorderStroke(3.dp, Color.DarkGray)
        ) {
            Text("+", fontSize = 25.sp)
        }
    }
}
@Composable
fun PathItem(
    indexState: Int,
    indexPath: Int,
    stateString: MutableState<String>,
    keys: SnapshotStateList<String>,
    values: SnapshotStateList<String>,
    vm: DKAScreenViewModel
) {
    // Используем remember для сохранения состояния элемента
    val symbol = remember(keys[indexPath]) { mutableStateOf(keys[indexPath]) }
    val nextStateString = remember(values[indexPath]) { mutableStateOf(values[indexPath]) }

    Row {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Symbol")
            TextField(
                value = symbol.value,
                textStyle = TextStyle(fontSize = 25.sp),
                onValueChange = { newText ->
                    // Проверяем, является ли новый текст пустым или равно 1 символу
                    if (newText.length <= 1) {
                        symbol.value = newText
                        keys[indexPath] = newText // Обновляем значение в keys
                        vm.updateItemsPath(indexState, stateString.value, keys, values)
                    }
                }
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Next")
            TextField(
                value = nextStateString.value,
                textStyle = TextStyle(fontSize = 25.sp),
                onValueChange = { newText ->
                    nextStateString.value = newText
                    values[indexPath] = newText // Обновляем значение в values
                    vm.updateItemsPath(indexState, stateString.value, keys, values)
                }
            )
        }
    }
}
//@Composable
//fun PathItem(indexState : Int, indexPath : Int, stateString : MutableState<String>, key : String, value : String, vm : DKAScreenViewModel) {
//    val symbol = remember {mutableStateOf(key)}
//    val nextStateString = remember {mutableStateOf(value)}
//    Row {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(text = "Symbol")
//            TextField(
//                value = symbol.value,
//                textStyle = TextStyle(fontSize = 25.sp),
//                onValueChange = { newText ->
//                    // Проверяем, является ли новый текст пустым или равно 1 символу
//                    if (newText.length <= 1) {
//                        symbol.value = newText // разрешаем обновление
//                        //vm.updateItem(index, stateString.value, symbol.value, nextStateString.value)
//                        vm.updateItemsPath(indexState, stateString.value, symbol.value, nextStateString.value)
//                    }
//                }
//            )
//        }
//        Column(modifier = Modifier.weight(1f)) {
//            Text(text = "Next")
//            TextField(
//                value = nextStateString.value,
//                textStyle = TextStyle(fontSize = 25.sp),
//                onValueChange = { newText ->
//                    nextStateString.value = newText
//                    //vm.updateItem(index, stateString.value, symbol.value, nextStateString.value)
//                    vm.updateItemsPath(indexState, indexPath, stateString.value, symbol.value, nextStateString.value)
//                }
//            )
//        }
//    }
//}