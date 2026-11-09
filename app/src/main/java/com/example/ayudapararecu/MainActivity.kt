package com.example.ayudapararecu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ayudapararecu.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() //indispensable paraq que funcione tambien yua que con la que da firebase no funciona y marca error



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         binding.btnRegistrarInicio.setOnClickListener {
             val intento = Intent(this, Registrar::class.java)
             startActivity(intento)
         }
        binding.btnIniciarSesionMain.setOnClickListener {
            val correo = binding.etCorreo.text.toString().trim()
            val pass = binding.etContrasena.text.toString()

            if (correo.isNotEmpty() && pass.isNotEmpty()){
                loginUser(correo, pass)
            }else{
                Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
    public fun loginUser(mail: String, password: String) {
        auth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "Authentication successful.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val llamarPanel = Intent(this, Panel::class.java)
                    llamarPanel.putExtra("correoUsuario", mail)
                    startActivity(llamarPanel)
                    finish()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
    }
}