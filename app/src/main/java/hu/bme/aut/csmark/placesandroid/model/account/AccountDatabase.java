package hu.bme.aut.csmark.placesandroid.model.account;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;

import java.util.List;

@Database(
        entities = {Account.class},
        version = 1
)
public abstract class AccountDatabase extends RoomDatabase{
    public abstract AccountDao accountDao();

    static public List<Account> loadAllAccounts(final AccountDatabase database){
        AsyncTask<Void, Void, List<Account>> execute = new AsyncTask<Void, Void, List<Account>>(){
            @Override
            protected List<Account> doInBackground(Void... voids) {
                return database.accountDao().getAll();
            }
        }.execute();
        try {
            return execute.get();
        } catch (Exception e) {
            return null;
        }
    }

    static public void registerAccount(final Account newAccount, final AccountDatabase database){
        new AsyncTask<Void, Void, Account>() {

            @Override
            protected Account doInBackground(Void... voids) {
                newAccount.id = database.accountDao().insert(newAccount);
                return newAccount;
            }
        }.execute();
    }

    static public Account getAccountbyId(final long id, final AccountDatabase database){
        final List<Account> allAccounts;
        allAccounts = AccountDatabase.loadAllAccounts(database);
        AsyncTask<Void, Void, Account> execute = new AsyncTask<Void, Void, Account>() {
            @Override
            protected Account doInBackground(Void... voids) {
                if (id == -1) {
                    return new Account("notValid","");
                } else {
                    int index = (int) id;
                    index--;
                    return allAccounts.get(index);
                }
            }
        }.execute();
        try {
            return execute.get();
        } catch (Exception e) {
            return null;
        }
    }
}
