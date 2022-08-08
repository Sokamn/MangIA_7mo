package com.settlet.mangia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.settlet.mangia.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}