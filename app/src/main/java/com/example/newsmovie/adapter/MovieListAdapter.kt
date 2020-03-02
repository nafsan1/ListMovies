package com.example.newsmovie.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.newsmovie.R
import com.example.newsmovie.databinding.ItemMovieBinding
import com.example.newsmovie.model.Movie
import com.example.newsmovie.view.ListMovieFragmentDirections
import com.example.newsmovie.view.MovieClickListener


class MovieListAdapter(private val movieList: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieListAdapter.AnimalViewHolder>(),
    MovieClickListener {

    private lateinit var listener:OnItemOnClick

    fun updateMovieList(movie: List<Movie>) {
        movieList.clear()
        movieList.addAll(movie)
        notifyDataSetChanged()
    }
    fun SetOnItemClick(listener: OnItemOnClick) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)
        return AnimalViewHolder(view)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {

        holder.view.viewmodel = movieList[position]
        val movie:Movie = movieList[position]
        holder.itemView.setOnClickListener {
            listener.onItemClick(movie,it)
        }

        holder.view.listener = this

    }

    override fun onClick(v: View) {


    }

    class AnimalViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root)

    interface OnItemOnClick {
        fun onItemClick(movie:Movie,view:View)
    }
}