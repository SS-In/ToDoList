package com.ssin.todolist.ui.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.ssin.todolist.R;
import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.ui.login.module.DaggerLoginComponent;
import com.ssin.todolist.ui.login.module.LoginModule;
import com.ssin.todolist.ui.login.presenter.LoginPresenter;
import com.ssin.todolist.ui.main.view.MainActivity;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindString(R.string.login_btn)
    String loginTitle;

    @BindString(R.string.login_in_progress)
    String loginProgress;

    @BindString(R.string.login_email_empty)
    String emailEmpty;

    @BindString(R.string.login_password_empty)
    String passwordEmpty;

    @BindView(R.id.text_input_layout_email)
    TextInputLayout tilEmail;

    @BindView(R.id.text_input_layout_password)
    TextInputLayout tilPassword;

    @BindView(R.id.button_login)
    Button btnLogin;

    @Inject
    LoginPresenter presenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(loginProgress);
        progressDialog.setTitle(loginTitle);

        DaggerLoginComponent.builder()
                .firebaseModule(new FirebaseModule())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(loginTitle);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void setLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_login)
    public void login() {
        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();

        presenter.validateCredentials(email, password);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void resetErrors() {
        tilEmail.setError(null);
        tilPassword.setError(null);
    }

    @Override
    public void setEmailError() {
        tilEmail.setError(emailEmpty);
    }

    @Override
    public void setPasswordError() {
        tilPassword.setError(passwordEmpty);
    }
}
