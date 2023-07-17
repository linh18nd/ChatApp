package com.kit18.chatapp.data.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kit18.chatapp.R
import com.kit18.chatapp.common.util.AppConvert
import com.kit18.chatapp.data.model.Message
import com.kit18.chatapp.data.model.UserModel
import com.squareup.picasso.Picasso


class MessageAdapter(private val messageList: ArrayList<Message>,val currentUserId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENDER = 1
    private val VIEW_TYPE_RECEIVER = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER -> {
                val itemView =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_message_sender, parent, false)
                MessageSenderViewHolder(itemView)
            }
            VIEW_TYPE_RECEIVER -> {
                val itemView =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_message_receiver, parent, false)
                MessageReceiverViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_SENDER -> {
                val messageSenderViewHolder = holder as MessageSenderViewHolder
                messageSenderViewHolder.bind(message)
            }
            VIEW_TYPE_RECEIVER -> {
                val messageReceiverViewHolder = holder as MessageReceiverViewHolder
                messageReceiverViewHolder.bind(message)
            }
        }
    }

    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.senderId == currentUserId) VIEW_TYPE_SENDER else VIEW_TYPE_RECEIVER
    }

    inner class MessageSenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageContentTextView)
        private val messageTimeTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(message: Message) {
            messageTextView.text = message.content
            messageTimeTextView.text = AppConvert.convertTime(message.createAt?.toLong() ?: 0)
        }
    }

    inner class MessageReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageContentTextView)
        private val messageTimeTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        private val userImage: ImageView = itemView.findViewById(R.id.avatarReceiverImageView)

        fun bind(message: Message) {
            messageTextView.text = message.content
            messageTimeTextView.text = AppConvert.convertTime(message.createAt?.toLong() ?: 0)

            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("users").document(message.senderId!!).get()
                .addOnSuccessListener {
                    val user = it.toObject(UserModel::class.java)
                    Picasso.get().load(user!!.profileImageUrl).into(userImage)
                }

        }
    }

}
