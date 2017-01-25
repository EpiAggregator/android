package com.epiagregator;

import com.epiagregator.impls.webapi.error.RetrofitException;
import com.epiagregator.impls.webapi.model.SignInRequest;
import com.epiagregator.impls.webapi.providers.WebApiAccountProvider;
import com.epiagregator.model.providers.Providers;
import com.epiagregator.model.userprofile.UserProfile;
import com.epiagregator.model.userprofile.UserProfileService;
import com.epiagregator.screens.signin.SignInMvpView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class SignInPresenterTest {

    @Mock SignInMvpView mMockSignInMvpView;
    @Mock WebApiAccountProvider mWebApiAccountProvider;

    private SignInRequest mSignInRequest = new SignInRequest("etienne.debas@gmail.com", "1234");

    @Before
    public void setUp() {
        Providers.setAccountProvider(mWebApiAccountProvider);
    }

    @Test
    public void login_Success() {
        doReturn(Observable.empty())
                .when(mWebApiAccountProvider)
                .signIn(any(SignInRequest.class));

        UserProfileService.loginUser(mSignInRequest, mMockSignInMvpView);
        verify(mMockSignInMvpView).showLoading();
        verify(mMockSignInMvpView).onResponse(any(UserProfile.class));
        verify(mMockSignInMvpView, never()).onError(any(RetrofitException.class));
    }
}