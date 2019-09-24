package hu.bme.aut.csmark.placesandroid.adapter;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.csmark.placesandroid.R;
import hu.bme.aut.csmark.placesandroid.fragment.RatePlaceDialog;
import hu.bme.aut.csmark.placesandroid.model.place.Contact;
import hu.bme.aut.csmark.placesandroid.model.place.Place;

public class PlacesAdapter
        extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private final List<Place> items;

    private PlaceClickListener listener;

    private int layoutType;    //0-all, 1-own
    private Bundle bundle;

    public interface PlaceClickListener {
        void onPlaceChanged(Place item);
        void onPlaceDeleted(Place item);
    }

    public PlacesAdapter(PlaceClickListener listener, int layoutType) {
        this.listener = listener;
        items = new ArrayList<>();
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public PlacesAdapter.PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (layoutType == 0) {
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.place_list, parent, false);
        } else if (layoutType == 1) {
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.own_place_list, parent, false);
        } else {    //NEVER HAPPEN
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.place_list, parent, false);
        }

        return new PlacesViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        Place item = items.get(position);
        holder.nameTextView.setText(item.name);
        holder.categoryTextView.setText(item.placeType.name());

        if(item.contact != null) {
            Contact contact = Contact.toCantact(item.contact);
            holder.addressTextView.setText(contact.getCity() + ", " + contact.getStreet() + " " + contact.getHouseNumber() + ".");
        }

        if(item.rating == null) {
            holder.ratingTextView.setText(" ");
        } else {
            holder.ratingTextView.setText(roundTwoDecimals(item.rating).toString());
        }
        holder.iconImageButton.setImageResource(getImageResource(item.placeType));

        holder.item = item;
    }

    private Double roundTwoDecimals(Double value) {

        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private @DrawableRes
    int getImageResource(Place.PlaceType placeType) {
        @DrawableRes int ret;
        switch (placeType) {
            case BOLT:
                ret = R.drawable.ic_menu_places;
                break;
            case INTEZMENY:
                ret = R.drawable.ic_menu_places;
                break;
            case ETTEREM:
                ret = R.drawable.ic_menu_places;
                break;
            case SZALLAS:
                ret = R.drawable.ic_menu_places;
                break;
            case SZORAKOZAS:
                ret = R.drawable.ic_menu_places;
                break;
            case BEVASARLOKOZPONT:
                ret = R.drawable.ic_menu_places;
                break;
            default:
                ret = 0;
        }
        return ret;
    }

    public void addItem(Place placeItem) {
        items.add(placeItem);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<Place> placeItems) {
        items.clear();
        items.addAll(placeItems);
        notifyDataSetChanged();
    }

    public void delete(Place placeItems) {
        int position = items.indexOf(placeItems);
        items.remove(placeItems);
        notifyItemRemoved(position);
    }

    class PlacesViewHolder extends RecyclerView.ViewHolder {

        ImageButton iconImageButton;
        TextView nameTextView;
        TextView categoryTextView;
        TextView ratingTextView;
        TextView addressTextView;
        ImageButton removeButton;

        Place item;

        PlacesViewHolder(final View itemView) {
            super(itemView);
            iconImageButton = itemView.findViewById(R.id.PlaceIconImageButton);
            nameTextView = itemView.findViewById(R.id.PlaceName);
            categoryTextView = itemView.findViewById(R.id.PlaceCategory);
            ratingTextView = itemView.findViewById(R.id.Rating);
            addressTextView = itemView.findViewById(R.id.PlaceAddress);

            bundle = new Bundle();


            if(layoutType == 0) {
                iconImageButton.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            FragmentManager manager = ((AppCompatActivity) itemView.getContext()).getSupportFragmentManager();
                            RatePlaceDialog ratePlaceDialog = new RatePlaceDialog();
                            bundle.putLong("userID", item.id);
                            ratePlaceDialog.setArguments(bundle);
                            ratePlaceDialog.show(manager, RatePlaceDialog.TAG);
                        }
                    }
                );
            }

            if (layoutType == 1) {
                removeButton = itemView.findViewById(R.id.PlaceRemoveButton);
                removeButton.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            delete(item);
                            listener.onPlaceDeleted(item);
                        }
                    });
            }
        }


    }
}
