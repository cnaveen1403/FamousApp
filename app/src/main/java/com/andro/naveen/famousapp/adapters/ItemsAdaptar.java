package com.andro.naveen.famousapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andro.naveen.famousapp.R;
import com.andro.naveen.famousapp.parsing.Grocery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 5/3/2016.
 */
public class ItemsAdaptar extends ArrayAdapter<Grocery> {

    Context mcontext;
    ArrayList<Grocery> list;
    LayoutInflater mlayoutInflater;
    int resource;


    public ItemsAdaptar(Context context, int resource, ArrayList<Grocery> objects) {
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

        Grocery mGrocery = getItem(position);
        ViewHolder mViewHolder;

        if (convertView == null) {
            convertView= mlayoutInflater.inflate(resource, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mcontext)
                .load(mGrocery.getPhoto())
                .into(mViewHolder.imageView);
        mViewHolder.txtTitle.setText(mGrocery.getCategory());
        mViewHolder.txtItems.setText(mGrocery.getItems());

        return convertView;
    }



    private static class ViewHolder {
        public TextView txtTitle;
        public TextView txtItems;
        public ImageView imageView;

        public ViewHolder(View view) {

            imageView = (ImageView) view.findViewById(R.id.profileImageView);
            txtTitle = (TextView) view.findViewById(R.id.titleTextView);
            txtItems = (TextView) view.findViewById(R.id.itemsTextView);
        }
    }
}
