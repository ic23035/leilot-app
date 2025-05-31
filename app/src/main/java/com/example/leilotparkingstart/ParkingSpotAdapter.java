package com.example.leilotparkingstart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ParkingSpotAdapter extends RecyclerView.Adapter<ParkingSpotAdapter.ParkingSpotViewHolder> {

    private List<ParkingSpot> parkingSpots;
    private ParkingSpot selectedSpot;
    private OnParkingSpotClickListener listener;

    public ParkingSpotAdapter(List<ParkingSpot> parkingSpots, OnParkingSpotClickListener listener) {
        this.parkingSpots = parkingSpots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParkingSpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parking_spot, parent, false);
        return new ParkingSpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingSpotViewHolder holder, int position) {
        ParkingSpot spot = parkingSpots.get(position);
        holder.nameTextView.setText(spot.getName());
        holder.priceTextView.setText("Τιμή: $" + spot.getPricePerHour());
        holder.notesTextView.setText(spot.getNotes());

        // Ενημέρωση της κατάστασης επιλογής
        if (selectedSpot != null && selectedSpot.equals(spot)) {
            holder.itemView.setSelected(true);  // Σήμανση ως επιλεγμένο
        } else {
            holder.itemView.setSelected(false); // Σήμανση ως μη επιλεγμένο
        }

        // Click listener για την επιλογή της θέσης
        holder.itemView.setOnClickListener(v -> {
            selectedSpot = spot;  // Αποθήκευση της επιλεγμένης θέσης
            notifyDataSetChanged();  // Ενημέρωση της προβολής για να αλλάξει το χρώμα
            listener.onParkingSpotClick(spot);  // Κλήση του listener
        });
    }

    @Override
    public int getItemCount() {
        return parkingSpots.size();
    }

    public static class ParkingSpotViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView priceTextView;
        TextView notesTextView;

        public ParkingSpotViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
        }
    }

    // Interface για το κλικ
    public interface OnParkingSpotClickListener {
        void onParkingSpotClick(ParkingSpot spot);
    }
}
