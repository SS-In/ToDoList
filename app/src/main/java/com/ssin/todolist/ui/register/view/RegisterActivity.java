package com.ssin.todolist.ui.register.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ssin.todolist.R;
import com.ssin.todolist.module.FirebaseModule;
import com.ssin.todolist.ui.main.view.MainActivity;
import com.ssin.todolist.ui.register.module.DaggerRegisterComponent;
import com.ssin.todolist.ui.register.module.RegisterModule;
import com.ssin.todolist.ui.register.presenter.RegisterPresenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    @BindString(R.string.register_title)
    String registerTitle;

    @BindString(R.string.register_in_progress)
    String registerProgress;

    @BindString(R.string.register_message_confirm_password_error)
    String confirmPasswordError;

    @BindString(R.string.register_message_displayname_error)
    String displayNameError;

    @BindString(R.string.register_message_email_error)
    String emailError;

    @BindString(R.string.register_message_password_error)
    String passwordError;

    @BindString(R.string.register_account_created)
    String success;

    @BindString(R.string.register_password_not_match)
    String passwordNotMatchError;

    @BindString(R.string.register_invalid_email_format)
    String invalidEmailFormatError;

    @BindString(R.string.register_server_error)
    String serverError;

    @BindView(R.id.button_register)
    Button btnRegister;

    @BindView(R.id.text_input_layout_display_name)
    TextInputLayout tilDisplayName;

    @BindView(R.id.text_input_layout_password)
    TextInputLayout tilPassword;

    @BindView(R.id.text_input_layout_email)
    TextInputLayout tilEmail;

    @BindView(R.id.text_input_layout_confirm_password)
    TextInputLayout tilConfirmPassword;

    @BindView(R.id.text_view_error)
    TextView tvError;

    @Inject
    RegisterPresenter presenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(registerTitle);
        progressDialog.setMessage(registerProgress);
        progressDialog.setCancelable(false);

        DaggerRegisterComponent.builder()
                .firebaseModule(new FirebaseModule())
                .registerModule(new RegisterModule(this))
                .build()
                .inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(registerTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.button_register)
    public void register() {
        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();
        String displayName = tilDisplayName.getEditText().getText().toString();
        String confirmPassword = tilConfirmPassword.getEditText().getText().toString();

        presenter.validateRegisterCredentials(email, password, confirmPassword, displayName);
    }

    @Override
    public void setDisplayNameError() {
        tvError.setVisibility(View.VISIBLE);
        tilDisplayName.setError(displayNameError);
    }

    @Override
    public void setPasswordError() {
        tvError.setVisibility(View.VISIBLE);
        tilPassword.setError(passwordError);
    }

    @Override
    public void setEmailError() {
        tvError.setVisibility(View.VISIBLE);
        tilEmail.setError(emailError);
    }

    @Override
    public void setConfirmPasswordError() {
        tvError.setVisibility(View.VISIBLE);
        tilConfirmPassword.setError(confirmPasswordError);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void navigateToMain() {
        Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPasswordNotMatchError() {
        tilPassword.setError(passwordNotMatchError);
    }

    @Override
    public void setInvalidEmailFormatError() {
        tilEmail.setError(invalidEmailFormatError);
    }

    @Override
    public void resetErrors() {
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);
        tilDisplayName.setError(null);
    }

    @Override
    public void setGeneralError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
