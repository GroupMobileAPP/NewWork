package com.example.work


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Register : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.register)
        onClick()

    }


    private fun onClick() {
        val email = findViewById<EditText>(R.id.register_email)
        val password = findViewById<EditText>(R.id.register_pwd)
        val  button1 = findViewById<Button>(R.id.register_submit)

        button1.setOnClickListener{//点击登陆
            if(email.text.isEmpty()  ){   //未输入邮箱 报错
                Toast.makeText(baseContext, "Please enter the E-mail",
                    Toast.LENGTH_SHORT).show()
            }else if(password.text.isEmpty()){   //未输入密码 报错
                android.widget.Toast.makeText(baseContext, "Please enter the Password",
                    android.widget.Toast.LENGTH_SHORT).show()
            }else {//进行登陆验证
                registerEvent(
                    email.text.toString(), password.text.toString()
                )
            }
        }
    }


    private fun registerEvent(email: String, password: String) {
        // [START create_user_with_email]
        val name = findViewById<EditText>(R.id.register_name)
        val position = findViewById<RadioGroup>(R.id.group_position)





        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (name.text.isEmpty()) {
                    Toast.makeText(
                        baseContext, "Please enter your Name.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (position.checkedRadioButtonId == -1) { //未选择职位
                        Toast.makeText(
                            baseContext, "Please choose the Position.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (task.isSuccessful) { //成功
                            // Sign in success, update UI with the signed-in user's information

                            updataDatabase()
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        // [END create_user_with_email]
    }




    fun updataDatabase(){
        val email = findViewById<EditText>(R.id.register_email)
        val name = findViewById<EditText>(R.id.register_name)
        val member = findViewById<RadioButton>(R.id.register_member)
        val manager = findViewById<RadioButton>(R.id.register_manager)
        val position = findViewById<RadioGroup>(R.id.group_position)
        val radioButtonText = when (position.checkedRadioButtonId) {
            R.id.register_member -> member.text
            else -> manager.text
        }

        val user = hashMapOf(
            "name" to "${name.text}",
            "position" to "$radioButtonText"
        )

        db.collection("users").document("${email.text}")
                .set(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

    }



    private fun updateUI(user: FirebaseUser?) {  //界面跳转
        startActivity(Intent(this@Register, Login::class.java))
    }
}