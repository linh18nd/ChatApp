package com.kit18.chatapp.presenstation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kit18.chatapp.R
import com.kit18.chatapp.data.adapter.UserAdapter
import com.kit18.chatapp.data.model.UserModel

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: ArrayList<UserModel>
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Log.d("TAG", "kkkkkkkkkkkkkkkkkkkkk")
        getUserList { users ->
            userList = users
            setupRecyclerView()
        }


    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewUsers)
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun getUserList(callback: (ArrayList<UserModel>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
      val uid = FirebaseAuth.getInstance().uid
        val data = firestore.collection("users").whereNotEqualTo("uid", "${uid}").get()

        var users = ArrayList<UserModel>()
        data.addOnSuccessListener { documents ->
            for (document in documents) {
                val user = document.toObject(UserModel::class.java)
                users.add(user)
                Log.d("TAG", "${user.name} => ${user.email}")
            }
            callback(users) // Gọi callback khi danh sách người dùng đã được lấy
        }
    }
}