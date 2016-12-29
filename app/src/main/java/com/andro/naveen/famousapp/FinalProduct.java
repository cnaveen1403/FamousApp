package com.andro.naveen.famousapp;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.naveen.famousapp.database.DatabaseHelper;
import com.andro.naveen.famousapp.myapplication.MyApplication;
import com.andro.naveen.famousapp.parsing.Cart;
import com.squareup.picasso.Picasso;

public class FinalProduct extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener {

    String title, img, price, txtIngredients;
    Spinner spinner;
    private NestedScrollView mNestedScrollView;
    private ImageView mImageView;
    private View mImageContainer;
    private TextView tvPrice, tvIngredients;
    private View mToolbarContainer;
    private LinearLayout mContentLayout;
    MyApplication myApplication;
    DatabaseHelper mdatabaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_product);


        //geting Intent extra
        title = getIntent().getStringExtra("Title");
        img = getIntent().getStringExtra("Image");
        price = getIntent().getStringExtra("Price");
        txtIngredients = getIntent().getStringExtra("Ingredients");


        //VIEW matching
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        mImageView = (ImageView) findViewById(R.id.imageViewFinal);
        mImageContainer = findViewById(R.id.image_container);
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mToolbarContainer = findViewById(R.id.toolbar_container);
        tvIngredients = (TextView) findViewById(R.id.textViewIngredients);
        tvPrice = (TextView) findViewById(R.id.tv_price);

        spinner = (Spinner)findViewById(R.id.spinner_quantity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.quantity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        /*noPicker = (NumberPicker) findViewById(R.id.pickNumber);
        noPicker.setMinValue(1);
        noPicker.setMaxValue(10);
        noPicker.setWrapSelectorWheel(true);*/


        Picasso.with(getApplicationContext())
                .load(img)
                .into(mImageView);

        tvPrice.setText(price);
        tvIngredients.setText(txtIngredients);

        // setup toolbar
        setupToolbar();

        // setup nested scroll view
        setupNestedScrollView();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNestedScrollView() {
        mNestedScrollView.setOnScrollChangeListener(this);

        ViewTreeObserver viewTreeObserver = mNestedScrollView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // get size
                    int toolbarLinearLayoutHeight = mToolbarContainer.getHeight();
                    int imageHeight = mImageView.getHeight();

                    // adjust image frame layout height
                    ViewGroup.LayoutParams layoutParams = mImageContainer.getLayoutParams();
                    if (layoutParams.height != imageHeight) {
                        layoutParams.height = imageHeight;
                        mImageContainer.setLayoutParams(layoutParams);
                    }

                    // adjust top margin of content linear layout
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mContentLayout.getLayoutParams();
                    if (marginLayoutParams.topMargin != toolbarLinearLayoutHeight + imageHeight) {
                        marginLayoutParams.topMargin = toolbarLinearLayoutHeight + imageHeight;
                        mContentLayout.setLayoutParams(marginLayoutParams);
                    }

                    // call onScrollChange to update initial properties.
                    onScrollChange(mNestedScrollView, 0, 0, 0, 0);
                }
            });
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        // choose appropriate y
        float newY = Math.max(mImageView.getHeight(), scrollY);

        // translate image and toolbar
        ViewCompat.setTranslationY(mToolbarContainer, newY);
        ViewCompat.setTranslationY(mImageContainer, scrollY * 0.5f);
    }

    public void showDialog(View v) {

        //Insert the product details selected into table Orders
        myApplication = (MyApplication) getApplication();
        String id = myApplication.getSavedValue("Id");

        String quantity = spinner.getSelectedItem().toString();

        boolean state = false;
        state = mdatabaseHelper.removeDuplicate(id, title, quantity,getApplicationContext());
        if (!state) {
            Cart cart = new Cart();
            cart.setId(id);
            cart.setUrl(img);
            cart.setName(title);
            cart.setPrice(price);
            cart.setQuantity(quantity);
            mdatabaseHelper.insertDetailsToCart(cart);
        }

        Toast.makeText(getApplicationContext(), "The item has been added to your cart", Toast.LENGTH_LONG).show();
        Intent i = new Intent(FinalProduct.this, MainPageActivity.class);
        startActivity(i);
        finish();
    }

    public void buyNowClicked (View v){
        myApplication = (MyApplication) getApplication();
        String id = myApplication.getSavedValue("Id");

        String quantity = spinner.getSelectedItem().toString();

        boolean state = false;
        state = mdatabaseHelper.removeDuplicate(id, title, quantity,getApplicationContext());
        if (!state) {
            Cart cart = new Cart();
            cart.setId(id);
            cart.setUrl(img);
            cart.setName(title);
            cart.setPrice(price);
            cart.setQuantity(quantity);
            mdatabaseHelper.insertDetailsToCart(cart);
        }

        Toast.makeText(getApplicationContext(), "The item has been added to your cart", Toast.LENGTH_LONG).show();
        Intent i = new Intent(FinalProduct.this, ActivityCart.class);
        startActivity(i);
        finish();
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

        if (id == R.id.home) {
            Intent i = new Intent(FinalProduct.this, MainPageActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
