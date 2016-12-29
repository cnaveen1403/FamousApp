package com.andro.naveen.famousapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectItem extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.listView);
 //       ArrayList<String> epl = new ArrayList<>();
        ArrayAdapter<String> aStringAdapter = new ArrayAdapter<String>(
                SelectItem.this, android.R.layout.simple_expandable_list_item_1,getResources().getStringArray(R.array.categories));
        listView.setAdapter(aStringAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clickedItem = (String) parent.getAdapter().getItem(position);

        if (clickedItem.equals("Grocery Items")){

            Intent intent = new Intent(SelectItem.this, GroceryJson.class);
            startActivity(intent);
        }
        else if (clickedItem.equals("Food Items")){

            Intent intent = new Intent(SelectItem.this, FoodItems.class);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
            Intent i = new Intent(SelectItem.this, MainPageActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
