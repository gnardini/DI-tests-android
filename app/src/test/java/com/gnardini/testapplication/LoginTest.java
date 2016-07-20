package com.gnardini.testapplication;

import com.gnardini.testapplication.login.LoginPresenter;
import com.gnardini.testapplication.login.LoginView;
import com.gnardini.testapplication.model.User;
import com.gnardini.testapplication.network.UserRequest;
import com.gnardini.testapplication.repository.LocalStorage;
import com.gnardini.testapplication.repository.RepoCallback;
import com.gnardini.testapplication.repository.UsersRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

    @Mock LoginView loginView;
    @Mock LocalStorage localStorage;
    @Mock UsersRepository usersRepository;

    private LoginPresenter loginPresenter;
    private User exampleUser;

//    Not anymore!
//    @BeforeClass
//    public static void initServices() {
//        RetrofitServices.init();
//    }

    @Before
    public void initPresenter() {
        loginPresenter = new LoginPresenter(loginView, localStorage, usersRepository);
    }

    @Before
    public void createExampleUser() {
        exampleUser = new User(1, "username");
    }

    /**
     * Checks that the login button isn't enabled if invalid input is provided.
     */
    @Test
    public void checkInvalidInput() {
        loginPresenter.updateEmail("valid@email.com");
        loginPresenter.updatePassword("");

        loginPresenter.updateEmail("");
        loginPresenter.updatePassword("password");

        // Check that the login button is never enabled.
        verify(loginView, never()).setButtonEnabled(true);

        // To be defensive, check that the login doesn't go through if |loginPressed| is called but
        // credentials aren't valid.
        loginPresenter.loginPressed();
        verify(usersRepository, never()).login(any(), any());
    }

    /**
     * When a successful login is made, check that the user is stored in local storage and that the
     * view is told to navigate to the main screen.
     */
    @Test
    public void checkUserLoggedSuccessfully() {
        inputValidCredentials();
        doAnswer(invocationOnMock -> {
            invocationOnMock.getArgumentAt(1, RepoCallback.class).onSuccess(exampleUser);
            return null;
        }).when(usersRepository).login(any(UserRequest.class), any(RepoCallback.class));

        loginPresenter.loginPressed();

        verify(localStorage).storeInSharedPreferences(Configuration.KEY_EMAIL, exampleUser);
        verify(loginView).goToMainScreen();
    }

    /**
     * Check that an error message is called when the login API call fails.
     */
    @Test
    public void checkUserLoginFailed() {
        inputValidCredentials();
        doAnswer(invocationOnMock -> {
            invocationOnMock.getArgumentAt(1, RepoCallback.class).onError("error", 0);
            return null;
        }).when(usersRepository).login(any(UserRequest.class), any(RepoCallback.class));

        loginPresenter.loginPressed();

        verify(loginView).showError(any(String.class));
    }

    private void inputValidCredentials() {
        loginPresenter.updateEmail("valid@email.com");
        loginPresenter.updatePassword("password");
    }

}