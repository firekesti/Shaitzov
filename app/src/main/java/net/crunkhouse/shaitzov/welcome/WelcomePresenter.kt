package net.crunkhouse.shaitzov.welcome

import android.content.Intent
import net.crunkhouse.shaitzov.LocalPreferences
import net.crunkhouse.shaitzov.MainActivity
import net.crunkhouse.shaitzov.R

class WelcomePresenter(private val activity: WelcomeActivity,
                       private val prefs: LocalPreferences) {

    fun onCreate() {
        // If the user has a previous nickname, prepopulate it:
        if (prefs.playerNickname.isNotEmpty()) {
            activity.setNicknameText(prefs.playerNickname)
        }
        // If the user has a previous game id, prepopulate it:
        if (prefs.gameId.isNotEmpty()) {
            activity.setGameIdText(prefs.gameId)
        }
    }

    fun onClickContinue(nickText: String) {
        val username = nickText.trim { it <= ' ' }
        if (username.isNotEmpty()) {
            // The user has entered a name
            prefs.playerNickname = username
            activity.setWelcomeText(activity.getString(R.string.thanks_what_game, username))
            activity.goToGameInput()
        } else {
            // Show an error, the user needs to enter a name!
            activity.setNicknameError(activity.getString(R.string.err_enter_nickname))
        }
    }

    fun onClickLetsGo(gameInput: String) {
        val gameId = gameInput.trim { it <= ' ' }
        if (gameId.isNotEmpty()) {
            // The user has entered a game Id
            prefs.gameId = gameId
            activity.goToGame()
        } else {
            // Show an error, the user needs to enter a game ID!
            activity.setGameIdError(activity.getString(R.string.err_enter_game_id))
        }
    }

    fun handleOnBackPressed(): Boolean {
        return if (activity.isOnGameIdInput()) {
            // Go back to name input
            activity.goToNameInput()
            true
        } else {
            false
        }
    }
}
