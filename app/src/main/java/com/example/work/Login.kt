package com.example.work

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Login : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.login)

        onClick()
        test()
    }



    private fun test() {
        val  button = findViewById<Button>(R.id.test)
        val user = hashMapOf(
            "first" to "dd",
            "last" to "Lovelace",
            "born" to 1815
        )
        button.setOnClickListener{







//            数据库读取

//              全部读取
//             val user1=db.collectionGroup("projects")
//            user1.get().addOnSuccessListener {
//                it.forEach{ it ->
//                    Log.d(TAG, "name:${it.get("name")}   ;  date:${it.get("date")}")
//                }
//
//            }


//            db.collection("users")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        Toast.makeText(baseContext, "${document.data}",
//                            Toast.LENGTH_SHORT).show()
//
//                        Log.d(TAG, "${document.id} => ${document.data}")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents.", exception)
//                }



            //数据库写入
//            db.collection("users")
//                .add(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w(TAG, "Error adding document", e)
//                }
        }
    }









    private fun onClick() {
        val email = findViewById<EditText>(R.id.login_email)
        val password = findViewById<EditText>(R.id.login_pwd)
        val  button1 = findViewById<Button>(R.id.login_login)
        button1.setOnClickListener{//点击登陆
            if(email.text.isEmpty()){   //未输入邮箱 报错
                Toast.makeText(baseContext, "Please enter the E-mail",
                    Toast.LENGTH_SHORT).show()
            }else if(password.text.isEmpty()){   //未输入密码 报错
                android.widget.Toast.makeText(baseContext, "Please enter the Password",
                    android.widget.Toast.LENGTH_SHORT).show()
            }else {//进行登陆验证
                loginEvent(
                    email.text.toString(), password.text.toString()
                )
            }

        }


        val button2 = findViewById<Button>(R.id.login_register)

        button2.setOnClickListener{//注册按钮
            startActivity(Intent(this@Login, Register::class.java))
        }
    }

    private fun loginEvent(email: String, password: String) {//登录验证
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {//成功
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {//密码错误
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {  //界面跳转
        startActivity(Intent(this@Login, MainActivity::class.java))
    }


}
