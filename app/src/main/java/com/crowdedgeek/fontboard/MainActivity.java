package com.crowdedgeek.fontboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import com.crowdedgeek.fontboard.Styler.DecorateTool;
import com.crowdedgeek.fontboard.Styler.StylistGenerator;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import static com.crowdedgeek.fontboard.Constants.kawais;

public class MainActivity extends AppCompatActivity {
    Switch decSwitch;
    EditText input;
    ListView listView;
    boolean decors;
    ArrayList<String> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        listView = findViewById(R.id.myTextList);
        input = findViewById(R.id.input);
        decSwitch = findViewById(R.id.decSwitch);
        decSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    decors = true;
                    makeMyListGoUp();
                } else {
                    decors = false;
                    makeMyListGoUp();
                }
            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                makeMyListGoUp();
            }
        });
        CardView llBottomSheet = findViewById(R.id.bottom_sheet);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setHideable(false);

        GridView v = findViewById(R.id.simpleGridView);
        CustomAdapter adapter = new CustomAdapter(MainActivity.this, kawais);
        v.setAdapter(adapter);

    }

    private void makeMyListGoUp(){
        if(!decors) {
            dataList = new ArrayList<>();
            StylistGenerator gen = new StylistGenerator();
            dataList = gen.generate(input.getText().toString());
            dataList.add("");
            MyListAdapter adapter = new MyListAdapter(MainActivity.this, dataList);
            listView.setAdapter(adapter);
        } else {
            dataList = new ArrayList<>();
            DecorateTool s = new DecorateTool();
            dataList = s.generate(input.getText().toString());
            dataList.add("");
            MyListAdapter adapter = new MyListAdapter(MainActivity.this, dataList);
            listView.setAdapter(adapter);
        }
    }
}
