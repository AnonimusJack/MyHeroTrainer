package com.jftech.myherotrainer.Views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jftech.myherotrainer.Data.Services.HeroTrainerViewModel
import com.jftech.myherotrainer.Data.Services.RetrofitBuilder
import com.jftech.myherotrainer.R
import com.jftech.myherotrainer.Utilities.Fragments
import com.jftech.myherotrainer.Utilities.MHTExceptions
import com.jftech.myherotrainer.Utilities.UserDataValidation
import com.squareup.picasso.Picasso
import kotlin.math.round

class HeroViewFragment: Fragment()
{
    private lateinit var viewModel: HeroTrainerViewModel
    private lateinit var heroImageImageView: AppCompatImageView
    private lateinit var heroNameTextView: AppCompatTextView
    private lateinit var heroAbilityTypeTextView: AppCompatTextView
    private lateinit var heroPowerLevelProgressBar: ProgressBar
    private lateinit var heroPowerLevelTextView: AppCompatTextView
    private lateinit var trainHeroButton: AppCompatButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_hero_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        setupViewModel()
        view.post {
            if (viewModel.SelectedHero.value != null )
                setupUI()
            setupObservers()
        }
    }

    private fun setupUI()
    {
        wireViews()
        val selectedHero = viewModel.SelectedHero.value!!
        Picasso.get()
                .load(RetrofitBuilder.ImageUrlForHero(selectedHero.Id))
                .placeholder(R.drawable.image_hero_placeholder)
                .error(R.drawable.image_hero_error)
                .into(heroImageImageView)
        heroImageImageView.setOnClickListener { viewModel.SwitchFragment(Fragments.GridView) }
        heroNameTextView.text = selectedHero.Name
        heroAbilityTypeTextView.text = selectedHero.Type.toString()
        val powerLevel: Int = round(selectedHero.CurrentPower).toInt()
        heroPowerLevelProgressBar.progress = powerLevel
        heroPowerLevelTextView.text = "$powerLevel"
        trainHeroButton.setOnClickListener { viewModel.TrainHero(selectedHero.Id) }
        if (selectedHero.TrainingStamina <= 0)
            trainHeroButton.isEnabled = false
    }

    private fun setupObservers()
    {
        viewModel.SelectedHero.observe(viewLifecycleOwner) { setupUI() }
        viewModel.HeroTrainedFlag.observe(viewLifecycleOwner) { trainHeroButton.isEnabled = !it && viewModel.SelectedHero.value!!.TrainingStamina > 0 }
    }

    private fun wireViews()
    {
        heroImageImageView = view!!.findViewById(R.id.hero_view_hero_imageview)
        heroNameTextView = view!!.findViewById(R.id.hero_view_name_textview)
        heroAbilityTypeTextView = view!!.findViewById(R.id.hero_view_ability_type_textview)
        heroPowerLevelProgressBar = view!!.findViewById(R.id.hero_view_power_level_progressbar)
        heroPowerLevelTextView = view!!.findViewById(R.id.hero_view_power_level_textview)
        trainHeroButton = view!!.findViewById(R.id.hero_view_train_button)
    }

    private fun setupViewModel()
    {
        viewModel = ViewModelProvider(requireActivity()).get(HeroTrainerViewModel::class.java)
    }
}