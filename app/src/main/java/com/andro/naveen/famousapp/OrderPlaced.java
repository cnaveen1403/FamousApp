package com.andro.naveen.famousapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OrderPlaced extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
    }

    public void continueShopping(View view){

        Intent i = new Intent(OrderPlaced.this,MainPageActivity.class);
        startActivity(i);
        finish();
    }
}
