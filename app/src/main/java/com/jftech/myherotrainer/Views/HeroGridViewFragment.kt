package com.jftech.myherotrainer.Views

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jftech.myherotrainer.Data.Services.HeroTrainerViewModel
import com.jftech.myherotrainer.Data.Services.RetrofitBuilder
import com.jftech.myherotrainer.R
import com.jftech.myherotrainer.Utilities.Fragments
import com.jftech.myherotrainer.Utilities.HeroViewClickListener
import com.jftech.myherotrainer.Utilities.SharedSharedPreferences
import com.jftech.myherotrainer.Views.Adapters.HeroGridViewAdapter
import com.squareup.picasso.Picasso
import kotlin.math.round

class HeroGridViewFragment: Fragment()
{
    private lateinit var viewModel: HeroTrainerViewModel
    private lateinit var heroRecyclerView: RecyclerView
    private lateinit var heroDataRefresher: SwipeRefreshLayout
    private lateinit var logoutButton: AppCompatButton
    private lateinit var clickListener: HeroViewClickListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_grid_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        setupViewModel()
        view.post {
            wireViews()
            setupUI()
            if (viewModel.HeroData.value != null )
                setupDynamicUI()
            viewModel.HeroData.observe(viewLifecycleOwner) { setupDynamicUI() }
        }
    }

    private fun setupUI()
    {
        clickListener = HeroViewClickListener { hero -> viewModel.SwitchFragment(Fragments.DetailView)
            viewModel.SelectHero(hero) }
        heroRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        heroDataRefresher.setOnRefreshListener {
            heroDataRefresher.isRefreshing = false
            viewModel.OnUserAuthenticated()
        }
        logoutButton.setOnClickListener { logout() }
    }

    private fun setupDynamicUI()
    {
        val sortedHeroData = viewModel.HeroData.value!!.sortedByDescending { hero -> hero.CurrentPower }
        heroRecyclerView.adapter = HeroGridViewAdapter(sortedHeroData.toTypedArray(), clickListener)
    }

    private fun wireViews()
    {
        heroRecyclerView = view!!.findViewById(R.id.grid_view_recyclerview)
        heroDataRefresher = view!!.findViewById(R.id.grid_view_refresher)
        logoutButton = view!!.findViewById(R.id.grid_view_logout_button)
    }

    private fun setupViewModel()
    {
        viewModel = ViewModelProvider(requireActivity()).get(HeroTrainerViewModel::class.java)
    }

    private fun logout()
    {
        SharedSharedPreferences.SharedInstance.SharedPreferences.edit().remove("auth_token").apply()
        viewModel.SwitchFragment(Fragments.Login)
    }
}