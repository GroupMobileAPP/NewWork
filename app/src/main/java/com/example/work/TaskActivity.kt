//package com.example.work
//
//import android.app.ProgressDialog
//import android.content.ContentValues
//import android.content.Intent
//import android.graphics.BitmapFactory
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.isVisible
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.work.databinding.ActivityMainBinding
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.FirebaseStorage
//import kotlinx.android.synthetic.main.activity_main.*
//import java.io.File
//
//class TaskActivity : AppCompatActivity(){
//    //val name:String=intent.getStringExtra("project_name").toString()
//
////    val name=intent.getStringExtra("project_name")
////    val nametext=name.toString()
//    private val db = Firebase.firestore
//    private lateinit var auth: FirebaseAuth
//    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
//
//    private lateinit var binding: ActivityMainBinding
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_task)
//        //setContentView(binding.root)
//
//        val fab=findViewById<Button>(R.id.button2)
//        initialUI()//初始化界面
////        val user = FirebaseAuth.getInstance().currentUser//判断是否保持登录状态
////        Toast.makeText(baseContext, "用户邮箱：${user?.email}", Toast.LENGTH_SHORT).show()
//
//
//
//
//
//    }
//
//
//
//    private fun initialUI() {
//        //Log.d(ContentValues.TAG, name)
//        var bundle: Bundle? = intent.extras
//        if (bundle != null) {
//            var nametext: String? = bundle!!.getString("project_name")
//
//            auth = Firebase.auth
//            val query = db.collection("projects").document("$nametext").collection("task")
//            val options =
//                FirestoreRecyclerOptions.Builder<Project>().setQuery(query, Project::class.java)
//                    .setLifecycleOwner(this).build()
//            val adapter = object : FirestoreRecyclerAdapter<Project, UserViewHolder>(options) {
//                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//                    val view = LayoutInflater.from(this@TaskActivity)
//                        .inflate(R.layout.item_board, parent, false)
//                    return UserViewHolder(view)
//                }
//
//                override fun onBindViewHolder(
//                    holder: UserViewHolder,
//                    position: Int,
//                    model: Project
//                ) {
//
//                    val taskname: TextView = holder.itemView.findViewById(R.id.task_name)
//                    val taskdate: TextView = holder.itemView.findViewById(R.id.task_date)
//                    var taskstatus: TextView = holder.itemView.findViewById(R.id.task_status)
//                    //val image: ImageView = holder.itemView.findViewById(R.id.iv_board_image)
//                    val enterBtn: LinearLayout = holder.itemView.findViewById(R.id.project_card)
//
//                    taskname.text = model.name
//                    taskdate.text = "Deadline:" + model.date
//                    taskstatus.text = model.status
//                    enterBtn.setOnClickListener {//卡片点击事件
//                        val intent = Intent(this@TaskActivity, TaskActivity::class.java)
//                        intent.putExtra("project_name", "${taskname.text}")
//                        startActivity(intent)
//                        //startActivity(Intent(this@MainActivity, NewActivity::class.java))
//                    }
//                    //getpicture(image, taskname)
//                }
//            }
//            task_view.adapter = adapter
//            task_view.layoutManager = LinearLayoutManager(this)
//        }
//    }
//
//
//
//
//
//
//}
//
//
