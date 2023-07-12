package com.kit18.chatapp.data.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kit18.chatapp.R
import com.kit18.chatapp.data.model.UserModel
import com.kit18.chatapp.data.remote.ConverstationProvider
import com.kit18.chatapp.presenstation.view.ChatPageActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class UserAdapter(private val userList: ArrayList<UserModel>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textViewUserName.text = currentUser.name
        Picasso.get().load(currentUser.profileImageUrl).into(holder.userImage)
        holder.textViewUserStatus.text = currentUser.email
        holder.itemView.setOnClickListener {

            val converstationProvider = ConverstationProvider()

                runBlocking {


                    converstationProvider.getConverstation(currentUser.uid!!) {
                        if (it.isNotEmpty()) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Đang tạo cuộc trò chuyện",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(holder.itemView.context, ChatPageActivity::class.java)
                            intent.putExtra("converstationId", it)
                            intent.putExtra("receiverId", currentUser.uid)
                            holder.itemView.context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                holder.itemView.context,
                                "Không thể tạo cuộc trò chuyện",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                }
            }
        }

    }

    override fun getItemCount() = userList.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName: TextView = itemView.findViewById(R.id.txtUsername)
        val textViewUserStatus: TextView = itemView.findViewById(R.id.textViewUserStatus)
        val userImage: ImageView = itemView.findViewById(R.id.imgAvatarUrl)

    }
}
