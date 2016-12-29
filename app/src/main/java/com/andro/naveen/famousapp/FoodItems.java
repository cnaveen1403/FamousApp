package com.andro.naveen.famousapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FoodItems extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView foodlistView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_items);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodlistView = (ListView) findViewById(R.id.foodListView);
        ArrayAdapter<String> bStringAdapter = new ArrayAdapter<String>(
                FoodItems.this, android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.food_items));
        foodlistView.setAdapter(bStringAdapter);
        foodlistView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clickedItem = (String) parent.getAdapter().getItem(position);

        if (clickedItem.equals("Vegetarian")){

            Intent i = new Intent(FoodItems.this, VegetarianJson.class);
            startActivity(i);

        }
        else if (clickedItem.equals("Non Vegetarian")){

            Intent i = new Intent(FoodItems.this, NonVegetarianJson.class);
            startActivity(i);
        }

    }



    //For top right return to Home button

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home){
            Intent i = new Intent(FoodItems.this, MainPageActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
