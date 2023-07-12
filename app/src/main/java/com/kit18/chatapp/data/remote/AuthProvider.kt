package com.kit18.chatapp.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kit18.chatapp.data.model.UserModel

class AuthProvider {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun saveUserToDatabase(user: UserModel) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(user.uid!!)
        userRef.set(user)
    }

    fun logOut() {
        mAuth.signOut()
    }
}
