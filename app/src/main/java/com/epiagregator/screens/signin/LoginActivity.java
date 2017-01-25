package com.epiagregator.screens.signin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.epiagregator.R;
import com.epiagregator.impls.webapi.error.RetrofitException;
import com.epiagregator.model.userprofile.UserProfile;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements SignInMvpView {

    private static final String EMAIL_REGEXP = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    // SING IN UI
    @BindView(R.id.login_activity_sign_in_form)
    View mSignInForm;

    @BindView(R.id.login_activity_sign_in_email_text_input_layout)
    TextInputLayout mSignInEmailInputLayout;

    @BindView(R.id.login_activity_sign_in_password_text_input_layout)
    TextInputLayout mSignInPasswordInputLayout;

    // SING UP UI
    @BindView(R.id.login_activity_sign_up_form)
    View mSignUpForm;

    @BindView(R.id.login_activity_sign_up_email_text_input_layout)
    TextInputLayout mSignUpEmailInputLayout;

    @BindView(R.id.login_activity_sign_up_password_text_input_layout)
    TextInputLayout mSignUpPasswordInputLayout;

    @BindView(R.id.login_activity_sign_up_password_confirm_text_input_layout)
    TextInputLayout mSignUpPasswordConfirmInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_activity_sign_in_button)
    public void signIn(View v) {
        resetError(mSignInEmailInputLayout, mSignUpEmailInputLayout);
        if (checkEmail(mSignInEmailInputLayout) & checkPassword(mSignInPasswordInputLayout)) {
            // TODO Sign in request
            Toast.makeText(this, "Can sign in", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.login_activity_sign_up_button)
    public void signUp(View v) {
        resetError(mSignUpEmailInputLayout, mSignUpEmailInputLayout);
        if (checkEmail(mSignUpEmailInputLayout)
                & checkPasswords(mSignUpPasswordInputLayout, mSignUpPasswordConfirmInputLayout)) {
            // TODO Sign up request
            Toast.makeText(this, "Can sign up", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.login_activity_sign_up_already_have_account)
    public void alreadyHaveAccount(View v) {
        mSignUpForm.setVisibility(View.GONE);
        mSignInForm.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.login_activity_sign_in_create_account)
    public void createAccount(View v) {
        mSignUpForm.setVisibility(View.VISIBLE);
        mSignInForm.setVisibility(View.GONE);
    }

    public boolean checkEmail(@NonNull TextInputLayout email) {
        EditText editText = email.getEditText();
        boolean valid = editText != null && editText.length() > 0 && Pattern.matches(EMAIL_REGEXP, editText.getText());
        setErrorTextInput(valid, email, R.string.error_invalid_email);
        return valid;
    }

    public boolean checkPassword(TextInputLayout password) {
        EditText editText = password.getEditText();
        boolean valid = editText != null && editText.length() > 0;
        setErrorTextInput(valid, password, R.string.error_invalid_password);
        return valid;
    }

    public boolean checkPasswords(TextInputLayout password, TextInputLayout passwordConfirm) {
        return checkPassword(password) && checkPasswordMatches(password, passwordConfirm);
    }

    private boolean checkPasswordMatches(TextInputLayout password, TextInputLayout passwordConfirm) {
        EditText editTextPassword = password.getEditText();
        EditText editTextPasswordConfirm = passwordConfirm.getEditText();
        boolean valid = editTextPassword != null && editTextPasswordConfirm != null && editTextPassword.getText().toString().equals(editTextPasswordConfirm.getText().toString());
        setErrorTextInput(valid, passwordConfirm, R.string.error_invalid_password_matches);
        return valid;
    }

    private void resetError(TextInputLayout ... textInputLayouts) {
        for (TextInputLayout textInputLayout: textInputLayouts) {
            textInputLayout.setErrorEnabled(false);
        }
    }

    private void setErrorTextInput(boolean valid, TextInputLayout textInputLayout, @StringRes int errorMessage) {
        if (valid) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getString(errorMessage));
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onResponse(UserProfile result) {

    }

    @Override
    public void onError(RetrofitException exception) {

    }
}

