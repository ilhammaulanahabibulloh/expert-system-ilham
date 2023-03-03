package com.e_clinicmelati;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.menu_list, itemname);

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.menu_list, null, true);

        TextView txtTitle = rowView.findViewById(R.id.menu_text);
        ImageView imageView = rowView.findViewById(R.id.menu_icon);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    }

    ;
}
