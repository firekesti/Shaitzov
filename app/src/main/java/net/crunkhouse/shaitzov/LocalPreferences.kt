package net.crunkhouse.shaitzov

import android.content.Context
import android.content.SharedPreferences

/**
 * Private constructor that gets an instance of SharedPreferences
 * @param context A Context used to get SharedPreferences.
 */
class LocalPreferences(context: Context) {
    companion object {
        // Name of file where preferences are stored
        private val FILE_NAME = LocalPreferences::class.java.getPackage()!!.name

        // Preference names
        private val PLAYER_NICKNAME = "$FILE_NAME.a"
        private val GAME_ID = "$FILE_NAME.b"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var gameId: String
        get() = prefs.getString(GAME_ID, null) ?: ""
        set(gameId) = prefs.edit().putString(GAME_ID, gameId).apply()

    var playerNickname: String
        get() = prefs.getString(PLAYER_NICKNAME, null) ?: ""
        set(nickname) = prefs.edit().putString(PLAYER_NICKNAME, nickname).apply()
}
