package com.jftech.myherotrainer.Data.Services

import android.util.Log
import androidx.lifecycle.*
import com.jftech.myherotrainer.Data.Models.Hero
import com.jftech.myherotrainer.Data.Models.LoginForm
import com.jftech.myherotrainer.Data.Models.RegisterForm
import com.jftech.myherotrainer.Utilities.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException

class HeroTrainerViewModel(private val heroTrainerRepository: HeroTrainerRepository) : ViewModel()
{
    private val raisedException: MutableLiveData<MHTExceptions> = MutableLiveData()
    val RaisedException: LiveData<MHTExceptions>
        get() { return raisedException }
    private val currentFragment: MutableLiveData<Fragments> = MutableLiveData()
    val CurrentFragment: LiveData<Fragments>
        get() { return  currentFragment }
    private val heroTrainedFlag: MutableLiveData<Boolean> = MutableLiveData()
    val HeroTrainedFlag: LiveData<Boolean>
        get() { return  heroTrainedFlag }
    private val heroData: MutableLiveData<Array<Hero>> = MutableLiveData()
    val HeroData: LiveData<Array<Hero>>
        get() { return heroData }
    private val selectedHero: MutableLiveData<Hero> = MutableLiveData()
    val SelectedHero: LiveData<Hero>
        get() { return selectedHero }
    private val connectedToNetwork: MutableLiveData<Boolean> = MutableLiveData()
    val ConnectedToNetwork: LiveData<Boolean>
        get() { return  connectedToNetwork }
    var PasswordCache: String = ""


    fun OnUserAuthenticated()
    {
        viewModelScope.launch {
            if (isConnectedToNetwork())
            {
                val token = SharedSharedPreferences.SharedInstance.SharedPreferences.getString("auth_token", "")!!
                val lastUpdated = SharedSharedPreferences.SharedInstance.SharedPreferences.getString("data_last_updated", "1-1-2000 00:00:00")!!
                try
                {
                    val result = heroTrainerRepository.Sync(token, lastUpdated)
                    handleHeroDataResult(result, false)
                }
                catch (e: Exception)
                {
                    val result = heroTrainerRepository.LoadCachedData()
                    handleHeroDataResult(result, true)
                    handleAsyncExceptions(e)
                }
            }
            else
            {
                val result = heroTrainerRepository.LoadCachedData()
                handleHeroDataResult(result, true)
                raisedException.postValue(MHTExceptions.NoInternetConnectionException)
            }
        }
    }

    fun Login(username: String, password: String)
    {
        viewModelScope.launch {
            if (isConnectedToNetwork())
                try
                {
                    val loginBody = LoginForm(username, password)
                    val token = heroTrainerRepository.LoginTrainer(loginBody)
                    if (token.Token.isNotBlank())
                    {
                        SharedSharedPreferences.SharedInstance.SharedPreferences.edit().putString("auth_token", "Bearer ${token.Token}").apply()
                        currentFragment.postValue(Fragments.GridView)
                        OnUserAuthenticated()
                    }
                    else
                        raisedException.postValue(MHTExceptions.EmptyTokenException)
                }
                catch (e: Exception)
                {
                    handleAsyncExceptions(e)
                }
            else
                raisedException.postValue(MHTExceptions.NoInternetConnectionException)
        }
    }

    fun Register(username: String, email: String, password: String)
    {
        viewModelScope.launch {
            if (isConnectedToNetwork())
                try
                {
                    val registerBody = RegisterForm(username, email, password)
                    val response = heroTrainerRepository.RegisterTrainer(registerBody)
                    if (response.Status == "Success")
                    {
                        SwitchFragment(Fragments.Login)
                        Login(username, password)
                    }
                    else
                        raisedException.postValue(MHTExceptions.InternalServerException)
                }
                catch (e: HttpException)
                {
                    handleAsyncExceptions(e)
                }
            else
                raisedException.postValue(MHTExceptions.NoInternetConnectionException)
        }
    }

    fun TrainHero(id: String)
    {
        heroTrainedFlag.postValue(false)
        viewModelScope.launch {
            if (isConnectedToNetwork())
                try
                {
                    val token = SharedSharedPreferences.SharedInstance.SharedPreferences.getString("auth_token", "")!!
                    heroTrainerRepository.TrainHero(token, id)
                    val result = heroTrainerRepository.Sync(token)
                    if (handleHeroDataResult(result, false))
                    {
                        selectedHero.postValue(result.data!!.toList().find { hero -> hero.Id == id })
                        heroTrainedFlag.postValue(true)
                    }
                }
                catch (e: Exception)
                {
                    handleAsyncExceptions(e)
                }
            else
                raisedException.postValue(MHTExceptions.NoInternetConnectionException)
        }
    }

    fun ResetHeroTrainFlag()
    {
        heroTrainedFlag.postValue(false)
    }

    fun SwitchFragment(fragment: Fragments)
    {
        currentFragment.postValue(fragment)
    }

    fun RaiseException(exception: MHTExceptions)
    {
        raisedException.postValue(exception)
    }

    fun SelectHero(hero: Hero)
    {
        selectedHero.postValue(hero)
    }

    private suspend fun handleAsyncExceptions(e: Exception)
    {
        if (e is HttpException)
            when(e.code())
            {
                400 -> raisedException.postValue(MHTExceptions.InvalidInputException)
                401, 403 -> raisedException.postValue(MHTExceptions.InvalidCredentialsException)
                500 -> raisedException.postValue(MHTExceptions.UserAlreadyExistsException)
            }
        else if (e is SocketTimeoutException)
        {
            raisedException.postValue(MHTExceptions.ServerConnectionTimedOutException)
        }
    }

    private suspend fun isConnectedToNetwork(): Boolean
    {
        val activeNetworkInfo = SharedConnectivityManager.SharedInstance.ConnectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private suspend fun handleHeroDataResult(result: Result<Array<Hero>>, local: Boolean): Boolean
    {
        if (result.status == Status.Success)
            heroData.postValue(result.data)
        else if (result.status == Status.Error)
        {
            Log.d("Hero Data Fetch Error:", result.message!!)
            raisedException.postValue(if(local) MHTExceptions.RealmInitializationException else MHTExceptions.InternalServerException)
            return false
        }
        return true
    }

    class HeroTrainerViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory
    {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T
        {
            if (modelClass.isAssignableFrom(HeroTrainerViewModel::class.java))
                return HeroTrainerViewModel(HeroTrainerRepository(apiHelper)) as T
            throw  IllegalArgumentException("Unknown class name")
        }
    }
}