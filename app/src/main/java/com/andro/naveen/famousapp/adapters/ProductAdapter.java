package com.andro.naveen.famousapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andro.naveen.famousapp.R;
import com.andro.naveen.famousapp.parsing.ParProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 5/20/2016.
 */
public class ProductAdapter extends ArrayAdapter<ParProduct> {


    Context mcontext;
    ArrayList<ParProduct> list;
    LayoutInflater mlayoutInflater;
    int resource;


    public ProductAdapter(Context context, int resource, ArrayList<ParProduct> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.resource = resource;
        this.mcontext = context;
        mlayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ParProduct mProduct = getItem(position);
        ViewHolder mViewHolder;

        if (convertView == null) {
            convertView= mlayoutInflater.inflate(resource, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mcontext).load(mProduct.getPhoto()).into(mViewHolder.imageView);
        mViewHolder.txtTitle.setText(mProduct.getCategory());
        mViewHolder.txtItems.setText(mProduct.getItems());
        mViewHolder.txtIngredients.setText(mProduct.getIngredients());

        return convertView;
    }


    private static class ViewHolder {
        public TextView txtTitle,txtItems,txtIngredients;
        public ImageView imageView;

        public ViewHolder(View view) {

            imageView = (ImageView) view.findViewById(R.id.profileImageView);
            txtTitle = (TextView) view.findViewById(R.id.titleTextView);
            txtItems = (TextView) view.findViewById(R.id.itemsTextView);
            txtIngredients = (TextView) view.findViewById(R.id.ingredientsTextView);
        }
    }
}
