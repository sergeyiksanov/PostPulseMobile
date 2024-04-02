package com.example.prodapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.model.PublicationModel
import com.example.prodapp.ui.theme.AppTheme
import com.example.prodapp.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: MainViewModel) {

    vm.loadListChannels()
    val items by vm.listChannels.observeAsState()

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var channelId by rememberSaveable {
            mutableIntStateOf(-1)
        }
        var tabItem by rememberSaveable {
            mutableIntStateOf(0)
        }
        vm.loadPublications(channelId, tabItem)
//        val listPublications by vm.publications.observeAsState()
//        val listPublications = emptyList<PublicationModel>()
//        vm.loadAllPublication(selectedTabItemIndex, if(items != null && items!!.isNotEmpty() && selectedItemIndex != 0) { items?.get(selectedItemIndex - 1)?.id ?: 0 } else { 0 })
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    NavigationDrawerItem(
                        label = {
                            Text(text = "Все посты")
                        },
                        selected = -1 == channelId,
                        onClick = {
                            channelId = -1
                            scope.launch {
                                drawerState.close()
                                vm.loadPublications(tabItem, channelId)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if(-1 == channelId) {
                                    Icons.Filled.List
                                } else Icons.Outlined.List,
                                contentDescription = "Все посты"
                            )
                        },
//                            badge = {
//                                item.badgeCount?.let {
//                                    Text(text = item.badgeCount.toString())
//                                }
//                            },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    items?.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = item.name)
                            },
                            selected = index == channelId,
                            onClick = {
                                channelId = index
                                scope.launch {
                                    drawerState.close()
                                    vm.loadPublications(tabItem, channelId)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if(index == channelId) {
                                        Icons.Filled.Create
                                    } else Icons.Outlined.Create,
                                    contentDescription = item.name
                                )
                            },
//                            badge = {
//                                item.badgeCount?.let {
//                                    Text(text = item.badgeCount.toString())
//                                }
//                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Text(text = if(items != null && items!!.isNotEmpty() && channelId != -1) {items?.get(channelId)?.name ?: ""} else {"Все посты"})
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
//                                    vm.loadAllPublication(tabItem, channelId)
                                }
                            } ) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    Modifier.padding(innerPadding)
                ) {
                    TabRow(
                        modifier = Modifier.fillMaxWidth(),
                        selectedTabIndex = tabItem,
                        tabs = {
                            Tab(
                                selected = tabItem == 0,
                                onClick = {
                                    tabItem = 0
                                    vm.loadPublications(tabItem, channelId)
                                },
                                text = { Text(text = "Отложенные", maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 10.sp) },
                                icon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = null) }
                            )
                            Tab(
                                selected = tabItem == 1,
                                onClick = {
                                    tabItem = 1
                                    vm.loadPublications(tabItem, channelId)
                                },
                                text = { Text(text = "Черновики", maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 10.sp) },
                                icon = { Icon(imageVector = Icons.Default.Create, contentDescription = null) }
                            )
                            Tab(
                                selected = tabItem == 2,
                                onClick = {
                                    tabItem = 2
                                    vm.loadPublications(tabItem, channelId)
                                },
                                text = { Text(text = "Отправленные", maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 10.sp) },
                                icon = { Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null) }
                            )
                        }
                    )
                    ListPublications(vm = vm)
                }
            }
        }
    }

}

@Composable
fun TabBar() {

}

//@Preview
//@Composable
//fun ItemPreview() {
//    ItemPublication(PublicationModel(
//        id = 0,
//        title = "Церемония награждения",
//        text = "ука",
//        time = 0,
//        channelId = 0
//    ))
//}

@Composable
fun ListPublications(vm: MainViewModel) {

    val listPublications by vm.publications.observeAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        listPublications?.forEach {
            item { ItemPublication(item = it) }
        }
    }
}

@Composable
fun ItemPublication(item: PublicationModel) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    if(openDialog.value) {
        FullInfoDialog(item = item)
    }

    AppTheme {
//        Box(
//            Modifier.background(color = MaterialTheme.colorScheme.background)
//        ) {

            Surface(
//                modifier = Modifier.padding(40.dp),
                shape = RoundedCornerShape(6.dp),
//                border = BorderStroke(1.dp, Color.DarkGray),
                shadowElevation = 4.dp,
                onClick = {
                    openDialog.value = true
                }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = item.name,
                        fontSize = 16.sp
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    ) {
                        Text(text = "31.03.2024 13:00", fontSize = 10.sp, color = Color.DarkGray)
//                Text(text = , fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                    }
                    Text(minLines = 3, text = item.text, maxLines = 3, fontSize = 10.sp, overflow = TextOverflow.Ellipsis)
                }
            }
//        }
    }
}