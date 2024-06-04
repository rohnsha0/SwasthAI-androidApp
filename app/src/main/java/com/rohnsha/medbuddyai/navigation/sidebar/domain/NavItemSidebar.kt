package com.rohnsha.medbuddyai.navigation.sidebar.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavItemSidebar(
    val title: String,
    val imageVector: ImageVector
) {

    Home(
        title = "Home",
        imageVector = Icons.Outlined.Home
    ),
    Search(
        title = "Search",
        imageVector = Icons.Outlined.Search
    ),
    Library(
        title = "Library",
        imageVector = Icons.Outlined.Book
    ),


}