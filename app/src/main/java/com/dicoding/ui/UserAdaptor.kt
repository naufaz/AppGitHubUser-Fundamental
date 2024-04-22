package com.dicoding.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.databinding.ItemRowUsersBinding
import com.dicoding.model.Items

class UserAdaptor : RecyclerView.Adapter<UserAdaptor.ListViewHolder>() {

    private val listUsers = ArrayList<Items>()
    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ListViewHolder(var binding:ItemRowUsersBinding) :
            RecyclerView .ViewHolder(binding.root) {
                fun bind(users: Items) {
                    binding.root.setOnClickListener {
                        onItemClickCallback?.onItemClicked(users)
                    }

                    binding.apply {
                        tvUname.text = users.login
                        tvLink.text = users.htmlUrl
                        Glide.with(itemView)
                            .load(users.avatarUrl)
                            .into(imgAvatar)
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemRowUsersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setListUser(users: ArrayList<Items>) {
        listUsers.clear()
        listUsers.addAll(users)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Items)
    }
}