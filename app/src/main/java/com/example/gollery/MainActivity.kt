package com.example.gollery

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gollery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter

    private val imageLoadLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            updateImages(uriList)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when (item.itemId){
            R.id.add_image -> {
                checkPermission()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolBar.apply {
            title = "Gallery permission test"
            setSupportActionBar(this)

        }


        initRecyclerView()

        binding.btnLoadImage.setOnClickListener {
            checkPermission()
        }

        binding.navigateFrameActivityButtom.setOnClickListener {

            navigateToFrameActivity()
        }
    }


    private fun navigateToFrameActivity() {
        val images = imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map { it.uri.toString() }.toTypedArray()
        val intent = Intent(this, FrameActivity::class.java)
            .putExtra("uriList", images)

        startActivity(intent)
    }
    private fun checkPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                loadImage()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionInfoDialog()
            }
            else -> {
                requestMediaStorage()
            }
        }
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this)
            .setMessage("Rasm olib kelish uchun ruxsat kerak!")
            .setNegativeButton("Bekor", null)
            .setPositiveButton("OK") { _, _ -> requestMediaStorage() }
            .show()
    }

    private fun requestMediaStorage() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        ActivityCompat.requestPermissions(
            this,
            permissions,
            REQUEST_CODE_FOR_MEDIA_PERMISSION
        )
    }

    private fun loadImage() {
        imageLoadLauncher.launch("image/*")
    }

    private fun updateImages(uriList: List<Uri>) {
        Log.d("image", "updateImages: $uriList")

        val images = uriList.map { ImageItems.Image(it) }
        val updatedList = imageAdapter.currentList.toMutableList().apply {
            addAll(images)
        }
        imageAdapter.submitList(updatedList)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_FOR_MEDIA_PERMISSION) {
            val resultCode = grantResults.firstOrNull() ?: PackageManager.PERMISSION_DENIED
            if (resultCode == PackageManager.PERMISSION_GRANTED) {
                loadImage()
            } else {
                Toast.makeText(this, "Ruxsat berilmadi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView() {
        imageAdapter = ImageAdapter(object : ImageAdapter.ItemClickListener {
            override fun onItemClick() {
                checkPermission()
            }
        })

        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    companion object {
        const val REQUEST_CODE_FOR_MEDIA_PERMISSION = 100
    }
}
