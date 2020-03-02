package com.example.newsmovie.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager

import com.example.newsmovie.R
import com.example.newsmovie.adapter.MovieListAdapter
import com.example.newsmovie.databinding.FragmentFavoriteBinding
import com.example.newsmovie.model.Movie
import com.example.newsmovie.viewmodel.ListViewModel

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {

    private lateinit var dataBinding: FragmentFavoriteBinding
    private lateinit var viewModel: ListViewModel
    private val listAdaper: MovieListAdapter = MovieListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
       return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        showProgress()
        viewModel.getFavoriteMovie()
        setRecycle()
        setData()
    }

    private fun setRecycle(){
        dataBinding.movieListFav.apply {
            layoutManager = GridLayoutManager(activity, 1)
            adapter = listAdaper
        }

        listAdaper.SetOnItemClick(object : MovieListAdapter.OnItemOnClick{
            override fun onItemClick(movie: Movie, view:View) {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(movie)
                Navigation.findNavController(view).navigate(action)
            }

        })
    }
    private fun setData() {
        viewModel.resultMovie.observe(activity!!, Observer {data ->
            hideProgress()

            if (data.size > 0) {
                dataBinding.txtFavorite.visibility = View.GONE
                data.let { it1 -> listAdaper.updateMovieList(it1) }
            }else{
                dataBinding.movieListFav.visibility = View.GONE
                dataBinding.txtFavorite.visibility = View.VISIBLE
            }
        })

        viewModel.error.observe(activity!!, Observer {

            hideProgress()

            if (it.isNotEmpty()) {
                showError(it)
            }
        })
    }

    private fun showProgress() {
        dataBinding.loadingView.bringToFront()
        dataBinding.loadingView.visibility = View.VISIBLE
        dataBinding.movieListFav.visibility = View.GONE
    }

    private fun hideProgress() {
        dataBinding.loadingView.bringToFront()
        dataBinding.loadingView.visibility = View.GONE
        dataBinding.movieListFav.visibility = View.VISIBLE
    }

    private fun showError(error: String?) {
        Toast.makeText(activity!!, "Error $error", Toast.LENGTH_SHORT).show()
    }
}
