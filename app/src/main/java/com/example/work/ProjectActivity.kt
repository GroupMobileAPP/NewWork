package com.example.work

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.projectactivity.*
import java.io.File

class ProjectActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    private lateinit var projectnameView : TextView
    private lateinit var imageview :ImageView
    private lateinit var charname:String
    private lateinit var chardate:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.projectactivity)

        imageview = findViewById(R.id.projectDetail_image)
        projectnameView = findViewById(R.id.projectDetail_projectname)


        initialUI()

//        btnBack.setOnClickListener{
//            startActivity(Intent(this@ProjectActivity, MainActivity::class.java))
//        }


    }

    private fun initialUI() {
        var bundle:Bundle?=intent.extras
        if(bundle!=null){
            var nametext:String?=bundle!!.getString("project_name")
            var position:String?=bundle!!.getString("position")
            val managerName=findViewById<TextView>(R.id.textView9)
            val datam=findViewById<TextView>(R.id.projectDetail_date)
            val backbtn = findViewById<Button>(R.id.projectDetail_back)
            val btnComplete = findViewById<Button>(R.id.projectDetail_complete)
            backbtn.setOnClickListener{
                startActivity(Intent(this@ProjectActivity, MainActivity::class.java))
            }
            Toast.makeText(baseContext, "用户邮箱：$position", Toast.LENGTH_SHORT).show()
            val mname=db.collection("projects").document("$nametext").get().addOnSuccessListener {
                charname="${it.get("manager")}"
                chardate="${it.get("date")}"
                if(position == "Manager"){
                    btnComplete.text = "Add Task"
                    managerName.text= charname
                    datam.text=chardate
                    btnComplete.setOnClickListener{
                        addTask()
                    }
                }else{
                    btnComplete.setOnClickListener{
                        completeTask()
                    }
                }

            }
//                .get().addOnSuccessListener {
//                it.forEach{
//                    charname="${it.get("manager")}"
//                    chardate="${it.get("date")}"
//
//                }
//            }
//            if(position == "Manager"){
//                btnComplete.text = "Add Task"
//                managerName.text= charname
//                datam.text=chardate
//                btnComplete.setOnClickListener{
//                    addTask()
//                }
//            }else{
//                btnComplete.setOnClickListener{
//                    completeTask()
//                }
//            }

            auth = Firebase.auth

            val query = db.collection("projects").document("$nametext").collection("task")
            val options =
                FirestoreRecyclerOptions.Builder<Project>().setQuery(query, Project::class.java)
                    .setLifecycleOwner(this).build()
            val adapter = object : FirestoreRecyclerAdapter<Project, UserViewHolder>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                    val view = LayoutInflater.from(this@ProjectActivity)
                        .inflate(R.layout.item_board, parent, false)
                    return UserViewHolder(view)
                }

                override fun onBindViewHolder(
                    holder: UserViewHolder,
                    position: Int,
                    model: Project
                ) {

                    val taskname: TextView = holder.itemView.findViewById(R.id.task_name)
                    val taskdate: TextView = holder.itemView.findViewById(R.id.task_date)
                    var taskstatus: TextView = holder.itemView.findViewById(R.id.task_status)
                    val enterBtn: LinearLayout = holder.itemView.findViewById(R.id.project_card)
                    var taskmem=model.name
                    //taskname.text = model.name
                    db.collection("projects").document("$nametext")
                        .collection("task").document("$taskmem").get().addOnSuccessListener {
                            taskname.text="${it.get("email")}"
                        }
                    taskdate.text = "Deadline:" + model.date
                    taskstatus.text = model.status

                }
            }
            member_view.adapter = adapter
            member_view.layoutManager = LinearLayoutManager(this)




            getpicture(imageview,nametext.toString()) //刷新图片
            projectnameView.text="$nametext" //更新名字

        }
    }

    private fun addTask() {
        var intent = Intent(this@ProjectActivity,NewTask::class.java)
        intent.putExtra("project_name","${projectnameView.text}")
        startActivity(intent)
    }

    private fun completeTask() {//改变任务状态为完成
        var bundle:Bundle?=intent.extras
        var nametext:String?=bundle!!.getString("project_name")

        val s= hashMapOf<String,Any>(
            "status" to "completed"
        )
        db.collection("tasks").document("$nametext").update(s)

    }

    fun getpicture(image: ImageView, name : String) {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()


        val filename = name
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