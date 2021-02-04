package com.jftech.myherotrainer.Data.Services

import com.jftech.myherotrainer.Data.Models.Hero
import com.jftech.myherotrainer.Data.Models.LoginForm
import com.jftech.myherotrainer.Data.Models.RegisterForm
import com.jftech.myherotrainer.Utilities.Result
import com.jftech.myherotrainer.Utilities.SharedSharedPreferences
import com.jftech.myherotrainer.Utilities.Status
import io.realm.Realm
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HeroTrainerRepository(private val apiHelper: ApiHelper)
{
    suspend fun RegisterTrainer(body: RegisterForm) = apiHelper.RegisterTrainer(body)
    suspend fun LoginTrainer(body: LoginForm) = apiHelper.LoginTrainer(body)
    suspend fun Sync(token: String, date: String): Result<Array<Hero>>
    {
        val result = apiHelper.SyncRequired(token, date)
        val syncRequired = result.Message.toBoolean()
        if (syncRequired)
        {
            return try
            {
                val heroesJson = apiHelper.Sync(token)
                saveToLocalData(heroesJson.toMutableList())
                Result(Status.Success, heroesJson, null)
            }
            catch (e: Exception)
            {
                return Result(Status.Error, null, e.message)
            }
        }
        else
            return getLocalHeroData()
    }

    suspend fun Sync(token: String): Result<Array<Hero>>
    {
        return try
        {
            val heroesJson = apiHelper.Sync(token)
            saveToLocalData(heroesJson.toMutableList())
            Result(Status.Success, heroesJson, null)
        }
        catch (e: Exception)
        {
            return Result(Status.Error, null, e.message)
        }
    }

    suspend fun TrainHero(token: String, id: String) = apiHelper.TrainHero(token, id)
    suspend fun LoadCachedData(): Result<Array<Hero>>
    {
        return getLocalHeroData()
    }

    private suspend fun saveToLocalData(heroes: MutableList<Hero>)
    {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.deleteAll()
        realm.insert(heroes)
        realm.commitTransaction()
        SharedSharedPreferences.SharedInstance.SharedPreferences.edit().putString("data_last_updated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).apply()
    }

    private suspend fun getLocalHeroData(): Result<Array<Hero>>
    {
        val heroes: Array<Hero>
        try
        {
            val realm = Realm.getDefaultInstance()
            heroes = realm.where(Hero::class.java).findAll().toTypedArray()
        }
        catch (e: Exception)
        {
            return Result(Status.Error, null, e.message)
        }
        return Result(Status.Success, heroes, null)
    }
}