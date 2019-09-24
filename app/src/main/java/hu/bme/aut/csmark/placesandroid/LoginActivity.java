package hu.bme.aut.csmark.placesandroid;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.model.account.Account;
import hu.bme.aut.csmark.placesandroid.model.account.AccountDatabase;

public class LoginActivity extends AppCompatActivity {

    private AccountDatabase database;
    private List<Account> allAccounts;
    private Account userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        final EditText etEmailAddress = findViewById(R.id.etEmailAddress);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final Button btnRegister = findViewById(R.id.btnRegister);

        database = Room.databaseBuilder(
                getApplicationContext(),
                AccountDatabase.class,
                getString(R.string.accountTable)
        ).build();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
            if (etEmailAddress.getText().toString().isEmpty()) {
                etEmailAddress.requestFocus();
                etEmailAddress.setError(getString(R.string.please_enter_your_email));
                return;
            }

            if (etPassword.getText().toString().isEmpty()) {
                etPassword.requestFocus();
                etPassword.setError(getString(R.string.please_enter_your_password));
                return;
            }

            allAccounts = database.loadAllAccounts(database);
            userAccount = new Account(etEmailAddress.getText().toString(), etPassword.getText().toString());
            boolean ok = false;
            if (userAccount.login(allAccounts)) {
                ok = true;
            }

            if (ok){
                Log.i(getString(R.string.enter_succesful), userAccount.name + getString(R.string.space) + userAccount.id);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("userID", userAccount.id);
                startActivity(intent);
                finish();
            } else {
                Log.i(getString(R.string.enter_failed), getString(R.string.wrong_email_or_password));
                Snackbar.make(view, R.string.wrongEmailOrPassword, Snackbar.LENGTH_LONG).show();
                etEmailAddress.requestFocus();
                etEmailAddress.setError(getString(R.string.please_try_again));
            }
            }
        });

        btnRegister.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(final View view) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            }
        }));
    }
}