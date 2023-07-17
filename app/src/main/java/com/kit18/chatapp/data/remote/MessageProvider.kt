package com.kit18.chatapp.data.remote

import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.kit18.chatapp.data.model.Message
import kotlinx.coroutines.tasks.await

class MessageProvider {
    private val db = FirebaseFirestore.getInstance()

    fun sendMessage(conversationId: String, message: Message, callback: (Boolean) -> Unit) {


        db.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener { documentReference ->
                callback(true)
            }
            .addOnFailureListener { e ->
                callback(false)
                println("Error adding document: $e")
            }
    }

    suspend fun getMessages(conversationId: String, callback: (ArrayList<Message>) -> Unit) {
        try {
            val messages = ArrayList<Message>()
            val collectionRef = db.collection("conversations")
                .document(conversationId)
                .collection("messages")

            collectionRef.orderBy("createAt").addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Toast.makeText(null, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    callback(ArrayList())
                    return@addSnapshotListener
                }

                messages.clear()
                for (document in snapshot?.documents ?: emptyList()) {
                    val message = document.toObject(Message::class.java)
                    message?.let { messages.add(it) }
                }
                callback(messages)
            }
        } catch (e: Exception) {
            Toast.makeText(null, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            callback(ArrayList())
        }
    }



}