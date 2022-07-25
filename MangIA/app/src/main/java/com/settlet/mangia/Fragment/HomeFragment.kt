package com.settlet.mangia.Fragment

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.settlet.mangia.MRecipeStep1Activity
import com.settlet.mangia.Model.CustomTypefaceSpan
import com.settlet.mangia.Model.Recipe
import com.settlet.mangia.R
import com.settlet.mangia.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.bottom_bar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

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

        addReadMore("Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.",binding.textView, 5)

        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addReadMore(text: String, textView: TextView, titleCharacters: Int) {
        val ss = SpannableString(text.substring(0, 270) + "... Leer más")
        val manjariBold = Typeface.createFromAsset(requireActivity().applicationContext.assets, "font/manjaribold.ttf")
        val manjariThin = Typeface.createFromAsset(requireActivity().applicationContext.assets, "font/manjarithin.ttf")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadLess(text, textView,titleCharacters)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.color = resources.getColor(R.color.black)
                }
            }
        }
        ss.setSpan(clickableSpan, ss.length - 12, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(CustomTypefaceSpan("",manjariThin), ss.length - 12, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, titleCharacters, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun addReadLess(text: String, textView: TextView, titleCharacters:Int) {
        val manjariBold = Typeface.createFromAsset(requireActivity().applicationContext.assets, "font/manjaribold.ttf")
        val manjariThin = Typeface.createFromAsset(requireActivity().applicationContext.assets, "font/manjarithin.ttf")
        val ss = SpannableString("$text leer menos")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadMore(text, textView,titleCharacters)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.color = resources.getColor(R.color.black)
                }
            }
        }
        ss.setSpan(clickableSpan, ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(CustomTypefaceSpan("",manjariThin), ss.length - 10, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(CustomTypefaceSpan("",manjariBold), 0, titleCharacters, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}

