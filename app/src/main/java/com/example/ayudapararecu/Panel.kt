package com.example.ayudapararecu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ayudapararecu.data.Producto
import com.example.ayudapararecu.databinding.ActivityPanelBinding
import com.example.ayudapararecu.ui.adapter.ProductoAdapter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Panel : AppCompatActivity() {
    private lateinit var binding: ActivityPanelBinding
    private val db = Firebase.firestore
    private lateinit var productoAdapter: ProductoAdapter
    private val listaProductos = mutableListOf<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        productoAdapter = ProductoAdapter(listaProductos)
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = productoAdapter

        cargarProductosFirestore()


        binding.btnAlta.setOnClickListener {
            val intent = Intent(this, Alta::class.java)
            startActivity(intent)
        }
        binding.btnSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnBaja.setOnClickListener {
            val intent = Intent(this, baja::class.java)
            startActivity(intent)
        }

        binding.btnBuscar.setOnClickListener {
            val intent = Intent(this, busqueda::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarProductosFirestore()
    }

    private fun cargarProductosFirestore() {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                listaProductos.clear()
                for (document in result) {
                    val producto = document.toObject(Producto::class.java)
                    listaProductos.add(producto)
                }
                productoAdapter.actualizarLista(listaProductos)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}