package com.crowdedgeek.fontboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] kawais;
    LayoutInflater inflter;
    public CustomAdapter(Context applicationContext, String[] kawais) {
        this.context = applicationContext;
        this.kawais = kawais;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return kawais.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.kawai_item, null); // inflate the layout
        final TextView kw = view.findViewById(R.id.textKawai);
        kw.setText(kawais[i]);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("styletext", kawais[i]);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
        kw.setSelected(true);
        return view;
    }
}
