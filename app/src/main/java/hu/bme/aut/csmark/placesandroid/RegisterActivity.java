package hu.bme.aut.csmark.placesandroid;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.model.account.Account;
import hu.bme.aut.csmark.placesandroid.model.account.AccountDatabase;

public class RegisterActivity extends AppCompatActivity {

    static private AccountDatabase database;
    static private Account account;
    static private List<Account> allAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnDoneRegister = findViewById(R.id.btnDoneRegister);
        final EditText etRegEmail = findViewById(R.id.etRegEmail);
        final EditText etRegPassword = findViewById(R.id.etRegPassword);
        final EditText etRegName = findViewById(R.id.etRegName);


        database = Room.databaseBuilder(
                getApplicationContext(),
                AccountDatabase.class,
                getString(R.string.accountTable)
        ).build();

        btnDoneRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if (etRegEmail.getText().toString().isEmpty()) {
                    etRegEmail.requestFocus();
                    etRegEmail.setError(getString(R.string.please_enter_your_email_correctly));
                    return;
                }

                if (etRegPassword.getText().toString().isEmpty()) {
                    etRegPassword.requestFocus();
                    etRegPassword.setError(getString(R.string.please_enter_yout_password_correctly));
                    return;
                }

                if (etRegName.getText().toString().isEmpty()) {
                    etRegName.requestFocus();
                    etRegName.setError(getString(R.string.please_enter_your_name));
                    return;
                }

                account = new Account(etRegEmail.getText().toString(), etRegPassword.getText().toString(), etRegName.getText().toString());

                AccountDatabase.registerAccount(account, database);

                allAccounts = AccountDatabase.loadAllAccounts(database);

                Log.i(getString(R.string.succesful_addition), allAccounts.get(allAccounts.size() - 1).name);
                finish();
            }


        });

    }



}
