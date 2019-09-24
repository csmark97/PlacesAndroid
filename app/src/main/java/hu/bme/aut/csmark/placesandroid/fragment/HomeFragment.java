package hu.bme.aut.csmark.placesandroid.fragment;


import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
public class HomeFragment extends Fragment implements PlacesAdapter.PlaceClickListener,
        AddPlaceFragment.AddPlaceListener,
        RatePlaceDialog.PlaceDialogListener{

    private RecyclerView recyclerView;

    private static PlacesAdapter adapter;

    private static PlaceDatabase database;

    private View contentView;

    private static String searcherString;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, null);

        FloatingActionButton fab = contentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity) getView().getContext()).getSupportFragmentManager();
                SearchPlaceDialog searchPlaceDialog = new SearchPlaceDialog();
                searchPlaceDialog.show(manager, RatePlaceDialog.TAG);
            }
        });

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                PlaceDatabase.class,
                getString(R.string.place)
        ).build();

        recyclerView = contentView.findViewById(R.id.AllPlaceRecyclerView);
        adapter = new PlacesAdapter(this, 0);

        searcherString = getArguments().get(getString(R.string.searcher)).toString();

        if (searcherString.equals(getString(R.string.no_search))) {
            loadItemsInBackground();
        } else {
            searcherString = getString(R.string.percentage) + searcherString + getString(R.string.percentage);
            loadSearchedItemsInBackGround();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(contentView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return contentView;
    }

    private static void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Place>>() {

            @Override
            protected List<Place> doInBackground(Void... voids) {
                return database.placeDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Place> places) {
                adapter.update(places);
            }
        }.execute();
    }

    private static void loadSearchedItemsInBackGround() {
        new AsyncTask<Void, Void, List<Place>>() {

            @Override
            protected List<Place> doInBackground(Void... voids) {
                return database.placeDao().getSearched(searcherString);
            }

            @Override
            protected void onPostExecute(List<Place> places) {
                adapter.update(places);
            }
        }.execute();
    }

    @Override
    public void onPlaceChanged(final Place item) {
        placeChanged(item);
    }

    public static void placeChanged(final Place item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.placeDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("HomeFragment", "Place update was successful");
            }
        }.execute();
    }

    @Override
    public void onPlaceCreated(Place newPlace) {
        placeCreated(newPlace);
    }

    public static void placeCreated(final Place newPlace) {
        new AsyncTask<Void, Void, Place>() {

            @Override
            protected Place doInBackground(Void... voids) {
                newPlace.id = database.placeDao().insert(newPlace);
                return newPlace;
            }

            @Override
            protected void onPostExecute(Place place) {
                adapter.addItem(place);
            }
        }.execute();
    }

    @Override
    public void onPlaceDeleted(Place item) {

    }

    @Override
    public void onPlaceRated(Place place) {
        placeRated(place);
    }

    public static void placeRated(final Place item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.placeDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("HomeFragment", "Place update was successful");
            }
        }.execute();
    }
}
