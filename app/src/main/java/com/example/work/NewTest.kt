//package com.example.work
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import kotlinx.android.synthetic.main.newtest.*
//
//
//class NewTest : AppCompatActivity() {
//
//
//    private val db = Firebase.firestore
//    private lateinit var auth: FirebaseAuth
//
//    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.newtest)
//
//
//        //实时获取数据并更新UI
//        auth=Firebase.auth
//        val query=db.collection("projects")
//        val options= FirestoreRecyclerOptions.Builder<Project>().setQuery(query,Project::class.java)
//            .setLifecycleOwner(this).build()
//        val adapter = object : FirestoreRecyclerAdapter<Project, UserViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//                val view = LayoutInflater.from(this@NewTest)
//                    .inflate(R.layout.item_board, parent, false)
//                return UserViewHolder(view)
//            }
//
//            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: Project) {
//                val taskname: TextView = holder.itemView.findViewById(R.id.task_name)
//                val taskdate: TextView = holder.itemView.findViewById(R.id.task_date)
//                taskname.text= model.name
//                taskdate.text= "Deadline:"+ model.date
//            }
//        }
//        rv_text.adapter= adapter
//        rv_text.layoutManager = LinearLayoutManager(this)
//
//
//
//    }
//}