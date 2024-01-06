package com.example.homework_18_8

import android.os.Bundle
import android.widget.Adapter
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.homework_18_8.databinding.ActivityMediaFileBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MediaFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaFileBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

    val mediaFiles = viewModel.getListOfFiles()
        val adapter = MyAdapter()
        adapter.setData(mediaFiles)
        binding.recycler.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allFiles.collect() {
                    adapter.setData(it)
                }
            }
        }
    }


}