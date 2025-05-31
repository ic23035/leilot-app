package com.example.leilotparkingstart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class UserStatsFragment extends Fragment {

    private ParkingDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_stats, container, false);

        TextView tvSessions = view.findViewById(R.id.tvSessions);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvCost = view.findViewById(R.id.tvCost);

        dbHelper = new ParkingDatabaseHelper(getContext());
        UserStats stats = dbHelper.getCachedUserStats("1"); // αν ο χρήστης έχει id = "1"


        tvSessions.setText(stats.getTotalSessions() + " ");

        long seconds = stats.getTotalTime();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String timeFormatted = String.format("%02dh %02dm", hours, minutes % 60);
        tvTime.setText(timeFormatted);

        tvCost.setText(String.format("%.2f $", stats.getTotalCost()));

        // Ανάκτηση στατιστικών
        UserStats stats1 = dbHelper.getUserStats(1);

        Log.d("Stats", "Sessions: " + stats.getTotalSessions() + ", Time: " + stats.getTotalTime() + ", Cost: " + stats.getTotalCost());

        return view;
    }
}
