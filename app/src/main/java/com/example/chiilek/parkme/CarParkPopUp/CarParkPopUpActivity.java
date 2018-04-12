package com.example.chiilek.parkme.CarParkPopUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chiilek.parkme.Suggestion.SuggestionsActivity;
import com.example.chiilek.parkme.R;

/**
 * Created by DanSeb on 06/04/18.
 */

public class CarParkPopUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_info);
        getSupportActionBar().hide();
        //TODO Pass the choice over to navigation
        //this makes the SELECT button go to navigation
        ImageView b = findViewById(R.id.pop_up_select_button);
        TextView motorCarValue = findViewById(R.id.pop_up_motor_car_value);
        TextView heavyVehicleValue = findViewById(R.id.pop_up_heavy_vehicle_value);
        TextView motorCycleValue = findViewById(R.id.pop_up_motor_cycle_value);
        TextView carParkTypeValue = findViewById(R.id.pop_up_car_park_type_value);
        TextView parkingSystemValue = findViewById(R.id.pop_up_parking_system_value);
        TextView freeParkingValue = findViewById(R.id.pop_up_free_parking_value);
        TextView shortTermParkingValue = findViewById(R.id.pop_up_short_term_parking_value);
        TextView nightParkingValue = findViewById(R.id.pop_up_night_parking_value);
        

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarParkPopUpActivity.this, SuggestionsActivity.class));
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9), (int)(height*.9));
    }

}
