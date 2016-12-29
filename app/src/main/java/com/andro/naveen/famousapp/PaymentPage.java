package com.andro.naveen.famousapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.naveen.famousapp.database.DatabaseHelper;
import com.andro.naveen.famousapp.myapplication.MyApplication;
import com.andro.naveen.famousapp.parsing.Address;
import com.andro.naveen.famousapp.parsing.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PaymentPage extends AppCompatActivity {

    private static final String ORDER_URL = "http://localhost/shop/api/order";
    ArrayList<Address> mAddress;
    ArrayList<Cart> cartArrayList;
    MyApplication myApplication;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    String id, displaySum;
    String tab_address, tab_city, tab_pincode, tab_name, tab_phone, tab_landmark, tab_state;
    TextView tvAddress, tvCity, tvPin, tvName, tvPhone, tvPrice;
    ImageView edit_address;
    CheckBox cbCod;
    int totalList = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        init();

        myApplication = (MyApplication) getApplication();
        id = myApplication.getSavedValue("Id");
        displaySum = myApplication.getSavedValue("Total");

        mAddress = databaseHelper.getTableAddress(id);

        tvPrice.setText(displaySum);
        getAddress();
        setAddress();

        //On edit address clicked
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentPage.this,ShippingDetails.class);
                intent.putExtra("Status","Old");
                startActivity(intent);
            }
        });
    }

    private void init() {

        tvAddress = (TextView) findViewById(R.id.delAddress);
        tvCity = (TextView) findViewById(R.id.delCity);
        tvPin = (TextView) findViewById(R.id.delPincode);
        tvName = (TextView) findViewById(R.id.delName);
        tvPhone = (TextView) findViewById(R.id.delPhone);
        edit_address = (ImageView) findViewById(R.id.edit_address);
        tvPrice = (TextView) findViewById(R.id.displayPrice);
        cbCod = (CheckBox) findViewById(R.id.checkboxCod);
    }

    private void getAddress() {

        Address address = mAddress.get(0);
        tab_address = address.getdAddress();
        tab_city = address.getdCity();
        tab_pincode = address.getdPost();
        tab_name = address.getdName();
        tab_phone = address.getdPhone();
        tab_landmark = address.getdLand();
        tab_state = address.getdState();
    }

    private void setAddress() {

        tvAddress.setText(tab_address);
        tvCity.setText(tab_city);
        tvPin.setText(tab_pincode);
        tvName.setText(tab_name);
        tvPhone.setText(tab_phone);
    }

    public void placeOrder(View view){

        if ((cbCod).isChecked()){
            placeOrder ();
        }else {
            Toast.makeText(getApplicationContext()," Please Select a Payment Option ",Toast.LENGTH_LONG).show();
        }
    }

    public void placeOrder (){
        cartArrayList = databaseHelper.getOrderDetails(id);
        totalList = cartArrayList.size();

        //     itemcount.setText("Items (" + totalList + ")");

        try{
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < totalList; i++) {
                Cart mcart = cartArrayList.get(i);
                String price = mcart.getPrice();
                price = price.replace("Rs ", "");
                String quant = mcart.getQuantity();
                String itemName = mcart.getName();
                int quan = Integer.parseInt(quant);

                JSONObject jsonObj= new JSONObject();
                jsonObj.put("item_name", itemName);
                jsonObj.put("price", price);
                jsonObj.put("quantity", quan);

                jsonArray.put(jsonObj);
            }

            String itemDetails = jsonArray.toString();
            PlaceOrderAsyncTask orderPlaced = new PlaceOrderAsyncTask();
            orderPlaced.execute(itemDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeCartItems (){
        cartArrayList = databaseHelper.getOrderDetails(id);
        totalList = cartArrayList.size();
        databaseHelper = new DatabaseHelper(getApplicationContext());

        for (int i = 0; i < totalList; i++) {
            Cart mcart = cartArrayList.get(i);
            String itemName = mcart.getName();
            databaseHelper.removeItem(id, itemName, getApplicationContext());

        }
    }

    private class PlaceOrderAsyncTask extends AsyncTask<String, Void, String> {
        String result = "";
        final ProgressDialog progressDialog = new ProgressDialog(PaymentPage.this, R.style.AppTheme_Dark_Dialog);

        @Override
        protected  void onPreExecute()
        {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("processing your Order...please wait!!!");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String ... params){
            HttpURLConnection connection = null;
            OutputStream os = null;
            BufferedReader reader=null;
            BufferedWriter writer = null;
            StringBuilder sb = null;
            String line = null;

            try{

                String data = URLEncoder.encode("customer_id", "UTF-8")
                        + "=" + URLEncoder.encode(id, "UTF-8");

                data += "&" + URLEncoder.encode("postal_code", "UTF-8")
                        + "=" + URLEncoder.encode(tab_pincode, "UTF-8");

                data += "&" + URLEncoder.encode("address", "UTF-8")
                        + "=" + URLEncoder.encode(tab_address, "UTF-8");

                data += "&" + URLEncoder.encode("landmark", "UTF-8")
                        + "=" + URLEncoder.encode(tab_landmark, "UTF-8");

                data += "&" + URLEncoder.encode("city", "UTF-8")
                        + "=" + URLEncoder.encode(tab_city, "UTF-8");

                data += "&" + URLEncoder.encode("state", "UTF-8")
                        + "=" + URLEncoder.encode(tab_state, "UTF-8");

                data += "&" + URLEncoder.encode("item_details", "UTF-8")
                        + "=" + URLEncoder.encode(params[0], "UTF-8");

                URL url = new URL(ORDER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setDoInput(true);
                connection.setDoOutput(true);


                os = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line + "\n");
                }

                result = sb.toString();

            }catch (MalformedURLException m){
                m.printStackTrace();
            }catch (IOException io){
                io.printStackTrace();
            }
            finally
            {
                //close the connection, set all objects to null
                connection.disconnect();
                os = null;
                reader=null;
                writer = null;
                sb = null;
                line = null;
                connection = null;
            }

            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("response");
                if(response.equals("error")){
                    //redirect user to login page
                    progressDialog.dismiss();
                    Toast.makeText(PaymentPage.this, "Sorry we couldn't complete the order ... please try again !!!", Toast.LENGTH_LONG).show();
                }else{
                    progressDialog.dismiss();
                    removeCartItems ();
                    Intent i = new Intent(PaymentPage.this,OrderPlaced.class);
                    startActivity(i);
                    finish();
                }
            }catch (JSONException jsonException){
                jsonException.printStackTrace();
            }
        }
    }
}
