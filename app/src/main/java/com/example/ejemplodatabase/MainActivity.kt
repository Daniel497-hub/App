package com.example.ejemplodatabase

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import com.example.ejemplodatabase.data.NombreEntity
import com.example.ejemplodatabase.data.NombreViewModel
import com.example.ejemplodatabase.ui.theme.EjemploDataBaseTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: NombreViewModel by viewModels()

        setContent {
            EjemploDataBaseTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: NombreViewModel) {
    var currentScreen by remember { mutableStateOf("welcome") }

    when (currentScreen) {
        "welcome" -> WelcomeScreen(
            onNavigateToInfo = { currentScreen = "info" },
            onNavigateToRegister = { currentScreen = "register" }
        )
        "info" -> NombreScreen(viewModel)
        "register" -> RegisterScreen(viewModel)  // Pasamos viewModel
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onNavigateToInfo: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Deportes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "Imagen de bienvenida",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "¡Vida Más Saludable!",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { onNavigateToRegister() },
                modifier = Modifier.width(200.dp)
            ) {
                Text("Registrarse")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onNavigateToInfo,
                modifier = Modifier.width(200.dp)
            ) {
                Text("Informacion Jugadores")
            }
        }
    }
}

@Composable
fun RegisterScreen(viewModel: NombreViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))  // Espacio desde la parte superior
        Text(
            text = "Pantalla de Registro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))  // Espacio entre el título y los campos

        // Campo de entrada para el email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada para la contraseña
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada para confirmar la contraseña
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password == confirmPassword) {
                    viewModel.insertName(NombreEntity(name = email))  // Guardar usuario
                    showMessage = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        if (showMessage) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Usuario registrado con éxito!")
        }
    }
}

@Composable
fun NombreScreen(viewModel: NombreViewModel) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))
        Listar(viewModel)
    }
}

@Composable
fun Listar(viewModel: NombreViewModel) {
    val nombres by viewModel.getNombres().asFlow().collectAsState(
        initial = emptyList()
    )
    List(nombres)
}

@Composable
fun List(nombre: List<NombreEntity>) {
    nombre.forEach {
        Text(it.name)
    }
}