package com.rohnsha.medbuddyai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavGraph
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.ui.theme.DermBuddyAITheme
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customGrey
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.formAccent
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
                    bottomNavItems.Community,
                    bottomNavItems.Preferences,
                )
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                val navcontroller= rememberNavController()
                val navBackStackEntry by navcontroller.currentBackStackEntryAsState()
                val currentDestination= navBackStackEntry?.destination
                val currentItemIndex = items.indexOfFirst { it.route == currentDestination?.route }
                val bottomDestination= items.any { it.route== currentDestination?.route }

                if (currentItemIndex != -1 && currentItemIndex != selectedItemIndex){
                    selectedItemIndex= currentItemIndex
                }

                Scaffold(
                    bottomBar = {
                        if (bottomDestination){
                            NavigationBar(
                                containerColor = ViewDash
                            ) {
                                items.forEachIndexed { index, bottomNavItems ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex==index,
                                        onClick = {
                                            selectedItemIndex= index
                                            navcontroller.navigate(bottomNavItems.route){
                                                popUpTo(navcontroller.graph.findStartDestination().id)
                                                launchSingleTop= true
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = bottomNavItems.title,
                                                fontSize = 12.sp,
                                                fontFamily = fontFamily,
                                                color = customGrey
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = ViewDash,
                                            selectedIconColor = formAccent,
                                            unselectedIconColor = formAccent
                                        ),
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