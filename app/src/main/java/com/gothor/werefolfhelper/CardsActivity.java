package com.gothor.werefolfhelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class CardsActivity extends AppCompatActivity {

    Button werewolves_minus, werewolves_plus, bt_start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cards);
    }
}
