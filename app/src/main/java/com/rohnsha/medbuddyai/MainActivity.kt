package com.rohnsha.medbuddyai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavGraph
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.ui.theme.DermBuddyAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isLoadingCompleted= false
        installSplashScreen().setKeepOnScreenCondition{
            !isLoadingCompleted
        }
        ContextUtill.ContextUtils.initialize(this)
        setContent {
            DermBuddyAITheme {
                val items= listOf(
                    bottomNavItems.Home,
                    bottomNavItems.Explore,
                    bottomNavItems.Insights,
                    bottomNavItems.Settings,
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
                                        },
                                        alwaysShowLabel = false,
                                    )
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    bottomNavGraph(
                        navController = navcontroller,
                        padding = paddingValues
                    )
                    isLoadingCompleted=true
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