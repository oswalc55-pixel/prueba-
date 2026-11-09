package com.example.ayudapararecu.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayudapararecu.data.Producto
import com.example.ayudapararecu.databinding.ItemProductoBinding

class ProductoAdapter (
    private var  listaProductos: List<Producto>
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder{
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context), parent,false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {

        val producto = listaProductos[position]
        holder.binding.tvNombreProducto.text = producto.nombre
        holder.binding.tvStockPrecio.text = "Stock: ${producto.stock} | \$${producto.precio}"
        holder.binding.tvCodigoProducto.text = "Código: ${producto.codigo}"
        holder.binding.tvCategoriaProducto.text = "Categoría: ${producto.categoria}"
    }




    override fun getItemCount(): Int = listaProductos.size

    fun actualizarLista(nuevaLista: List<Producto>){
        listaProductos = nuevaLista
        notifyDataSetChanged()
    }

    class ProductoViewHolder (val  binding: ItemProductoBinding) :
            RecyclerView.ViewHolder(binding.root)



}