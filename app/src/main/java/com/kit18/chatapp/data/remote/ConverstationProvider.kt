package com.kit18.chatapp.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kit18.chatapp.data.model.Converstation
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
        val converstation = Converstation(
            id,
            System.currentTimeMillis(),
            "private",
            "Cuộc trò chuyện mới",
            System.currentTimeMillis(),
            participants
        )
        db.collection("converstations")
            .add(converstation)
            .addOnSuccessListener { documentReference ->
                callback(documentReference.id)
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

    suspend fun getConverstation(friendId: String, callback: (String) -> Unit) {
        val uid = mAuth.uid

        val data = withContext(Dispatchers.IO) {
            db.collection("converstations")
                .whereArrayContains("participants", uid!!)

                .get()
                .await()
        }

        for (document in data) {
            val converstation = document.toObject(Converstation::class.java)
            if (converstation.participants.contains(friendId)) {
                callback(converstation.converstationId!!)
                return
            }

        }
        arrayListOf(uid, friendId).let {
            createConverstation(it) { conversationId ->
                callback(conversationId)
            }
        }
    }

    fun getMessages(conversationId: String, callback: (MutableList<Message>) -> Unit) {
        db.collection("converstations")
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
