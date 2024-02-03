package com.dicoding.myuserstory.ui.story

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.dicoding.myuserstory.R
import com.dicoding.myuserstory.databinding.ActivityStoryBinding
import com.dicoding.myuserstory.ui.main.MainActivity
import com.dicoding.myuserstory.utils.*
import com.google.android.gms.location.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStoryBinding
    private val viewModel : StoryViewModel by viewModels()
    private var getFile : File? = null
    private lateinit var dataPref: DataPref

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        dataPref = DataPref(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        setContentView(binding.root)
        setupView()
        setToolbar()
        binding.btnCamera.setOnClickListener{startTakePhoto()}
        binding.btnGallery.setOnClickListener{ startGallery()}
        binding.btnSubmit.setOnClickListener {uploadImage()}

    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@StoryActivity,
                "com.dicoding.myuserstory.ui.story",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private lateinit var currentPhotoPath : String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.preview.setImageBitmap(result)
            binding.preview.isVisible = true
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@StoryActivity)

            getFile = myFile

            binding.preview.setImageURI(selectedImg)
            binding.preview.isVisible = true
        }
    }

    private fun uploadImage() {
        showProgress(true)

        if (getFile != null) {
            val file = reduceFileImg(getFile as File)

            val description = binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            viewModel.apply {
                    if (dataPref.isLogin()) {
                        if(CheckPermission()){
                            if(isLocationEnabled()){
                                if (ActivityCompat.checkSelfPermission(
                                        applicationContext,
                                        ACCESS_FINE_LOCATION
                                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                        applicationContext,
                                        ACCESS_COARSE_LOCATION
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    return
                                }
                                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                                    val userLocation : Location? = task.result
                                    if(userLocation == null){
                                        NewLocationData()
                                    }else{
                                        postStoryWithLocation(imageMultipart, description,userLocation.latitude, userLocation.longitude, this@StoryActivity)
                                    }
                                }
                            }else{
                                Toast.makeText(this@StoryActivity,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            RequestPermission()
                        }
                    } else {
                        postStoryForGuest(this@StoryActivity,imageMultipart, description)
                    }
                    state.observe(this@StoryActivity) {
                        if(it == true) {
                            showProgress(false)
                            Toast.makeText(this@StoryActivity, resources.getString(R.string.post_success_message), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@StoryActivity, MainActivity::class.java))
                            finish()
                        } else {
                            showProgress(false)
                            Toast.makeText(this@StoryActivity, resources.getString(R.string.post_failed_message), Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        } else {
            Toast.makeText(this@StoryActivity, resources.getString(R.string.null_photo_message), Toast.LENGTH_SHORT).show()
        }
    }

    fun NewLocationData(){
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location? = locationResult.lastLocation
            if (lastLocation != null) {
                Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString() + " "+ lastLocation.latitude.toString())
            }
        }
    }

    private fun CheckPermission():Boolean{
        if(
            ActivityCompat.checkSelfPermission(this,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun showProgress(state: Boolean) {
        binding.progress.isVisible = state
    }

}