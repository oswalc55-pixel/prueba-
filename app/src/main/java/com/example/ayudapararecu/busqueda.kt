package com.example.ayudapararecu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ayudapararecu.databinding.ActivityBusquedaBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class busqueda : AppCompatActivity() {
    private lateinit var binding: ActivityBusquedaBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBusquedaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBuscarProducto.setOnClickListener {
            val codigo = binding.etCodigoProducto.text.toString().trim()

            if (codigo.isNotEmpty()) {
                db.collection("productos").document(codigo)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val nombre = document.getString("nombre") ?: ""
                            val stock = document.getLong("stock")?.toInt() ?: 0
                            val precio = document.getDouble("precio") ?: 0.0
                            val categoria = document.getString("categoria") ?: ""

                            binding.etNombreBusqueda.setText(nombre)
                            binding.etCantidadBusqueda.setText(stock.toString())
                            binding.etPrecioBusqueda.setText(precio.toString())
                            binding.etCategoriaBusqueda.setText(categoria)

                            Toast.makeText(this, "Producto encontrado. Ya puedes editarlo.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "El producto no existe", Toast.LENGTH_SHORT).show()
                            limpiarCampos()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al buscar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Ingresa un código para buscar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnActualizarProducto.setOnClickListener {
            val codigo = binding.etCodigoProducto.text.toString().trim()
            val nuevoNombre = binding.etNombreBusqueda.text.toString().trim()
            val nuevoStockStr = binding.etCantidadBusqueda.text.toString().trim()
            val nuevoPrecioStr = binding.etPrecioBusqueda.text.toString().trim()
            val nuevaCategoria = binding.etCategoriaBusqueda.text.toString().trim()

            if (codigo.isNotEmpty() && nuevoNombre.isNotEmpty() && nuevoStockStr.isNotEmpty() && nuevoPrecioStr.isNotEmpty()) {
                val stock = nuevoStockStr.toIntOrNull() ?: 0
                val precio = nuevoPrecioStr.toDoubleOrNull() ?: 0.0

                db.collection("productos").document(codigo)
                    .update(
                        mapOf(
                            "nombre" to nuevoNombre,
                            "stock" to stock,
                            "precio" to precio,
                            "categoria" to nuevaCategoria
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Datos actualizados corrctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al actualizar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Por favor completa los campos para actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelarActualizacion.setOnClickListener {
            val intento = Intent(this, Panel::class.java)
            startActivity(intento)
        }
    }

    private fun limpiarCampos() {
        binding.etNombreBusqueda.setText("")
        binding.etCantidadBusqueda.setText("")
        binding.etPrecioBusqueda.setText("")
        binding.etCategoriaBusqueda.setText("")
    }
}