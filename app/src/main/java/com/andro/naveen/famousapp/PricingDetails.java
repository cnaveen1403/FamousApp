package com.andro.naveen.famousapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andro.naveen.famousapp.database.DatabaseHelper;
import com.andro.naveen.famousapp.myapplication.MyApplication;

import java.text.DecimalFormat;

public class PricingDetails extends AppCompatActivity {

    int cartTotal,delivery, list;
    double vat,totalPay;
    String v,p;
    int sumTotal;
    TextView textViewCart,textViewVat,textViewdelivery,textViewfinal,textViewitems,textViewtotal;

    MyApplication myApplication;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing_details);

        cartTotal = getIntent().getIntExtra("CartTotal", 0);
        vat = getIntent().getDoubleExtra("Vat", 0);
        delivery = getIntent().getIntExtra("Delivery", 0);
        totalPay = getIntent().getDoubleExtra("Total", 0);
        list = getIntent().getIntExtra("Items", 0);
        init();
        DecimalFormat df=new DecimalFormat("0");
        v = df.format(vat);
        p = df.format(totalPay);
        sumTotal = Integer.parseInt(p);
        //if total is > 1000, delivery = Free
        if (cartTotal>1000){

            textViewdelivery.setText("FREE");
            textViewdelivery.setTextColor(Color.parseColor("#4CAF50"));
            sumTotal = sumTotal - delivery;
        }
        else{

            textViewdelivery.setText("Rs: "+delivery);
        }
        setPrice();
        myApplication = (MyApplication) getApplication();
        myApplication.saveToken("Total",String.valueOf(sumTotal));
    }

    private void setPrice() {

        textViewitems.setText("Items (" + list + ")");
        textViewtotal.setText("Amount : Rs " + sumTotal);
        textViewCart.setText("Rs: " + cartTotal);
        textViewVat.setText("Rs: "+v);
        textViewfinal.setText("Rs: " + sumTotal);
    }

    private void init() {

        textViewitems = (TextView) findViewById(R.id.itemCount);
        textViewtotal = (TextView) findViewById(R.id.totalDisplay);
        textViewCart = (TextView) findViewById(R.id.cartPriceTotal);
        textViewVat = (TextView) findViewById(R.id.vatPriceTotal);
        textViewdelivery = (TextView) findViewById(R.id.deliveryPriceTotal);
        textViewfinal = (TextView) findViewById(R.id.finalPriceTotal);

        myApplication = (MyApplication) getApplication();
        id = myApplication.getSavedValue("Id");
    }

    public void checkout(View v){

        String addressStatus = databaseHelper.searchAddress(id);

        if (addressStatus.equals("Not Found")){
            Intent intent = new Intent(PricingDetails.this,ShippingDetails.class);
            intent.putExtra("Status","New");
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(PricingDetails.this,PaymentPage.class);
            startActivity(intent);
        }
    }
}
