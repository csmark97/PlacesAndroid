package hu.bme.aut.csmark.placesandroid.model.account;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.os.AsyncTask;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    List<Account> getAll();

//    @Query("SELECT * FROM account WHERE id = :id")
//    Account getAccountbyId(Long id);

    @Insert
    long insert(Account accounts);

    @Update
    void update(Account accounts);

    @Delete
    void delete(Account accounts);
}
