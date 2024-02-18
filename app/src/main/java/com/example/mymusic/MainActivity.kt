package com.example.mymusic

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mymusic.ui.theme.MyMusicTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // expanded the app UI to system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            MyMusicTheme {
//                PlatformColors(statusBarColor = Color.White)
//                val musicPlayerViewModel: MusicPlayerViewModel by viewModels()
                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
//                        musicPlayerViewModel.uiState.collect {
//                            Log.d("MainActivity", "musicPlayerViewModel.audioList.size ${musicPlayerViewModel.audioList.size}")
//                        }
                        }
                    }
                }
                val systemUiController = rememberSystemUiController()
                
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color.Transparent,
                        darkIcons = false
                    )

                    systemUiController.setNavigationBarColor(
                        color = Color.Transparent,
                        darkIcons = false
                    )
                }

//                permissionLauncher = registerForActivityResult(
//                    ActivityResultContracts.RequestMultiplePermissions()
//                ) { permissions ->
//                    permissions[Manifest.permission.READ_MEDIA_AUDIO]
//                    permissions[Manifest.permission.READ_MEDIA_IMAGES]
//                }


//                if(darkTheme){
//                    systemUiController.setSystemBarsColor(
//                        color = Color.Transparent
//                    )
//                }else{
//                    systemUiController.setSystemBarsColor(
//                        color = Color.White
//                    )
//                }
//                LaunchComposeView()
//                ComposablePermission(permission = android.Manifest.permission.READ_MEDIA_AUDIO)

                RequestPermission(
//                    launcher = launcherPermissions,
                    permission = when {
                        Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2 -> {
                            Log.d("MainActivity", "RequestPermission SDK_INT ${Build.VERSION.SDK_INT}")
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                        else -> {
                            Log.d("MainActivity", "RequestPermission SDK_INT ${Build.VERSION.SDK_INT}")
                            Manifest.permission.READ_MEDIA_AUDIO
                        }
                    }
                ) {
                    MyMusicApp()
                };
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RequestPermissionScreen(modifier: Modifier, content: @Composable () -> Unit) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Request Permission",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }

    private fun navigateToSettingsActivity() {
        startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
        )
    }

    @Composable
    private fun RenderComposeView() {
        MyMusicApp()
    }

    @Composable
    fun RequestPermission(
//        launcher: ManagedActivityResultLauncher<String, Boolean>,
        permission: String,
        granted: @Composable () -> Unit
    ) {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

        Log.d("MainActivity", "requestPermession launcher $launcher")

        when {
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("MainActivity", "requestPermession $permission PERMISSION_GRANTED")

                granted()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                permission) -> {
                Log.d("MainActivity", "requestPermession READ_EXTERNAL_STORAGE shouldShowRequestPermissionRationale")
                SideEffect {
                    launcher.launch(permission)
                }
            }
            else -> {
                Log.d("MainActivity", "requestPermession not granted")
                SideEffect {
                    launcher.launch(permission)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun LaunchComposeView() {
        val filePermissionState = rememberPermissionState(
            permission = android.Manifest.permission.READ_MEDIA_AUDIO
        )

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(filePermissionState) {
            val lifecycle = lifecycleOwner.lifecycle

            val permissionResult = filePermissionState.status

            if (!permissionResult.isGranted) {
                if (permissionResult.shouldShowRationale) {
                    // Show a rationale if needed (optional)
                } else {
                    // Request the permission
//                   LaunchPermissionRequest(context, lifecycleOwner, filePermissionState)
                }
            }
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun LaunchPermissionRequest(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        permissionState: PermissionState
    ) {
//        val lifecycle = lifecycleOwner.lifecycle
//
//        val requestPermissionLauncher =
//            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//                permissionState.invalidatePermissionState()
//            }
//
//        lifecycleOwner.lifecycleScope.launch {
//            val result = requestPermissionLauncher.launch(permissionState.permission)
//            permissionState.updatePermissionResult(result)
//        }
    }

    @Composable
    fun ComposablePermission(permission: String) {
        val ctx = LocalContext.current
        val launcher: ManagedActivityResultLauncher<String, Boolean> =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
//                grantState = it
            }
        // check initial state of permission, it may be already granted
//        var grantState by remember {
//            mutableStateOf(
//                ContextCompat.checkSelfPermission(
//                    ctx,
//                    permission
//                ) == PackageManager.PERMISSION_GRANTED
//            )
//        }

//        Log.d("MainActivity", "grantState $grantState");
        when (ContextCompat.checkSelfPermission(ctx, permission)) {
            PackageManager.PERMISSION_GRANTED -> MyMusicApp()
            else -> {


                launcher.launch(permission)
//            onDenied { launcher.launch(permission) }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun PermissionScreen(
        permissionState: PermissionState,
        successScreen: @Composable () -> Unit,
        onRequestPermission: () -> Unit
    ) {

        Log.d("MainActivity", "permissionState.status ${permissionState.status}")
        when(permissionState.status) {
            is PermissionStatus.Granted -> {
                successScreen()
            }
            is PermissionStatus.Denied -> {
                Scaffold {innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Your Permission Status: ",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (permissionState.status.shouldShowRationale) {
                            Text(text = "We need permission to continue this demo")
                        } else {
                            Text(text = "Looks like you permanently denied permission. Please provide in Settings")
                        }
                    }
                }
            }

        }
        if (permissionState.status.shouldShowRationale) {
            Button(
                onClick = { onRequestPermission() }
            ) {
                Text(text = "Click to request permission")
            }
        }


    }

    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 101
    }
}
