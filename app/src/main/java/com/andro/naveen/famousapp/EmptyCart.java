package com.andro.naveen.famousapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andro.naveen.famousapp.myapplication.MyApplication;

public class EmptyCart extends AppCompatActivity {

    MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_cart);

        TextView textView = (TextView) findViewById(R.id.uName);

        myApplication = (MyApplication) getApplication();
        String id = myApplication.getSavedValue("Id");
        String user = myApplication.getSavedValue("NameS");
        String final_user = "Hello" + " " + user;
        textView.setText(final_user);
    }

    public void shopping(View v){

        Intent i = new Intent(EmptyCart.this,MainPageActivity.class);
        startActivity(i);
    }

    public void shopCategory(View v){

        Intent i = new Intent(EmptyCart.this,SelectItem.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(EmptyCart.this, MainPageActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
