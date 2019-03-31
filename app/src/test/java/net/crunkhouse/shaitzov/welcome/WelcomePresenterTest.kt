package net.crunkhouse.shaitzov.welcome

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import net.crunkhouse.shaitzov.LocalPreferences
import net.crunkhouse.shaitzov.R
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class WelcomePresenterTest {

    @Test
    fun `onCreate the presenter populates the edit texts with saved data`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every { prefs.playerNickname } returns "test"
        every { prefs.gameId } returns "1234"

        val presenter = WelcomePresenter(activity, prefs)
        presenter.onCreate()

        verify { activity.setNicknameText("test") }
        verify { activity.setGameIdText("1234") }
    }

    @Test
    fun `onCreate the presenter doesn't do anything if no saved data`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every { prefs.playerNickname } returns ""
        every { prefs.gameId } returns ""

        val presenter = WelcomePresenter(activity, prefs)
        presenter.onCreate()

        verify(exactly = 0) { activity.setNicknameText(any()) }
        verify(exactly = 0) { activity.setGameIdText(any()) }
    }

    @Test
    fun `on click continue with a username, save username and show game id input`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every { prefs.playerNickname = "my name" } just runs
        every { activity.getString(R.string.thanks_what_game, "my name") } returns "hi my name"

        val presenter = WelcomePresenter(activity, prefs)
        presenter.onClickContinue("my name")

        verify { prefs.playerNickname = "my name" }
        verify { activity.setWelcomeText("hi my name") }
        verify { activity.goToGameInput() }
    }

    @Test
    fun `on click continue without a username, show error`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every { activity.getString(R.string.err_enter_nickname) } returns "name error"

        val presenter = WelcomePresenter(activity, prefs)
        presenter.onClickContinue("")

        verify { activity.setNicknameError("name error") }
    }

    @Test
    fun `on click continue with a game id, save game id and go to game`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every { prefs.gameId = "123" } just runs

        val presenter = WelcomePresenter(activity, prefs)
        presenter.onClickLetsGo("123")

        verify { prefs.gameId = "123" }
        verify { activity.goToGame() }
    }

    @Test
    fun `on click letsgo without a game id, show error`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every { activity.getString(R.string.err_enter_game_id) } returns "id error"

        val presenter = WelcomePresenter(activity, prefs)
        presenter.onClickLetsGo("")

        verify { activity.setGameIdError("id error") }
    }

    @Test
    fun `when back pressed on game input, go to name input`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every {activity.isOnGameIdInput()} returns true

        val presenter = WelcomePresenter(activity, prefs)

        assertEquals(true, presenter.handleOnBackPressed())
        verify { activity.goToNameInput() }
    }

    @Test
    fun `when back pressed on name input, let super handle`() {
        val activity: WelcomeActivity = mockk(relaxed = true)
        val prefs: LocalPreferences = mockk()

        every {activity.isOnGameIdInput()} returns false

        val presenter = WelcomePresenter(activity, prefs)

        assertEquals(false, presenter.handleOnBackPressed())
    }
}
