package com.ssin.todolist.ui.splash.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.ssin.todolist.R;
import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.ui.login.view.LoginActivity;
import com.ssin.todolist.ui.main.view.MainActivity;
import com.ssin.todolist.ui.register.view.RegisterActivity;
import com.ssin.todolist.ui.splash.module.DaggerSplashComponent;
import com.ssin.todolist.ui.splash.module.SplashModule;
import com.ssin.todolist.ui.splash.presenter.SplashPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity implements SplashView {
    @Inject
    SplashPresenter presenter;

    @BindView(R.id.button_login)
    Button btnLogin;

    @BindView(R.id.button_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        DaggerSplashComponent.builder()
                .firebaseModule(new FirebaseModule())
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onUserCheckingAuth();
    }

    @Override
    public void navigateIntoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.button_login)
    public void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setAction(MainActivity.ACTION_LOGIN);
        startActivity(intent);

    }

    @OnClick(R.id.button_register)
    public void navigateToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
