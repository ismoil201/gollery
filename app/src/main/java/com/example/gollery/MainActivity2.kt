package com.example.gollery

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gollery.databinding.ActivityMain2Binding
import com.example.gollery.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {


    private val imageLoadLaucher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) {
        imageUri ->
        updateImages(imageUri)

    }
    private lateinit var binding : ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnLoadImage.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission(){

        val permission = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            android.Manifest.permission.READ_MEDIA_IMAGES
        }else{
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }


        when {

            ActivityCompat.checkSelfPermission(this, permission)==android.content.pm.PackageManager.PERMISSION_GRANTED ->{
                loadImages()
            }
            shouldShowRequestPermissionRationale(permission)->{
                showPermissionInfoDialog()
            }


            else ->{
                requestPermission()
            }
        }
    }

    private fun showPermissionInfoDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("This app require access to your photos")
            setNegativeButton("cancel", null)
            setPositiveButton("ok",) {_,_ ->
                requestPermission()
            }


        }.show()

        }


    private fun requestPermission() {
        val permission = if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO)
        }else{
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        ActivityCompat.requestPermissions(this, permission,
            REQUEST_CODE_FOR_MEDIA_PERMISSION)

    }

    private fun loadImages() {
        imageLoadLaucher.launch("image/*")

    }


    private fun updateImages(imageUri: List<Uri>) {

        Log.d("updateImage", "updateImages: $imageUri")


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

        when(requestCode){
           REQUEST_CODE_FOR_MEDIA_PERMISSION ->{

               if(grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED){
                   loadImages()
               }
           }
        }
    }
    companion object{
        const val REQUEST_CODE_FOR_MEDIA_PERMISSION = 100
    }

}

