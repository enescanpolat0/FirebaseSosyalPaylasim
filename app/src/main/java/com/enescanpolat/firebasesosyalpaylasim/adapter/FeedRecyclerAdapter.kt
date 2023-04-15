package com.enescanpolat.firebasesosyalpaylasim.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enescanpolat.firebasesosyalpaylasim.databinding.RecyclerRowBinding
import com.enescanpolat.firebasesosyalpaylasim.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(private val postlist : ArrayList<Post>):RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {


    class PostHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
       val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text=postlist.get(position).email
        holder.binding.recyclerCommandText.text=postlist.get(position).command
        Picasso.get().load(postlist.get(position).downloadUrl).into(holder.binding.recylerImageview)

    }

    override fun getItemCount(): Int {
        return postlist.size
    }
}