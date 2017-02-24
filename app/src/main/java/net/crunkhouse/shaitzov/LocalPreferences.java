package net.crunkhouse.shaitzov;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public final class LocalPreferences {

    // Name of file where preferences are stored
    private static final String FILE_NAME = LocalPreferences.class.getPackage().getName();

    // Preference names
    private static final String PLAYER_NICKNAME = FILE_NAME + ".a";
    private static final String GAME_ID = FILE_NAME + ".b";

    // Singleton instance of this class
    private static LocalPreferences instance;

    // Shared preferences instance
    private SharedPreferences prefs;

    /**
     * Private constructor that gets an instance of SharedPreferences
     * @param context A Context used to get SharedPreferences.
     */
    private LocalPreferences(Context context) {
        prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Initializes the singleton for LocalPreferences
     * @param context A Context that can be used to initialize this class.
     */
    public static void init(Context context) {
        instance = new LocalPreferences(context);
    }

    /**
     * Returns an instance of LocalPreferences
     * @return The singleton instance of LocalPreferences
     */
    public static LocalPreferences getInstance() {
        return instance;
    }

    @Nullable
    public String getGameId() {
        return prefs.getString(GAME_ID, null);
    }

    public void setGameId(String gameId) {
        prefs.edit().putString(GAME_ID, gameId).apply();
    }

    public String getPlayerNickname() {
        return prefs.getString(PLAYER_NICKNAME, null);
    }

    public void setPlayerNickname(String nickname) {
        prefs.edit().putString(PLAYER_NICKNAME, nickname).apply();
    }
}