package net.crunkhouse.shaitzov.welcome

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.activity_welcome.*
import net.crunkhouse.shaitzov.MainActivity
import net.crunkhouse.shaitzov.R
import net.crunkhouse.shaitzov.ShaitzovApplication

/**
 * An entry Activity to put in a player name and a game ID
 */

class WelcomeActivity : AppCompatActivity() {

    companion object {
        internal const val FADE_DURATION = 200
    }

    private val presenter = WelcomePresenter(this, (application as ShaitzovApplication).prefs)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        nickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nickname_layout.error = null
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        game_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                game_id_layout.error = null
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        continue_button.setOnClickListener { presenter.onClickContinue(nickname.text.toString()) }
        lets_go_button.setOnClickListener { presenter.onClickLetsGo(game_id.text.toString()) }
        presenter.onCreate()
    }

    override fun onBackPressed() {
        if (!presenter.handleOnBackPressed()) {
            super.onBackPressed()
        }
    }

    fun setNicknameText(name: String) {
        nickname.setText(name)
    }

    fun setGameIdText(game: String) {
        game_id.setText(game)
    }

    fun setNicknameError(error: String) {
        nickname_layout.error = error
    }

    fun setGameIdError(error: String) {
        game_id_layout.error = error
    }

    fun setWelcomeText(text: String) {
        thanks_what_game.text = text
    }

    fun goToGameInput() {
        // Animate Part 1 out and Part 2 in
        val animator = ObjectAnimator.ofFloat(part_one, View.ALPHA, 1f, 0f)
        animator.duration = WelcomeActivity.FADE_DURATION.toLong()
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                part_one.visibility = View.GONE
                part_two.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(part_two, View.ALPHA, 0f, 1f)
                        .setDuration(WelcomeActivity.FADE_DURATION.toLong())
                        .start()
            }
        })
        animator.start()
    }

    fun isOnGameIdInput(): Boolean {
        return part_two.visibility == View.VISIBLE
    }

    fun goToNameInput() {
        val animator = ObjectAnimator.ofFloat(part_two, View.ALPHA, 1f, 0f)
        animator.duration = WelcomeActivity.FADE_DURATION.toLong()
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                part_two.visibility = View.GONE
                part_one.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(part_one, View.ALPHA, 0f, 1f)
                        .setDuration(WelcomeActivity.FADE_DURATION.toLong())
                        .start()
            }
        })
        animator.start()
    }

    fun goToGame() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
