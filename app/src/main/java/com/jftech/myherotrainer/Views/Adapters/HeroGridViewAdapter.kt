package com.jftech.myherotrainer.Views.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.jftech.myherotrainer.Data.Models.Hero
import com.jftech.myherotrainer.Data.Services.RetrofitBuilder
import com.jftech.myherotrainer.R
import com.jftech.myherotrainer.Utilities.HeroViewClickListener
import com.squareup.picasso.Picasso
import kotlin.math.round

class HeroGridViewAdapter(private val heroData: Array<Hero>, private val clickListener: HeroViewClickListener): RecyclerView.Adapter<HeroGridViewAdapter.HeroGridViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroGridViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_hero, parent, false)
        return HeroGridViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroGridViewHolder, position: Int)
    {
        val hero = heroData[position]
        Picasso.get()
                .load(RetrofitBuilder.ImageUrlForHero(hero.Id))
                .placeholder(R.drawable.image_hero_placeholder)
                .error(R.drawable.image_hero_error)
                .resize(150, 250)
                .into(holder.HeroImageView)
        holder.HeroNameTextView.text = hero.Name
        holder.HeroPowerLevelProgressBar.progress = round(hero.CurrentPower).toInt()
        holder.HeroImageView.setOnClickListener { clickListener.OnClick(hero) }
    }

    override fun getItemCount(): Int
    {
        return heroData.size
    }

    class HeroGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val HeroImageView: AppCompatImageView = itemView.findViewById(R.id.hero_layout_hero_imageview)
        val HeroNameTextView: AppCompatTextView = itemView.findViewById(R.id.hero_layout_name_textview)
        val HeroPowerLevelProgressBar: ProgressBar = itemView.findViewById(R.id.hero_layout_power_level_progressbar)
    }
}