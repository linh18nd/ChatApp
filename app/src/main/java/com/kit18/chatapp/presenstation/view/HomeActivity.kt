package com.kit18.chatapp.presenstation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kit18.chatapp.R
import com.kit18.chatapp.data.adapter.UserAdapter
import com.kit18.chatapp.data.model.UserModel

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: ArrayList<UserModel> // Danh sách người dùng (thay thế bằng dữ liệu thực tế)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        userList = getUserList()


        recyclerView = findViewById(R.id.recyclerViewUsers)
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun getUserList(): ArrayList<UserModel> {
        val firestore = FirebaseFirestore.getInstance()
        val data = firestore.collection("users").get()
        var userList = ArrayList<UserModel>()
        data.addOnSuccessListener { documents ->
            for (document in documents) {
                val user = document.toObject(UserModel::class.java)
                userList.add(user)
            }
        }
        return userList
    }
}