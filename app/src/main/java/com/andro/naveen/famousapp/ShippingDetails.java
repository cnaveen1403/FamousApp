package com.andro.naveen.famousapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andro.naveen.famousapp.database.DatabaseHelper;
import com.andro.naveen.famousapp.myapplication.MyApplication;
import com.andro.naveen.famousapp.parsing.Address;
import com.andro.naveen.famousapp.PaymentPage;

import java.util.ArrayList;

public class ShippingDetails extends AppCompatActivity {


    EditText nameDelivery, postCodeDelivery, addressDelivery,
            landmarkDelivery, cityDelivery,stateDelivery, phoneDelivery, emailDelivery;
    MyApplication myApplication;
    String email, name, id, status;
    String dName, dPost, dAddress, dLand, dCity, dState, dPhone, dEmail;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ArrayList<Address> mAddress;
    String tab_sl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_details);

        status = getIntent().getStringExtra("Status");
        init();

        if (status.equals("New")){
            getSet();
        }
        else if (status.equals("Old")){

            myApplication = (MyApplication) getApplication();
            id = myApplication.getSavedValue("Id");
            mAddress = databaseHelper.getTableAddress(id);
            setOldAddress();
        }
    }

    private void setOldAddress() {

        Address address = mAddress.get(0);
        tab_sl = address.getCol_Sl();
        nameDelivery.setText(address.getdName());
        postCodeDelivery.setText(address.getdPost());
        addressDelivery.setText(address.getdAddress());
        landmarkDelivery.setText(address.getdLand());
        cityDelivery.setText(address.getdCity());
        stateDelivery.setText(address.getdState());
        phoneDelivery.setText(address.getdPhone());
        emailDelivery.setText(address.getdEmail());
    }

    private void getSet() {

        myApplication = (MyApplication) getApplication();
        email = myApplication.getSavedValue("EmailS");
        name = myApplication.getSavedValue("NameS");
        id = myApplication.getSavedValue("Id");
        nameDelivery.setText(name);
        emailDelivery.setText(email);
    }

    private void init() {

        nameDelivery = (EditText) findViewById(R.id.inputName);
        postCodeDelivery = (EditText) findViewById(R.id.inputPincode);
        addressDelivery = (EditText) findViewById(R.id.inputAddress);
        landmarkDelivery = (EditText) findViewById(R.id.inputLandmark);
        cityDelivery = (EditText) findViewById(R.id.inputCity);
        stateDelivery = (EditText) findViewById(R.id.inputState);
        phoneDelivery = (EditText) findViewById(R.id.inputPhone);
        emailDelivery = (EditText) findViewById(R.id.inputEmail);
    }

    public void getDeliveryAddress(View view){

        if (!validate()) {
            //if the entered details are not valid, goto the following function
            onValidFailed();
            return;
        }
        else {

            Address mAddress = new Address();
            mAddress.setId(id);
            mAddress.setdName(dName);
            mAddress.setdPost(dPost);
            mAddress.setdAddress(dAddress);
            mAddress.setdLand(dLand);
            mAddress.setdCity(dCity);
            mAddress.setdState(dState);
            mAddress.setdPhone(dPhone);
            mAddress.setdEmail(dEmail);


            if (status.equals("New")){

            //Insert the entered address into database
                databaseHelper.insertAddress(mAddress);
            }
            else if (status.equals("Old")){


                databaseHelper.editAddress(mAddress,tab_sl);
            }
        }
        resetText();
        Intent intent = new Intent(ShippingDetails.this, PaymentPage.class);
        startActivity(intent);
    }

    private void resetText() {

        nameDelivery.setText("");
        postCodeDelivery.setText("");
        addressDelivery.setText("");
        landmarkDelivery.setText("");
        cityDelivery.setText("");
        stateDelivery.setText("");
        phoneDelivery.setText("");
        emailDelivery.setText("");
    }

    public boolean validate() {

        // This function is to check whether the entered email address and password are in the prescribed format or not

        boolean valid = true;


        dName = nameDelivery.getText().toString();
        dPost = postCodeDelivery.getText().toString();
        dAddress = addressDelivery.getText().toString();
        dLand = landmarkDelivery.getText().toString();
        dCity = cityDelivery.getText().toString();
        dState = stateDelivery.getText().toString();
        dPhone = phoneDelivery.getText().toString();
        dEmail = emailDelivery.getText().toString();


        if (dName.isEmpty() || dName.length()< 7) {
            nameDelivery.setError("Enter your Full Name");
            valid = false;
        } else {
            nameDelivery.setError(null);
        }

        if (dPost.isEmpty() || dPost.length()< 6) {
            postCodeDelivery.setError("Enter a valid PinCode");
            valid = false;
        } else {
            postCodeDelivery.setError(null);
        }

        if (dAddress.isEmpty()) {
            addressDelivery.setError("Enter your delivery Address");
            valid = false;
        } else {
            addressDelivery.setError(null);
        }

        if (dLand.isEmpty()) {
            landmarkDelivery.setError("Enter your nearest Landmark");
            valid = false;
        } else {
            landmarkDelivery.setError(null);
        }

        if (dCity.isEmpty()) {
            cityDelivery.setError("Enter your City");
            valid = false;
        } else {
            cityDelivery.setError(null);
        }

        if (dState.isEmpty()) {
            stateDelivery.setError("Enter your State");
            valid = false;
        } else {
            stateDelivery.setError(null);
        }

        if (dPhone.isEmpty() || !Patterns.PHONE.matcher(dPhone).matches() || dPhone.length()<10) {
            phoneDelivery.setError("Enter a valid Phone number");
            valid = false;
        } else {
            phoneDelivery.setError(null);
        }


        if (dEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(dEmail).matches()) {
            emailDelivery.setError("Enter a valid email address");
            valid = false;
        } else {
            emailDelivery.setError(null);
        }
        return valid;
    }


    public void onValidFailed() {
        Toast.makeText(getBaseContext(), "Invalid Entries", Toast.LENGTH_LONG).show();
    }
}
