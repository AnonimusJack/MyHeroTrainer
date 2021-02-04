package com.jftech.myherotrainer.Data.Models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import org.json.JSONObject

open class Hero(@SerializedName("id") var Id: String, @SerializedName("name") var Name: String, @SerializedName("type") private var type: String, @SerializedName("currentPower") var CurrentPower: Float, @SerializedName("trainingStamina") var TrainingStamina: Int): RealmObject()
{
    constructor(): this("", "", "Support", 0f, 5)
    public val Type: AbilityType
        get() { return AbilityType.valueOfOldInterface(type.toInt()) }
    enum class AbilityType(val raw: String)
    {
        Attacker("Attacker"),
        Defender("Defender"),
        Support("Support");

        companion object
        {
            fun valueOfOldInterface(rawValue: Int): AbilityType
            {
                return when(rawValue)
                {
                    0 -> Attacker
                    1 -> Defender
                    2 -> Support
                    else -> Support
                }
            }
        }
    }
}