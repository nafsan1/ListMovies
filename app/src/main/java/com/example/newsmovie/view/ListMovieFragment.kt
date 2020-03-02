package com.example.newsmovie.view

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsmovie.R
import com.example.newsmovie.adapter.MovieListAdapter
import com.example.newsmovie.databinding.FragmentListMovieBinding
import com.example.newsmovie.databinding.ItemCategoryBinding
import com.example.newsmovie.model.Movie
import com.example.newsmovie.viewmodel.ListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * A simple [Fragment] subclass.
 */
class ListMovieFragment : Fragment() {

    private lateinit var dataBinding: FragmentListMovieBinding
    private lateinit var viewModel: ListViewModel
    private val listAdaper: MovieListAdapter = MovieListAdapter(arrayListOf())
    private val popular = "popular"
    private val now_playing = "now_playing"
    private val top_rated = "top_rated"
    private val upcoming = "upcoming"
    private var category = now_playing
    private lateinit var dataBindingCategory:ItemCategoryBinding
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (view == null) {
            dataBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_list_movie, container, false)

        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        navController = Navigation.findNavController(view)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        showProgress()
        viewModel.getMovie(popular)
        setRecycle()
        setData()


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dataBinding.btnCategory.setOnClickListener{
            showCategory()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.actionFavorite){
          navController.navigate(R.id.action_listMovieFragment_to_favoriteFragment)
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setRecycle(){
        dataBinding.movieList.apply {
            layoutManager = GridLayoutManager(activity, 1)
            adapter = listAdaper
        }

        listAdaper.SetOnItemClick(object : MovieListAdapter.OnItemOnClick{
            override fun onItemClick(movie: Movie,view:View) {
                val action = ListMovieFragmentDirections.actionListMovieFragmentToDetailFragment(movie)
                Navigation.findNavController(view).navigate(action)
            }

        })

    }
    private fun setData() {
        viewModel.resultMovie.observe(activity!!, Observer {data ->
            hideProgress()

            data.let { it1 ->   listAdaper.updateMovieList(it1) }
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
        dataBinding.movieList.visibility = View.GONE
    }

    private fun hideProgress() {
        dataBinding.loadingView.bringToFront()
        dataBinding.loadingView.visibility = View.GONE
        dataBinding.movieList.visibility = View.VISIBLE
    }

    private fun showCategory(){
        val dialog = BottomSheetDialog(activity!!)
        dataBindingCategory = DataBindingUtil.inflate(
            LayoutInflater.from(activity!!),
            R.layout.item_category,
            null,
            false
        )
        dialog.setContentView(dataBindingCategory.root)

        dataBindingCategory.txtNowPlaying.setOnClickListener{
            setDataCategory(now_playing,dialog)
        }

        dataBindingCategory.txtPopular.setOnClickListener{
            setDataCategory(popular,dialog)
        }

        dataBindingCategory.txtTopRated.setOnClickListener{
            setDataCategory(top_rated,dialog)
        }

        dataBindingCategory.txtUpcoming.setOnClickListener{
            setDataCategory(upcoming,dialog)
        }

        dialog.show()
    }

    private fun setDataCategory(category:String,dialog:BottomSheetDialog){
        showProgress()
        dialog.dismiss()
        viewModel.getMovie(category)
    }
    private fun showError(error: String?) {
        Toast.makeText(activity!!, "Error $error", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT).show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

}
