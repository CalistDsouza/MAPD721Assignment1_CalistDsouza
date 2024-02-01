package com.example.mapd721_assignment1_calistdsouza

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapd721_assignment1_calistdsouza.ui.theme.MAPD721Assignment1_CalistDsouzaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set the content of the activity using the custom theme
            MAPD721Assignment1_CalistDsouzaTheme {
                // Create a surface with the specified background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Display the main screen content
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // Get the current context
    val context = LocalContext.current
    // Create a coroutine scope for asynchronous operations
    val coroutineScope = rememberCoroutineScope()
    // Initialize a DataStore instance for handling data storage
    val dataStore = DataStore(context)

    // Retrieve the saved user data states from the data store
    val savedUsernameState = dataStore.getUsername.collectAsState(initial = "")
    val savedUserEmailState = dataStore.getUserEmail.collectAsState(initial = "")
    val savedUserIdState = dataStore.getUserId.collectAsState(initial = "")

    // Create mutable states to hold the user input data
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }

    // Create a column layout with padding for better appearance
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Add an outlined text field for the username input
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username", color = Color.Gray, fontSize = 14.sp) }
        )
        // Add some space between the text fields
        Spacer(modifier = Modifier.height(8.dp))

        // Add an outlined text field for the email input
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "User Email", color = Color.Gray, fontSize = 14.sp) },
        )
        // Add some space between the text fields
        Spacer(modifier = Modifier.height(8.dp))

        // Add an outlined text field for the id input
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = id,
            onValueChange = { id = it },
            label = { Text(text = "User ID", color = Color.Gray, fontSize = 14.sp) },
        )
        // Add some space between the text fields and the buttons
        Spacer(modifier = Modifier.height(16.dp))

        // Create a row layout for the action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Add a button for loading the user data
            Button(
                onClick = {
                    // Load the user data from the saved states and assign them to the input fields
                    username = savedUsernameState.value ?: ""
                    email = savedUserEmailState.value ?: ""
                    id = if(savedUserIdState.value != -1) {
                        // If the saved id is not -1, convert it to a string
                        (savedUserIdState.value ?: "").toString()
                    } else {
                        // If the saved id is -1, set the id input to empty
                        ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBF07B)),
                shape = MaterialTheme.shapes.small,
            ) {
                // Set the text of the button to "Load"
                Text("Load", color= Color.Black)
            }

            // Add a button for saving the user data
            Button(
                onClick = {
                    // Launch a coroutine to save the user data
                    coroutineScope.launch {
                        // Check if the id input is not empty
                        if (id.isNotEmpty()) {
                            try {
                                // Try to convert the id input to an integer and save the user data
                                dataStore.saveUserData(UserData(username, email, id.toInt()))

                                // Reset the input fields
                                username = ""
                                email = ""
                                id = ""
                            } catch (e: NumberFormatException) {
                                // If the id input is not a valid integer, save the user data with -1 as the id
                                dataStore.saveUserData(UserData(username, email, -1))
                            }
                        } else {
                            // If the id input is empty, save the user data with -1 as the id
                            dataStore.saveUserData(UserData(username, email, -1))
                        }
                    }


                    // Display a toast message indicating that the data is saved
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5CED73)),
                shape = MaterialTheme.shapes.small,
            ) {
                // Set the text of the button to "Save"
                Text("Save", color= Color.Black)
            }

            // Add a button for clearing the user data
            Button(
                onClick = {
                    // Launch a coroutine to clear the user data
                    coroutineScope.launch {
                        dataStore.clearUserData()
                    }

                    // Reset the input fields
                    username = ""
                    email = ""
                    id = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7F7F)),
                shape = MaterialTheme.shapes.small,
            ) {
                // Set the text of the button to "Clear"
                Text("Clear", color = Color.Black)
            }
        }

        // Add some space between the buttons and the text
        Spacer(modifier = Modifier.weight(1f))

        // Display a horizontal divider line
        Divider()

        // Create a column layout for displaying additional information
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Display the name of the author
            Text("Calist Dsouza", fontWeight = FontWeight.Bold)
            // Display the id of the author
            Text("301359253", fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    // Display a preview of the main screen
    MAPD721Assignment1_CalistDsouzaTheme {
        MainScreen()
    }
}
