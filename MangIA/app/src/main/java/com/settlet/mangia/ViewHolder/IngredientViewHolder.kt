package com.settlet.mangia.ViewHolder

import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.settlet.mangia.Model.Ingredient
import com.settlet.mangia.MRecipeStep2Activity
import com.settlet.mangia.R
import com.settlet.mangia.databinding.RowIngredientRecyclerBinding
import kotlinx.android.synthetic.main.activity_mrecipe_step2.*

class IngredientViewHolder (view:View):RecyclerView.ViewHolder(view) {
    val binding = RowIngredientRecyclerBinding.bind(view)

    fun render(ingredient: Ingredient){
        binding.txvIngredient.text = ingredient.nombre
        val hojas =  binding.imvIngredient.context.resources.getStringArray(R.array.hojas)
        val liquidos = binding.imvIngredient.context.resources.getStringArray(R.array.liquidos)
        val polvos = binding.imvIngredient.context.resources.getStringArray(R.array.polvos)
        val legumbres = binding.imvIngredient.context.resources.getStringArray(R.array.legumbres_semillas)
        val unidad = binding.imvIngredient.context.resources.getStringArray(R.array.unidad)
        var selectedUnity:String = ""

        val arrayAdapterHojas= ArrayAdapter<String>(binding.imvIngredient.context,
            R.layout.spinner_unity_item, hojas)
        val arrayAdapterLiquidos = ArrayAdapter<String>(binding.imvIngredient.context,
            R.layout.spinner_unity_item, liquidos)
        val arrayAdapterPolvos = ArrayAdapter<String>(binding.imvIngredient.context,
            R.layout.spinner_unity_item, polvos)
        val arrayAdapterLegumbresSemillas = ArrayAdapter<String>(binding.imvIngredient.context,
            R.layout.spinner_unity_item, legumbres)
        val arrayAdapterUnidad = ArrayAdapter<String>(binding.imvIngredient.context,
            R.layout.spinner_unity_item, unidad)
        when(ingredient.tipoUnidad)
        {
            "Hojas"->binding.spnUnity.adapter = arrayAdapterHojas
            "Liquidos"->binding.spnUnity.adapter = arrayAdapterLiquidos
            "Polvos"->binding.spnUnity.adapter = arrayAdapterPolvos
            "Legumbres"->binding.spnUnity.adapter = arrayAdapterLegumbresSemillas
            "Semillas"->binding.spnUnity.adapter = arrayAdapterLegumbresSemillas
            "Unidad"->binding.spnUnity.adapter = arrayAdapterUnidad
        }

        binding.spnUnity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0 != null) {
                    selectedUnity = p0.getItemAtPosition(p2).toString()
                    when(p0.getItemAtPosition(p2)){
                        "Atd"-> Toast.makeText(binding.spnUnity.context,"Atado",Toast.LENGTH_SHORT).show()
                        "Gr"-> Toast.makeText(binding.spnUnity.context,"Gramo",Toast.LENGTH_SHORT).show()
                        "Kg"-> Toast.makeText(binding.spnUnity.context,"Kilogramo",Toast.LENGTH_SHORT).show()
                        "Hjs"-> Toast.makeText(binding.spnUnity.context,"Hojas",Toast.LENGTH_SHORT).show()
                        "Un"-> Toast.makeText(binding.spnUnity.context,"Unidad",Toast.LENGTH_SHORT).show()
                        "Lts"-> Toast.makeText(binding.spnUnity.context,"Litros",Toast.LENGTH_SHORT).show()
                        "Ml"-> Toast.makeText(binding.spnUnity.context,"Mililitros",Toast.LENGTH_SHORT).show()
                        "Cm³"-> Toast.makeText(binding.spnUnity.context,"Centimetros Cúbicos",Toast.LENGTH_SHORT).show()
                        "Tz"-> Toast.makeText(binding.spnUnity.context,"Taza",Toast.LENGTH_SHORT).show()
                        "C/s"-> Toast.makeText(binding.spnUnity.context,"Cucharada o Cucharada Sopera",Toast.LENGTH_SHORT).show()
                        "C/c"-> Toast.makeText(binding.spnUnity.context,"Cucharadita o Cuchara de Postre",Toast.LENGTH_SHORT).show()
                        else-> Toast.makeText(binding.spnUnity.context,"Unidad",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.imvRemove.setOnClickListener {
            val a = binding.imvRemove.context as MRecipeStep2Activity
            binding.txpQuantity.setText("0")
            a.listIngredientRecipe.remove(ingredient)
            a.rcvIngredients.adapter!!.notifyDataSetChanged()
        }

        binding.imbAddQuantity.setOnClickListener {
            if (binding.txpQuantity.text.isEmpty())
            {
                binding.txpQuantity.setText("0")
            }
            when(selectedUnity ){
                "Atd"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "Gr"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+10).toString())
                "Kg"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "Hjs"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "Un"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "Lts"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "Ml"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+10).toString())
                "Cm³"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "Tz"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "C/s"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                "C/c"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
                else-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()+1).toString())
            }
        }
        binding.imbRemoveQuantity.setOnClickListener {
            if(binding.txpQuantity.text.toString() == "0")
            {
                Toast.makeText(binding.txpQuantity.context,"No puedes tener menos de 0 ${binding.spnUnity.selectedItem.toString()} de ${ingredient.nombre} en tu receta.",Toast.LENGTH_SHORT).show()
            }
            else{
                if (binding.txpQuantity.text.isEmpty())
                {
                    binding.txpQuantity.setText("0")
                }
                when(selectedUnity){
                    "Atd"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "Gr"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-10).toString())
                    "Kg"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "Hjs"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "Un"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "Lts"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "Ml"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-10).toString())
                    "Cm³"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "Tz"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "C/s"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    "C/c"-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                    else-> binding.txpQuantity.setText((binding.txpQuantity.text.trim().toString().toInt()-1).toString())
                }
            }
        }

        ingredient.imgRef.downloadUrl.addOnSuccessListener { result ->
            Glide.with(binding.imvIngredient.context)
                .load(result)
                .into(binding.imvIngredient)

        }.addOnFailureListener {

        }

    }
}