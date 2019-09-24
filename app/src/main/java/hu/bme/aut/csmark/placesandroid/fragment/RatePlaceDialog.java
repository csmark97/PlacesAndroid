package hu.bme.aut.csmark.placesandroid.fragment;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.R;
import hu.bme.aut.csmark.placesandroid.adapter.PlacesAdapter;
import hu.bme.aut.csmark.placesandroid.model.account.AccountDatabase;
import hu.bme.aut.csmark.placesandroid.model.place.Contact;
import hu.bme.aut.csmark.placesandroid.model.place.Place;
import hu.bme.aut.csmark.placesandroid.model.place.PlaceDatabase;

public class RatePlaceDialog extends DialogFragment {

    private TextView name;
    private TextView rating;
    private TextView address;
    private TextView phoneNumber;
    private TextView web;

    private Spinner ratingSpinner;

    Place actualPlace;




    public static final String TAG = "RatePlaceDialogFragment";
    private FragmentTransaction fragmentTransaction;
    private PlaceDatabase database;
    private List<Place> places;

    public interface PlaceDialogListener {
        void onPlaceRated(Place newItem);
    }

    private PlaceDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = new HomeFragment();

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                PlaceDatabase.class,
                getString(R.string.place)
        ).build();
        long id = getArguments().getLong(getString(R.string.userID));
        places = PlaceDatabase.loadAllPlaces(database);
        actualPlace = new Place();
        actualPlace = places.get((int)(id - 1));


    }

    private Place ratePlace() {
        if(actualPlace.ratingNumber == null) {
            actualPlace.ratingNumber = 1;
            actualPlace.rating = Double.parseDouble(ratingSpinner.getSelectedItem().toString());
        } else {
            double newRating = Double.parseDouble(ratingSpinner.getSelectedItem().toString());
            actualPlace.rating = ( actualPlace.rating * actualPlace.ratingNumber + newRating ) / (actualPlace.ratingNumber + 1 );
            actualPlace.ratingNumber++;
        }
        return actualPlace;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.rate_place, null);
        name = contentView.findViewById(R.id.nameTextView);
        rating = contentView.findViewById(R.id.ratingTextView);
        address = contentView.findViewById(R.id.addressTextView);
        phoneNumber = contentView.findViewById(R.id.phoneTextView);
        web = contentView.findViewById(R.id.webTextView);
        ratingSpinner = contentView.findViewById(R.id.ratingSpinner);
        ratingSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.rating_options)));

        name.setText(actualPlace.name);
        if(actualPlace.ratingNumber == null) {
            rating.setText(R.string.this_place_has_not_been_rated_yet);
        } else {
            rating.setText(actualPlace.rating.toString());
        }

        if(actualPlace.contact != null) {
            Contact contact = Contact.toCantact(actualPlace.contact);
            address.setText(contact.getZip() + " " + contact.getCity() + " " + contact.getStreet() + " " + contact.getHouseNumber());
            phoneNumber.setText(contact.getPhoneNumber());
            web.setText(contact.getWeb());
        }

        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.place_evaulation)
                .setView(contentView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPlaceRated(ratePlace());
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment homeFragment = new HomeFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.searcher), getString(R.string.no_search));
                        homeFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.main_container, homeFragment);
                        fragmentTransaction.commit();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
