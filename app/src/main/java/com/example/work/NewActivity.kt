package com.example.work


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class NewActivity : AppCompatActivity() {

    lateinit var storage: FirebaseStorage
    val db = Firebase.firestore
    private lateinit var name : EditText
    private lateinit var date : EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var submit : Button
    private lateinit var imageView : ImageView
    private lateinit var imageUri: Uri

    companion object{
        val IMAGE_REQUEST_CODE=100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.newactivity)
        date = findViewById<EditText>(R.id.newactivity_date)
        submit = findViewById<Button>(R.id.newactivity_submit)
        imageView = findViewById<CircleImageView>(R.id.newactivity_image)
        name = findViewById<EditText>(R.id.newactivity_projectname)
        initial()

    }

    private fun initial() { //初始化组件


        date.inputType = InputType.TYPE_NULL
        date.setOnClickListener {//显示日历
            createCalender()
        }
        submit.setOnClickListener{
            uploadData()
        }
        imageView.setOnClickListener{
            pickImageGallery()
        }

    }

    private fun uploadData() { //上传数据

        val user = FirebaseAuth.getInstance().currentUser
        val project = hashMapOf(
            "name" to "${name.text}",
            "date" to "${date.text}",
            "manager" to "${user?.email}",
            "status" to "Unfinished",
        )
        if(name.text.isEmpty()){
            Toast.makeText(
                baseContext, "Please enter the Project Name.",
                Toast.LENGTH_SHORT
            ).show()
        }else if (date.text.isEmpty()){
            Toast.makeText(
                baseContext, "Please enter Deadline.",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            db.collection("projects").document()
                .set(project)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                    if(imageUri==null){
                        startActivity(Intent(this@NewActivity, MainActivity::class.java))
                    }else{
                        uploadImage()
                        startActivity(Intent(this@NewActivity, MainActivity::class.java))
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }

    }

    private fun uploadImage(){//上传图片

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Waiting....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val fileName = name.text
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.putFile(imageUri).
        addOnFailureListener {
            if(progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this,"FALSE",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
            if(progressDialog.isShowing) progressDialog.dismiss()
        }
    }



    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type= "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            imageUri = data?.data!!
            imageView.setImageURI(imageUri)
            uploadImage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createCalender(){
        val showDate = findViewById<EditText>(R.id.newactivity_date)
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this,

            { _, year, monthOfYear, dayOfMonth ->
                showDate.setText(
                    "$monthOfYear-$dayOfMonth-$year"
                )
            }, mYear, mMonth, mDay)
        dpd.datePicker.minDate= System.currentTimeMillis()
        dpd.show()
    }

//        val myLinearLayout = findViewById<LinearLayout>(R.id.taskview)
//        val child = EditText(this)
//        child.textSize = 20f
//
//        child.setText("454564564564")
//        // 调用一个参数的addView方法
//        myLinearLayout.addView(child)


//    private fun deleteView() {
//        val id = thisButton
//        val child = LinearLayoutManager(this)
//        val layoutManager: LayoutManager = RecyclerView.LayoutManager(this)
//    }
//    fun delete(position: Int){
//        list.removeAt(position)
//        notifyDataSetChanged()
//    }
}