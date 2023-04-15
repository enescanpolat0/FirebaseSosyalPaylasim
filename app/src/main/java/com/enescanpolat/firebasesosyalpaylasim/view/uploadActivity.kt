package com.enescanpolat.firebasesosyalpaylasim.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.enescanpolat.firebasesosyalpaylasim.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class uploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher : ActivityResultLauncher<String>
    var selectedpicture : Uri? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        registerLauncher()
        auth = Firebase.auth
        firestore=Firebase.firestore
        storage = Firebase.storage

    }




    fun selectimage(view: View){

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)==true){
                Snackbar.make(view,"Galeriye gitmek için izniniz gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("Izin ver" ){
                    permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentGallery =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentGallery)
        }


    }

    fun save(view:View){

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imagereference = reference.child("images").child(imageName)

        if(selectedpicture!=null){
            imagereference.putFile(selectedpicture!!).addOnSuccessListener {
                //download-url->firestore
                val uploadPictureRefrence = storage.reference.child("images").child(imageName)
                uploadPictureRefrence.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()


                    if (auth.currentUser !=null){

                        val postMap = hashMapOf<String,Any>()


                        postMap.put("downloadUrl",downloadUrl)
                        postMap.put("userEmail",auth.currentUser!!.email!!)
                        postMap.put("command",binding.yorumText.text.toString())
                        postMap.put("date",com.google.firebase.Timestamp.now())


                        firestore.collection("Posts").add(postMap).addOnSuccessListener {

                            finish()


                        }.addOnFailureListener {
                            Toast.makeText(this@uploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }



                    }


                }





            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }


    }


    private fun registerLauncher(){

        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->

            if (result.resultCode== RESULT_OK){
                val intentfromResult = result.data
                if (intentfromResult!=null){
                    selectedpicture = intentfromResult.data
                    selectedpicture?.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }

        }

        permissionResultLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->

            if (result){

                val intentGallery =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentGallery)

            }else{
                Toast.makeText(this@uploadActivity,"Izininiz gerekli",Toast.LENGTH_LONG).show()
            }

        }

    }


}