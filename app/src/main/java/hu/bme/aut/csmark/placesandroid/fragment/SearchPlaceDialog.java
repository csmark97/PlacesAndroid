package hu.bme.aut.csmark.placesandroid.fragment;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.R;
import hu.bme.aut.csmark.placesandroid.model.place.Contact;
import hu.bme.aut.csmark.placesandroid.model.place.Place;
import hu.bme.aut.csmark.placesandroid.model.place.PlaceDatabase;

public class SearchPlaceDialog extends DialogFragment {
    private EditText searcher;

    public static final String TAG = "RatePlaceDialogFragment";
    private FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.search_place, null);
        searcher = contentView.findViewById(R.id.searcherEditText);

        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.search_place_by_name)
                .setView(contentView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        HomeFragment homeFragment = new HomeFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.searcher), searcher.getText().toString());
                        homeFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.main_container, homeFragment);
                        fragmentTransaction.commit();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }
}
