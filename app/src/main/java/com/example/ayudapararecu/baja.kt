package com.example.ayudapararecu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ayudapararecu.databinding.ActivityBajaBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class baja : AppCompatActivity() {
    private lateinit var binding: ActivityBajaBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBajaBinding.inflate(layoutInflater)
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

                            binding.tvDetalleNombre.text = "Nombre: $nombre"
                            binding.tvDetalleCantidad.text = "Cantidad: $stock"
                            binding.tvDetallePrecio.text = "Precio: \$$precio"
                            binding.tvDetalleCategoria.text = "Categoría: $categoria"

                            Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "El producto no existe", Toast.LENGTH_SHORT).show()
                            limpiarDetalles()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al buscar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Ingresa un código de producto", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnEliminarDefinitivamente.setOnClickListener {
            val codigo = binding.etCodigoProducto.text.toString().trim()

            if (codigo.isNotEmpty()) {
                db.collection("productos").document(codigo)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "¡Producto eliminado definitivamente!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Primero busca un producto para eliminar", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnCancelarEliminacion.setOnClickListener {
            val intento = Intent(this, Panel::class.java)
            startActivity(intento)
        }

    }

    private fun limpiarDetalles() {
        binding.tvDetalleNombre.text = "Nombre:"
        binding.tvDetalleCantidad.text = "Cantidad:"
        binding.tvDetallePrecio.text = "Precio:"
        binding.tvDetalleCategoria.text = "Categoría:"
    }
}