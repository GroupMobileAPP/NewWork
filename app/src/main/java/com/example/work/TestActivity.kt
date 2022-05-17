package com.example.work

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.example.work.databinding.ActivityMainBinding
import com.example.work.databinding.TestpageBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.testpage.*
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class TestActivity : AppCompatActivity() {
    lateinit var binding: TestpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TestpageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setOnClickListener{

        }
        binding.getImage.setOnClickListener{
            getpicture()
        }
    }

    private fun getpicture() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Waiting....")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val name = binding.editTextTextPersonName.text.toString()
        val storageRef= FirebaseStorage.getInstance().reference.child("images/$name")
        val localfile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localfile).addOnSuccessListener {

            if(progressDialog.isShowing) progressDialog.dismiss()
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageView.setImageBitmap(bitmap)

        }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show()
        }
    }

//    private fun uploadImage() {
//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Waiting....")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
//
//        val formatter=SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",Locale.getDefault())
//        val now = Date()
//        val fileName = formatter.format(now)
//        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
//        storageReference.putFile(ImageUri).
//                addOnFailureListener {
//                    if(progressDialog.isShowing) progressDialog.dismiss()
//                    Toast.makeText(this,"FALSE",Toast.LENGTH_SHORT).show()
//                }.addOnSuccessListener {
//                    binding.imageView.setImageURI(null)
//                Toast.makeText(this,"sucess",Toast.LENGTH_SHORT).show()
//            if(progressDialog.isShowing) progressDialog.dismiss()
//        }
//
//    }
//
//    private fun selectImage() {
//        val intent=Intent()
//        intent.type="image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent,100)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode==100 && resultCode== RESULT_OK){
//            ImageUri= data?.data!!
//            binding.imageView.setImageURI(ImageUri)
//        }
//    }
}