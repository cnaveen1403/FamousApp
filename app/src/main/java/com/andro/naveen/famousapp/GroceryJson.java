package com.andro.naveen.famousapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andro.naveen.famousapp.adapters.ItemsAdaptar;
import com.andro.naveen.famousapp.parsing.Grocery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GroceryJson extends AppCompatActivity{
    
    ListView listView;
    ArrayList<Grocery> groceryArrayList;
 //   String global_pos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_json);
        String jsonGrocery = loadGrocery();
        try {
            JSONObject jsonRootObj = new JSONObject(jsonGrocery);
            JSONArray grocery = jsonRootObj.getJSONArray("grocery");
            groceryArrayList = new ArrayList<>(grocery.length());

            for (int i = 0; i < grocery.length(); i++) {
                JSONObject jsonObject = grocery.getJSONObject(i);

                String title = jsonObject.optString("category").toString();
                String image = jsonObject.optString("photo").toString();
                String item = jsonObject.optString("items").toString();

                groceryArrayList.add(new Grocery(title, image,item));

            //    Toast.makeText(GroceryJson.this,  groceryArrayList+ "  Clicked Glocery", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        bindData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grocery mitem = groceryArrayList.get (position);
                String abc = mitem.getCategory();

             //   global_pos = (Integer.toString(position));
                Intent i = new Intent(GroceryJson.this, Product.class);
            //    i.putExtra("Position", global_pos);
                i.putExtra("Name","Grocery");
                i.putExtra("Title",abc);
                startActivity(i);

            }
        });

    }

    private void bindData() {

        listView = (ListView) findViewById(R.id.listViewGrocery);
        ItemsAdaptar itemsAdapter = new ItemsAdaptar(this,R.layout.single_row_item,groceryArrayList);
        listView.setAdapter(itemsAdapter);
    }

    public String loadGrocery() {

        String json = null;
        try{
            InputStream is = getAssets().open("grocery.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        return json;
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
            Intent i = new Intent(GroceryJson.this, MainPageActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
