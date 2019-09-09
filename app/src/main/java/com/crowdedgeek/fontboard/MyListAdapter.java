package com.crowdedgeek.fontboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import static android.content.Intent.ACTION_SEND;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> myStringList = new ArrayList<>();

    public MyListAdapter(Activity context, ArrayList<String> myStringList) {
        super(context, R.layout.style_list_item, myStringList);

        this.context=context;
        this.myStringList = myStringList;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.style_list_item, null,true);
        TextView text = rowView.findViewById(R.id.myText);
        text.setSelected(true);
        text.setText(myStringList.get(position));
        ImageView copy = rowView.findViewById(R.id.copy);
        ImageView share = rowView.findViewById(R.id.share);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("styletext", myStringList.get(position));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inttt = new Intent(ACTION_SEND);
                inttt.setType("text/plain");
                inttt.putExtra(Intent.EXTRA_TEXT, myStringList.get(position));
                context.startActivity(Intent.createChooser(inttt, context.getResources().getText(R.string.app_name)));
            }
        });

        return rowView;

    };
}
