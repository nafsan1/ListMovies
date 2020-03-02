package com.example.newsmovie.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newsmovie.MainActivity

import com.example.newsmovie.R
import com.example.newsmovie.databinding.FragmentDetailBinding
import com.example.newsmovie.model.Movie

import com.example.newsmovie.viewmodel.ListViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var movie: Movie
    private lateinit var dataBinding: FragmentDetailBinding
    private lateinit var viewModel: ListViewModel
    private var checkFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        arguments?.let {
            movie = DetailFragmentArgs.fromBundle(it).movie
        }

        (activity as MainActivity).supportActionBar?.title = movie.title

        dataBinding.viewmodel = movie
        dataBinding.lifecycleOwner = this
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnclickFavorite()
        checkFavorite()

        viewModel.error.observe(activity!!, Observer {
            showToast(it)
        })

        viewModel.succes.observe(activity!!, Observer {
            showToast(it)
        })
    }

    private fun setOnclickFavorite() {

        dataBinding.imgBtnFav.setOnClickListener {
            if (checkFavorite) {
                viewModel.deleteMovie(movie)
                dataBinding.imgBtnFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.ic_favorite_border_black_24dp
                    )
                )
            } else {
                viewModel.insertMovie(movie)
                dataBinding.imgBtnFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.ic_favorite_red_24dp
                    )
                )
            }


        }

    }

    private fun checkFavorite() {
        viewModel.getCheckMovie(movie.id!!).observe(activity!!, Observer {
            Log.e("DetailFragment", it.toString())
            if (it) {
                checkFavorite = true
                dataBinding.imgBtnFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.ic_favorite_red_24dp
                    )
                )
            } else {
                checkFavorite = false
                dataBinding.imgBtnFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        activity!!,
                        R.drawable.ic_favorite_border_black_24dp
                    )
                )
            }
        })
    }

    private fun showToast(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT).show()
    }
}

