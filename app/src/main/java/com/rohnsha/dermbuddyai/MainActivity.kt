package com.rohnsha.dermbuddyai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rohnsha.dermbuddyai.bottom_navbar.bottomNavGraph
import com.rohnsha.dermbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.dermbuddyai.ui.theme.DermBuddyAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextUtill.ContextUtils.initialize(this)
        setContent {
            DermBuddyAITheme {
                val items= listOf(
                    bottomNavItems.Home,
                    bottomNavItems.Insights,
                    bottomNavItems.Settings
                )
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                val navcontroller= rememberNavController()
                val navBackStackEntry by navcontroller.currentBackStackEntryAsState()
                val currentDestination= navBackStackEntry?.destination
                val bottomDestination= items.any { it.route== currentDestination?.route }

                Scaffold(
                    bottomBar = {
                        if (bottomDestination){
                            NavigationBar {
                                items.forEachIndexed { index, bottomNavItems ->
                                    NavigationBarItem(
                                        selected = currentDestination?.hierarchy?.any {
                                            it.route == bottomNavItems.route
                                        } == true,
                                        onClick = {
                                            selectedItemIndex= index
                                            navcontroller.navigate(bottomNavItems.route){
                                                popUpTo(navcontroller.graph.findStartDestination().id)
                                                launchSingleTop= true
                                            }
                                        },
                                        label = {
                                            Text(text = bottomNavItems.title)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (selectedItemIndex==index){
                                                    bottomNavItems.selectedIcon
                                                } else bottomNavItems.unselectedIcon,
                                                contentDescription = bottomNavItems.title
                                            )
                                        })
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    bottomNavGraph(
                        navController = navcontroller,
                        padding = paddingValues
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DermBuddyAITheme {
        Greeting("Android")
    }
}