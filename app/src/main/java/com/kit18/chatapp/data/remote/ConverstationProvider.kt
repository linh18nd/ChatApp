package com.kit18.chatapp.data.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kit18.chatapp.data.model.Conversation
import com.kit18.chatapp.data.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class ConverstationProvider {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun createConverstation(participants: ArrayList<String?>, callback: (String) -> Unit) {
        val id = UUID.randomUUID().toString()
        val conversation = Conversation(
            id,
            System.currentTimeMillis(),
            "private",
            "Cuộc trò chuyện mới",
            System.currentTimeMillis(),
            participants
        )
        db.collection("conversations").document(id)
            .set(conversation)
            .addOnSuccessListener { documentReference ->
                callback(id)
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

    suspend fun getConverstation(friendId: String, callback: (String) -> Unit) {
        val uid = mAuth.uid

        val data = withContext(Dispatchers.IO) {
            db.collection("conversations")
                .whereArrayContains("participants", uid!!)

                .get()
                .await()
        }
val conversation = data.toObjects(Conversation::class.java)
        for (document in data) {
            Log.d("TAG", "${document.id} => ${document.data}")
            val conversation = document.toObject(Conversation::class.java)
            if (conversation!!.participants.contains(friendId)) {
                callback(conversation.conversationId!!)
                return
            }

        }

        arrayListOf(uid, friendId).let {
            createConverstation(it) { conversationId ->
                callback(conversationId)
            }
        }
    }

    suspend fun getMessages(conversationId: String, callback: (MutableList<Message>) -> Unit) {
        db.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("createAt")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    println("Listen failed. $error")
                    return@addSnapshotListener
                }
                val messages = value!!.toObjects(String::class.java)


            }
    }
}
