package com.settlet.mangia.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.settlet.mangia.MRecipeStep1Activity
import com.settlet.mangia.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.bottomBarH.imbScanBB.setOnClickListener {
            Toast.makeText(it.context,"Escanear",Toast.LENGTH_SHORT).show()
        }
        binding.bottomBarH.imbMRecipeBB.setOnClickListener {
            val intent = Intent(it.context, MRecipeStep1Activity::class.java)
            this.startActivity(intent)
        }
        binding.bottomBarH.imbSearchBB.setOnClickListener {
            Toast.makeText(it.context,"Buscar",Toast.LENGTH_SHORT).show()
        }

        val root: View = binding.root
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}