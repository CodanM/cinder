package com.codan.cinder.ui.user

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
//import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codan.cinder.data.local.domain.user.User
import com.codan.cinder.viewmodel.UserViewModel
import com.codan.cinder.viewmodel.UserViewModelFactory

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit
) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )
    val focusManager = LocalFocusManager.current

    val users = userViewModel.users.observeAsState(listOf()).value

    var username by rememberSaveable { mutableStateOf("user") }
    var password by rememberSaveable { mutableStateOf("user") }

    var passwordVisibility by remember { mutableStateOf(false) }
    var invalidUsernameOrPassword by remember { mutableStateOf(false) }

    val checkLogin: (String, String) -> User? = { uname: String, pass: String ->
        val user = users.find { it.username == uname }
        if (user?.password != pass) {
            invalidUsernameOrPassword = true
            null
        }
        else user
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            OutlinedTextField(
                value = username,
                label = { Text("Username") },
                onValueChange = {
                    if (invalidUsernameOrPassword) invalidUsernameOrPassword = false
                    username = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                //colors = getMaterial3OutlinedTextFieldColors(),
                //shape = getMaterial3OutlinedTextFieldShape()
            )
            OutlinedTextField(
                value = password,
                label = { Text("Password") },
                onValueChange = {
                    if (invalidUsernameOrPassword) invalidUsernameOrPassword = false
                    password = it
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { checkLogin(username, password)?.let(onLoginSuccess) }
                ),
                singleLine = true,
                trailingIcon = {
                    val icon =
                        if (passwordVisibility) Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility
                    if (password.isNotEmpty()) {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Password visibility"
                            )
                        }
                    }
                },
                //colors = getMaterial3OutlinedTextFieldColors(),
                //shape = getMaterial3OutlinedTextFieldShape()
            )
            if (invalidUsernameOrPassword) {
                Text(
                    text = "Invalid username or password!",
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = { checkLogin(username, password)?.let(onLoginSuccess) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Log in"
                )
            }
        }
    }
}