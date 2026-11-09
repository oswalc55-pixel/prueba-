package com.example.ayudapararecu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ayudapararecu.databinding.ActivityRegistrarBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class Registrar : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRRegistrarse.setOnClickListener {
            val correo = binding.etRCorreo.text.toString().trim()
            val pass  = binding.etRPassword.text.toString().trim()
            val confirmarPass = binding.etCPassword.text.toString().trim()

            if (correo.isNotEmpty() && pass.isNotEmpty() && confirmarPass.isNotEmpty()) {
                if (pass == confirmarPass) {
                    registrarUsuario(correo, pass)
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRIniciarSesion.setOnClickListener {
            val  intento = Intent(this, MainActivity::class.java)
            startActivity(intento)
            finish()
        }
    }
    public fun registrarUsuario(correo: String, password: String) {
        auth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(baseContext,"Authentication is successful.",Toast.LENGTH_SHORT,).show()
                } else {
                    Toast.makeText(baseContext,"Authentication failed: ${task.exception?.message}",Toast.LENGTH_LONG,).show()
                }
            }
    }
}