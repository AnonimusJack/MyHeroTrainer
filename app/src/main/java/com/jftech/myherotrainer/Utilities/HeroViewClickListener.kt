package com.jftech.myherotrainer.Utilities

import com.jftech.myherotrainer.Data.Models.Hero

class HeroViewClickListener(val clickListener: (hero: Hero) -> Unit)
{
    fun OnClick(hero: Hero) = clickListener(hero)
}