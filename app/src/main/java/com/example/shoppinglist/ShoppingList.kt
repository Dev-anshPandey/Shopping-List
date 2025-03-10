package com.example.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int,
                        var name : String,
                        var quantity : Int,
                        var isEditing : Boolean= false
)

@Composable
fun ShoppingListTheme( modifier: Modifier = Modifier) {
    var sItems by remember{mutableStateOf(listOf<ShoppingItem>())}
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuality by remember { mutableStateOf("") }
    Column(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showDialog=true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems){
                item ->
                if(item.isEditing){
                    shoppingItemEditor(item = item,
                        onEditComplete =
                        { editedName , editedQuantity ->
                           sItems =sItems.map { it.copy(isEditing = false) }
                            val editedItem = sItems.find { it.id==item.id }
                            editedItem?.let {
                                it.name=editedName
                                it.quantity=editedQuantity

                            }
                        })

                }
                else{
                    shoppingListItem(item = item, onEdit = { /*TODO*/
                      sItems=sItems.map { it.copy(isEditing = it.id==item.id) }
                    }, onDelete = {
                        sItems=sItems-item
                    })
                }
            }
        }

    }
    if(showDialog){
       AlertDialog(onDismissRequest = { /*TODO*/
       showDialog=false
       }, confirmButton = {
           Row (
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(8.dp),
               horizontalArrangement = Arrangement.SpaceBetween
           ){
               Button(onClick = { /*TODO*/
                if(itemName.isNotBlank()){
                   val newItem = ShoppingItem(sItems.size+1,itemName,itemQuality.toInt())
                    sItems=sItems+newItem

                    print("Sze ${sItems.size}")
                    showDialog=false
                    itemName=""
                    itemQuality=""
                }
               }) {
                   Text(text = "Add")
               }
               Button(onClick = { /*TODO*/
               showDialog=false
               }) {
                   Text(text = "Cancel")
               }
           }
       } ,
           title = {
           Text(text = "Add Shopping Title", color = Color.Black)
       },
           text = {
               Column {
                   OutlinedTextField(value = itemName,
                       singleLine = true,
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(8.dp),
                       onValueChange ={
                       itemName=it
                   } )
                   OutlinedTextField(value = itemQuality,
                       singleLine = true,
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(8.dp),
                       onValueChange ={
                           itemQuality=it
                       } )
               }

           }
           )
    }
}
@Composable
fun shoppingListItem(item: ShoppingItem , onEdit:()->Unit,onDelete:()->Unit ){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(width = 2.dp, color = Color(0xff018786))
            ),

    ){
      Text(text = item.name , modifier = Modifier.padding(8.dp))
        Text(text = "Qty : ${item.quantity}" , modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(8.dp)){
               IconButton(onClick = onEdit) {
                   Icon(imageVector = Icons.Default.Edit, contentDescription = "")
               }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "")
            }
        }
    }

}

@Composable
fun shoppingItemEditor(item: ShoppingItem , onEditComplete:(String , Int)->Unit){
    var editedName by remember{mutableStateOf(item.name)}
    var editedQuantity by remember{mutableStateOf(item.quantity.toString())}
    var isEditing by remember{mutableStateOf(item.isEditing)}

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Column {
            BasicTextField(value = editedName, onValueChange ={
                editedName=it
            } )
            BasicTextField(value = editedQuantity, onValueChange ={
                editedQuantity=it
            } )
        }
  Button(onClick = { /*TODO*/
   isEditing=false
      onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)
  }) {
      Text(text = "Save")
  }
    }

}