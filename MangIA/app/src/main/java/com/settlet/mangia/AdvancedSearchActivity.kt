package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.settlet.mangia.databinding.ActivityAdvancedSearchBinding

class AdvancedSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdvancedSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvancedSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        
    }
}