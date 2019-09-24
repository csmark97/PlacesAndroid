package hu.bme.aut.csmark.placesandroid.model.place;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.os.AsyncTask;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.model.account.Account;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM place")
    List<Place> getAll();

    @Query("SELECT * FROM place WHERE owner_id=:param ORDER BY name")
    List<Place> getAllOwn(Long param);

    @Query("SELECT * FROM place where name LIKE :param")
    List<Place> getSearched(String param);

    @Insert
    long insert(Place places);

    @Update
    void update(Place places);

    @Delete
    void delete(Place places);
}
