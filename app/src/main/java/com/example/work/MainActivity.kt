package com.example.work

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.work.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab: FloatingActionButton = binding.fab
        initialUI()//初始化界面
//        val user = FirebaseAuth.getInstance().currentUser//判断是否保持登录状态
//        Toast.makeText(baseContext, "用户邮箱：${user?.email}", Toast.LENGTH_SHORT).show()

        fab.setOnClickListener {
            updateUI()
        }

        binding.exitBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, Login::class.java))
        }

    }


    private fun updateUI() {  //界面跳转
        startActivity(Intent(this, NewActivity::class.java))
    }


    private fun initialUI(){
        auth= Firebase.auth
        val user = FirebaseAuth.getInstance().currentUser
        val user_email = "${user?.email}"
        val query=db.collection("projects").whereEqualTo("manager",user_email)
        val position = db.collection("users").document(user_email)
        position.get().addOnSuccessListener {
            if("${it.get("position")}"=="Manager") fab.isVisible = true
        }


        val options= FirestoreRecyclerOptions.Builder<Project>().setQuery(query,Project::class.java)
            .setLifecycleOwner(this).build()
        val adapter = object : FirestoreRecyclerAdapter<Project, UserViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view = LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.item_board, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: Project) {

                val taskname: TextView = holder.itemView.findViewById(R.id.task_name)
                val taskdate: TextView = holder.itemView.findViewById(R.id.task_date)
                var taskstatus: TextView = holder.itemView.findViewById(R.id.task_status)
                val image: ImageView = holder.itemView.findViewById(R.id.iv_board_image)
                val enterBtn: LinearLayout = holder.itemView.findViewById(R.id.project_card)

                taskname.text= model.name
                taskdate.text= "Deadline:"+ model.date
                taskstatus.text = model.status
                enterBtn.setOnClickListener{//卡片点击事件
                    startActivity(Intent(this@MainActivity, NewActivity::class.java))
                }
                getpicture(image, taskname)
            }
        }
        task_view.adapter= adapter
        task_view.layoutManager = LinearLayoutManager(this)
    }


    private fun getpicture(image: ImageView, name :TextView) {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val filename=name.text
        val storageRef= FirebaseStorage.getInstance().reference.child("images/$filename")
        val localfile = File.createTempFile("tempImage","jpg")
        storageRef.getFile(localfile).addOnSuccessListener {

            if(progressDialog.isShowing) progressDialog.dismiss()
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            image.setImageBitmap(bitmap)

        }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()

        }
    }
}