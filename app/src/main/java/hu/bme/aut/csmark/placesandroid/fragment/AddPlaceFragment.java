package hu.bme.aut.csmark.placesandroid.fragment;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import hu.bme.aut.csmark.placesandroid.R;
import hu.bme.aut.csmark.placesandroid.adapter.PlacesAdapter;
import hu.bme.aut.csmark.placesandroid.model.place.Contact;
import hu.bme.aut.csmark.placesandroid.model.place.Place;
import hu.bme.aut.csmark.placesandroid.model.place.PlaceDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlaceFragment extends Fragment {

    public static final String TAG = "AddPlaceFragment";
    private EditText nameEditText;
    private EditText zipEditText;
    private EditText cityEditText;
    private EditText streetEditText;
    private EditText houseNumberEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText webEditText;
    private Spinner categorySpinner;
    private CheckBox authenticationCheckBox;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private static PlaceDatabase database;
    private long userId;
    private AddPlaceListener listener;

    public interface AddPlaceListener {
        void onPlaceCreated(Place newPlace);
    }


    public AddPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener = new HomeFragment();

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                PlaceDatabase.class,
                getString(R.string.place)
        ).build();
    }

    private Place getPlaceItem() {
        Place place = new Place();

        place.ownerId = userId;

        place.name = nameEditText.getText().toString();

        Contact contact = new Contact(Integer.parseInt(zipEditText.getText().toString()),
                cityEditText.getText().toString(),
                streetEditText.getText().toString(),
                Integer.parseInt(houseNumberEditText.getText().toString()),
                phoneNumberEditText.getText().toString(),
                emailEditText.getText().toString(),
                webEditText.getText().toString());

        place.contact = Contact.fromContact(contact);
        place.placeType = Place.PlaceType.getByOrdinal(categorySpinner.getSelectedItemPosition());
        Log.i(getString(R.string.succesful_creation), place.name + getString(R.string.ownerLog) + place.ownerId);
        return place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userId = getArguments().getLong(getString(R.string.userID));

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add, null);
        nameEditText = contentView.findViewById(R.id.PlaceNameEditText);
        zipEditText = contentView.findViewById(R.id.PlaceZipEditText);
        cityEditText = contentView.findViewById(R.id.PlaceCityEditText);
        streetEditText = contentView.findViewById(R.id.PlaceStreetEditText);
        houseNumberEditText = contentView.findViewById(R.id.HouseNumberNameEditText);
        phoneNumberEditText = contentView.findViewById(R.id.PhoneNumberNameEditText);
        emailEditText = contentView.findViewById(R.id.EmailEditText);
        webEditText = contentView.findViewById(R.id.WebEditText);
        categorySpinner = contentView.findViewById(R.id.PlaceCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.category_items)));
        authenticationCheckBox = contentView.findViewById(R.id.PlaceAuthentication);
        Button okButton = contentView.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view) {
                listener.onPlaceCreated(getPlaceItem());
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.searcher), getString(R.string.no_search));
                homeFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_container, homeFragment);
                fragmentTransaction.commit();
            }
        });

        return contentView;
    }
}
