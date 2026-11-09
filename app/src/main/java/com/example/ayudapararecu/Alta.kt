package com.example.ayudapararecu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ayudapararecu.databinding.ActivityAltaBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Alta : AppCompatActivity() {
    private lateinit var binding: ActivityAltaBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAltaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGuardarProducto.setOnClickListener {
            val codigo = binding.etCodigoProducto.text.toString().trim()
            val nombre = binding.etNombreProducto.text.toString().trim()
            val stockStr = binding.etCantidad.text.toString().trim()
            val precioStr = binding.etPrecio.text.toString().trim()
            val categoria = binding.etCategoria.text.toString().trim()

            if (codigo.isNotEmpty() && nombre.isNotEmpty() && stockStr.isNotEmpty() && precioStr.isNotEmpty()) {
                val stock = stockStr.toIntOrNull() ?: 0
                val precio = precioStr.toDoubleOrNull() ?: 0.0

                db.collection("productos").document(codigo)
                    .set(
                        hashMapOf(
                            "codigo" to codigo,
                            "nombre" to nombre,
                            "stock" to stock,
                            "precio" to precio,
                            "categoria" to categoria
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "¡Producto guardado exitosamente!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al guardar producto: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelar.setOnClickListener {
            val intento = Intent(this, Panel::class.java)
            startActivity(intento)
        }
    }
}