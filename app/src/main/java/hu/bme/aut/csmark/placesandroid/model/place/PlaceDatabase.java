package hu.bme.aut.csmark.placesandroid.model.place;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.os.AsyncTask;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.model.account.Account;

@Database(
        entities = {Place.class},
        version = 1
)
@TypeConverters(value = {Contact.class, Place.PlaceType.class})
public abstract class PlaceDatabase extends RoomDatabase{
    public abstract PlaceDao placeDao();

    static public List<Place> loadAllPlaces(final PlaceDatabase database){
        AsyncTask<Void, Void, List<Place>> execute = new AsyncTask<Void, Void, List<Place>>(){
            @Override
            protected List<Place> doInBackground(Void... voids) {
                return database.placeDao().getAll();
            }
        }.execute();
        try {
            return execute.get();
        } catch (Exception e) {
            return null;
        }
    }

    static public void registerPlace(final Place newPlace, final PlaceDatabase database){
        new AsyncTask<Void, Void, Place>() {

            @Override
            protected Place doInBackground(Void... voids) {
                newPlace.id = database.placeDao().insert(newPlace);
                return newPlace;
            }
        }.execute();
    }

    static public Place getPlacebyId(final long id, final PlaceDatabase database){
        final List<Place> allPlaces;
        allPlaces = PlaceDatabase.loadAllPlaces(database);
        AsyncTask<Void, Void, Place> execute = new AsyncTask<Void, Void, Place>() {
            @Override
            protected Place doInBackground(Void... voids) {
                if (id == -1) {
                    return null;
                } else {
                    int index = (int) id;
                    index--;
                    return allPlaces.get(index);
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
