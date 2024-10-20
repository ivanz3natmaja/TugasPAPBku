package com.tifd.projectcomposed
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.material.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tifd.projectcomposed.Navigation.NavigationItem
import com.tifd.projectcomposed.Navigation.Screen
import com.tifd.projectcomposed.Screen.ProfileScreen
import com.tifd.projectcomposed.Screen.TugasScreen




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectComposeDTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen()
                }
            }
        }
    }
}
@Composable
private fun BottomBar(navController: NavController, modifier: Modifier = Modifier){
    NavigationBar (
        modifier = modifier,
    ) {
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.matkul),
                icon = Icons.Default.Search,
                screen = Screen.Matkul
            ),
            NavigationItem(
                title = stringResource(R.string.tugas),
                icon = Icons.Default.Warning,
                screen = Screen.Tugas
            ),
            NavigationItem(
                title = stringResource(R.string.profil),
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profil
            ),
        )

        navigationItems.map{
            item -> NavigationBarItem(
                icon ={
                    Icon(imageVector = item.icon,
                        contentDescription = item.title)
                },
                label = {Text(item.title)},
                selected = false,
                onClick = {
                    navController.navigate(item.screen.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }

}

@Composable
fun MainActivityContent(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = { BottomBar(navController) },
        modifier = modifier
    ) { innerPadding -> // Use curly braces for the content lambda
        NavHost(
            navController = navController,
            startDestination = Screen.Matkul.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Matkul.route) { MatkulScreen() }
            composable(Screen.Tugas.route) { TugasScreen() }
            composable(Screen.Profil.route) { ProfileScreen() }
        }
    }
}



