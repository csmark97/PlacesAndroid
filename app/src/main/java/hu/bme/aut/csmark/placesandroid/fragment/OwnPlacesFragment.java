package hu.bme.aut.csmark.placesandroid.fragment;


import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.R;
import hu.bme.aut.csmark.placesandroid.adapter.PlacesAdapter;
import hu.bme.aut.csmark.placesandroid.model.place.Place;
import hu.bme.aut.csmark.placesandroid.model.place.PlaceDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnPlacesFragment extends Fragment implements PlacesAdapter.PlaceClickListener, AddPlaceFragment.AddPlaceListener {

    private RecyclerView recyclerView;

    private static PlacesAdapter adapter;

    private static PlaceDatabase database;

    private View contentView;

    private static long userId;

    public OwnPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_own_places, null);

        userId = getArguments().getLong(getString(R.string.userID));

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                PlaceDatabase.class,
                getString(R.string.place)
        ).build();

        recyclerView = contentView.findViewById(R.id.OwnPlaceRecyclerView);
        adapter = new PlacesAdapter(this, 1);
        loadOwnItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return contentView;
    }

    private static void loadOwnItemsInBackground() {
        new AsyncTask<Void, Void, List<Place>>() {

            @Override
            protected List<Place> doInBackground(Void... voids) {
                return database.placeDao().getAllOwn(userId);
            }

            @Override
            protected void onPostExecute(List<Place> places) {
                adapter.update(places);
            }
        }.execute();
    }

    @Override
    public void onPlaceChanged(Place item) {

    }

    @Override
    public void onPlaceDeleted(final Place placeItem) {
        placeDeleted(placeItem);
    }

    private static void placeDeleted(final Place placeItem) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.placeDao().delete(placeItem);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("MainActivity", "Place deletion was successful");
            }
        }.execute();
    }

    @Override
    public void onPlaceCreated(Place newPlace) {

    }
}
