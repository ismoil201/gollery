package com.example.gollery

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gollery.databinding.ActivityFrameBinding
import com.google.android.material.tabs.TabLayoutMediator

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val images  = (intent.getStringArrayExtra("uriList")?: emptyArray()).map {
            uriString -> FrameItem(uriString.toUri())
        }

        binding.viewPager2.adapter = FrameAdapter(images)
        binding.viewPager2.setCurrentItem(intent.getIntExtra("position",0),false)


        TabLayoutMediator(binding.tabLayout,binding.viewPager2, ){
            tab, position ->
               binding.viewPager2.currentItem =  tab.position

        }.attach()
    }
}