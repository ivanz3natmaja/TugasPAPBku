package com.tifd.projectcomposed

import android.os.Bundle
import coil.compose.AsyncImage
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tifd.projectcomposed.data.model.MainViewModel
import com.tifd.projectcomposed.data.model.Profile
import kotlinx.coroutines.flow.collectLatest

class GithubProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubProfileScreen()
        }
    }
}

@Composable
fun GithubProfileScreen(viewModel: MainViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProfileUser("ivanz3natmaja")
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                }
                user != null -> {
                    DetailContent(user = user!!)
                }
            }
        }
    }
}

@Composable
fun DetailContent(user: Profile) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
        Text(
            text = user.name,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        Text(
            text = user.login,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "Followers: ${user.followers}",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = "Following: ${user.followingCount}",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
