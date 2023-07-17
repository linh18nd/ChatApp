package com.kit18.chatapp.presenstation.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kit18.chatapp.R
import com.kit18.chatapp.data.adapter.MessageAdapter
import com.kit18.chatapp.data.model.Message
import com.kit18.chatapp.data.remote.MessageProvider
import kotlinx.coroutines.*
import java.util.UUID

class ChatPageActivity : AppCompatActivity() {
    private val messageList = ArrayList<Message>()
    private var currentUserId = ""
    private var conversationId = ""
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageProvider: MessageProvider
    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)
        coroutineScope = CoroutineScope(Dispatchers.Main)
        init()
        setupRecyclerView()
        catchEvents()
    }

    private fun catchEvents() {
        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.sendButton).setOnClickListener {
            sendMessage()
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(com.google.android.material.R.drawable.material_ic_keyboard_arrow_previous_black_24dp)
        toolbar.navigationIcon?.setTint(resources.getColor( R.color.white))
        toolbar.setNavigationOnClickListener {
            finish()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun init() {
        currentUserId = intent.getStringExtra("currentId").toString()
        conversationId = intent.getStringExtra("conversationId").toString()
        messageProvider = MessageProvider()

        runBlocking {
            try {
                messageProvider.getMessages(conversationId) { messageList ->
                    this@ChatPageActivity.messageList.clear()
                    this@ChatPageActivity.messageList.addAll(messageList)
                    messageAdapter.notifyDataSetChanged()

                    val messageRecyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
                    messageRecyclerView.scrollToPosition(messageList.size - 1)
                }
            } catch (e: Exception) {
                // Xử lý lỗi nếu cần
                Toast.makeText(this@ChatPageActivity, "Lỗi khi tải tin nhắn", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messageList, currentUserId)
        val messageRecyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        // Di chuyển tới cuối danh sách
        messageRecyclerView.scrollToPosition(messageList.size - 1)
        messageAdapter.notifyDataSetChanged()
    }


    private fun sendMessage() {
        val content = findViewById<EditText>(R.id.messageEditText).text.toString()
        val id = UUID.randomUUID().toString()
        val createAt = System.currentTimeMillis()
        val senderId = currentUserId
        val type = "text"
        val isRead = false
        val messageModel = Message(id, createAt, type, content, senderId, isRead)
        messageProvider.sendMessage(conversationId, messageModel) { success ->
            if (success) {
                findViewById<EditText>(R.id.messageEditText).setText("")
                val chatRecyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
                chatRecyclerView.scrollToPosition(messageList.size - 1)
                messageAdapter.notifyDataSetChanged()
            } else {
                // Thông báo gửi tin nhắn thất bại
                Toast.makeText(this, "Gửi tin nhắn thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
