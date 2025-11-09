package com.example.aless_finals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUser: TextView = view.findViewById(R.id.tvUser)
        val cardView: CardView = view.findViewById(R.id.cardUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_card, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvUser.text =
            "👤 ${user.firstName} ${user.lastName}\n🔑 ${user.username} | ${user.password}"
        holder.cardView.elevation = 8f
    }

    override fun getItemCount(): Int = userList.size
}
