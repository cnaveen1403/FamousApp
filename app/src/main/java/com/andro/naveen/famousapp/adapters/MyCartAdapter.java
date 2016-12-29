package com.andro.naveen.famousapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.naveen.famousapp.EmptyCart;
import com.andro.naveen.famousapp.R;
import com.andro.naveen.famousapp.database.DatabaseHelper;
import com.andro.naveen.famousapp.myapplication.MyApplication;
import com.andro.naveen.famousapp.parsing.Cart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 5/19/2016.
 */
public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.CustomViewHolder> {

    ArrayList<Cart> list;
    Context mContext;
    String id;
    DatabaseHelper mdatabaseHelper;
    MyCartAdapter myCartAdapter;

    public MyCartAdapter(Context context, ArrayList<Cart> list, String id) {

        this.list = list;
        this.mContext = context;
        this.id = id;

    }

    @Override
    public MyCartAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_cart, parent, false);
        CustomViewHolder cvh = new CustomViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(MyCartAdapter.CustomViewHolder holder, int position) {

        final Cart mCart = list.get(position);

        Picasso.with(mContext).load(mCart.getUrl()).into(holder.imageView);
        holder.txtName.setText(mCart.getName());
        holder.txtPrice.setText(mCart.getPrice());
        holder.txtQuantity.setText(mCart.getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName,txtPrice, txtQuantity;
        public ImageView imageView, delete_icon;


        CustomViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ImageViewCart);
            txtName = (TextView) itemView.findViewById(R.id.TextViewCart);
            txtPrice = (TextView) itemView.findViewById(R.id.TextViewPrice);
            delete_icon = (ImageView) itemView.findViewById(R.id.delete_icon);
            txtQuantity = (TextView) itemView.findViewById(R.id.TextViewQuantity);

            delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mdatabaseHelper = new DatabaseHelper(v.getContext());
                    Cart mCart = list.get(getLayoutPosition());
                    String name = mCart.getName();
                    mdatabaseHelper.removeItem(id, name, v.getContext());
                    int size = getAdapterPosition();
                    removeItem(size);
                   /* if (list.size() == 0) {
                        Intent i = new Intent(v.getContext(), EmptyCart.class);
                        startActivity(i);
                    }*/
                }
            });
        }
    }

    public void removeItem(int position){
        this.list.remove(position);
        notifyDataSetChanged();
    }
}
