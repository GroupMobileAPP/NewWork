package com.example.work

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class NewTask : AppCompatActivity() {
    var id = 0
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.new_task)
        initial()
        //onClick()

    }



    private fun initial() { //初始化组件

        val proDate = findViewById<EditText>(R.id.newtask_date)
        val proTask = findViewById<EditText>(R.id.newtask)
        val showDate = findViewById<EditText>(R.id.newtask_date)
        //val delete = findViewById<Button>(R.id.newactivity_delete)
        var proMem = findViewById<EditText>(R.id.newtask_member)
        var submit = findViewById<Button>(R.id.newtask_submit)


        showDate.inputType = InputType.TYPE_NULL
        showDate.setOnClickListener {//显示日历
            createCalender()
        }


        submit.setOnClickListener{
             if(proDate.text.isEmpty()){
                android.widget.Toast.makeText(baseContext, "Please enter the end date",
                    android.widget.Toast.LENGTH_SHORT).show()
            }else if(proTask.text.isEmpty()){
                android.widget.Toast.makeText(baseContext, "Please add at least one task ",
                    android.widget.Toast.LENGTH_SHORT).show()
            }else if(proMem.text.isEmpty()){
                android.widget.Toast.makeText(baseContext, "Please add at least one member ",
                    android.widget.Toast.LENGTH_SHORT).show()
            }else {
                AlertDialog.Builder(this).apply {
                    //构建一个对话框
                    //apply标准函数自动返回调用对象本身
                    //setTitle("警告")//表示
                    setMessage("Task added successfully")//内容
                    setCancelable(false)//是否使用Back关闭对话框
                    setPositiveButton("continue") {//确认按钮点击事件
                            dialog, which ->
                    }


                    show()
                }
                id=id+1
                updataDatabase()
                 startActivity(Intent(this@NewTask, ProjectActivity::class.java))
            }
        }

    }




    @SuppressLint("SetTextI18n")
    private fun createCalender(){
        val showDate = findViewById<EditText>(R.id.newtask_date)
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

    fun updataDatabase(){
        var bundle:Bundle?=intent.extras

        var proName: String? = bundle!!.getString("project_name")


        val proDate = findViewById<EditText>(R.id.newtask_date)
        val proTask = findViewById<EditText>(R.id.newtask)
        var proMem = findViewById<EditText>(R.id.newtask_member)
        val user = FirebaseAuth.getInstance().currentUser
        val user_email = "${user?.email}"

        val newtask = db.collection("projects").document("$proName")
            .collection("task").document("${proTask.text}")

        newtask.set(mapOf(
            "name" to "${proTask.text}",
            "status" to "On Going",
            "date" to "${proDate.text}",
            "email" to "${proMem.text}"
        ))

        db.collection("tasks").document("${proTask.text}")
            .set(mapOf(
                "name" to "${proTask.text}",
                "status" to "On Going",
                "date" to "${proDate.text}",
                "email" to "${proMem.text}"
            ))
            .addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }




    }



}