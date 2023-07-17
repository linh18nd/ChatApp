package com.kit18.chatapp.presenstation.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
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
        catchEvents()


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

    fun catchEvents() {
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val title = toolbar.title

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout)

            if (Math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
                // AppBarLayout đã cuộn vào hoàn toàn
                toolbar.title = "Collapsed Title"
                collapsingToolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.black))
                Log.d("TAG", "Collapsed")
            } else if (verticalOffset == 0) {
                // AppBarLayout đang mở rộng
                toolbar.title = title
                collapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.white))
                Log.d("TAG", "Expanded")
            } else {
                // Giữa hai trạng thái
                collapsingToolbarLayout.setExpandedTitleColor(resources.getColor(R.color.white))
                collapsingToolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.black))
                Log.d("TAG", "Somewhere in between")
            }
        })
    }

}