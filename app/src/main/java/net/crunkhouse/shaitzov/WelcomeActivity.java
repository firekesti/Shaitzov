package net.crunkhouse.shaitzov;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * An entry Activity to put in a player name and a game ID
 */

public class WelcomeActivity extends AppCompatActivity {
    private static final int FADE_DURATION = 250;

    private EditText nicknameView;
    private TextInputLayout nicknameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        nicknameView = (EditText) findViewById(R.id.nickname);
        // If the user has a previous nickname, prepopulate it:
        nicknameView.setText(LocalPreferences.getInstance().getPlayerNickname());
        nicknameLayout = (TextInputLayout) findViewById(R.id.nickname_layout);
        nicknameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nicknameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onClickContinue(View view) {
        String username = nicknameView.getText().toString().trim();
        if (username.length() > 0) {
            // The user has entered a name
            LocalPreferences.getInstance().setPlayerNickname(username);
            String text = getResources().getString(R.string.thanks_what_game, username);
            ((TextView) findViewById(R.id.thanks_what_game)).setText(text);

            ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.part_one), View.ALPHA, 1f, 0f);
            animator.setDuration(FADE_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    findViewById(R.id.part_one).setVisibility(View.GONE);
                    findViewById(R.id.part_two).setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(findViewById(R.id.part_two), View.ALPHA, 0f, 1f)
                            .setDuration(FADE_DURATION)
                            .start();
                }
            });
            animator.start();
        } else {
            // Show an error, the user needs to enter a name!
            nicknameLayout.setError(getString(R.string.err_enter_nickname));
        }
    }

    public void onClickLetsGo(View view) {
        // TODO save the Game ID once it matters
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.part_two).getVisibility() == View.VISIBLE) {
            // Go back to part one
            ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.part_two), View.ALPHA, 1f, 0f);
            animator.setDuration(FADE_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    findViewById(R.id.part_two).setVisibility(View.GONE);
                    findViewById(R.id.part_one).setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(findViewById(R.id.part_one), View.ALPHA, 0f, 1f)
                            .setDuration(FADE_DURATION)
                            .start();
                }
            });
            animator.start();
        } else {
            super.onBackPressed();
        }
    }
}
