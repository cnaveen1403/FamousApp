package com.andro.naveen.famousapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.andro.naveen.famousapp.adapters.ItemsAdaptar;
import com.andro.naveen.famousapp.parsing.Grocery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VegetarianJson extends AppCompatActivity {

    ListView listViewVeg;
    ArrayList<Grocery> vegArrayList;
 //   String global_pos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vegetarian_json);

        String jsonVeg = loadVegetarian();
        try {
            JSONObject jsonRootObj = new JSONObject(jsonVeg);
            JSONArray vegetarian = jsonRootObj.getJSONArray("vegetarian");
            vegArrayList = new ArrayList<>(vegetarian.length());

            for (int i = 0; i < vegetarian.length(); i++) {
                JSONObject jsonObject = vegetarian.getJSONObject(i);

                String title = jsonObject.optString("category").toString();
                String image = jsonObject.optString("photo").toString();
                String item = jsonObject.optString("items").toString();

                vegArrayList.add(new Grocery(title, image,item));

                //    Toast.makeText(GroceryJson.this,  groceryArrayList+ "  Clicked Glocery", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        bindData();

        listViewVeg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Grocery mitem = vegArrayList.get (position);
                String abc = mitem.getCategory();
         //       global_pos = (Integer.toString(position));
                Intent i = new Intent(VegetarianJson.this, Product.class);
         //       i.putExtra("Position", global_pos);
                i.putExtra("Name","Vegetarian");
                i.putExtra("Title",abc);
                startActivity(i);


            }
        });

    }

    private void bindData() {

        listViewVeg = (ListView) findViewById(R.id.listViewVeg);
        ItemsAdaptar itemsAdapter = new ItemsAdaptar(this,R.layout.single_row_item,vegArrayList);
        listViewVeg.setAdapter(itemsAdapter);
    }

    public String loadVegetarian() {

        String json = null;
        try{
            InputStream is = getAssets().open("vegetarian.json");
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
            Intent i = new Intent(VegetarianJson.this, MainPageActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
