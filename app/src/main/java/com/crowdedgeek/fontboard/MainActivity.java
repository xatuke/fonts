package com.crowdedgeek.fontboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.crowdedgeek.fontboard.Styler.DecorateTool;
import com.crowdedgeek.fontboard.Styler.StylistGenerator;
import com.crowdedgeek.fontboard.utils.BottomSheetGridView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import static com.crowdedgeek.fontboard.Constants.kawais;

public class MainActivity extends AppCompatActivity {
    Switch decSwitch;
    EditText input;
    ListView listView;
    ImageView asciiGen, qrGen;
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
        final CardView llBottomSheet = findViewById(R.id.bottom_sheet);

        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setHideable(false);
        BottomSheetGridView v = findViewById(R.id.simpleGridView);
        CustomAdapter adapter = new CustomAdapter(MainActivity.this, kawais);
        v.setAdapter(adapter);
        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClipboardManager clipboard = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("styletext", kawais[i]);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
        asciiGen = findViewById(R.id.asiicImgGen);
        qrGen = findViewById(R.id.qrCodeGen);
        asciiGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ASCIIActivity.class));
            }
        });
        qrGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QRActivity.class));
            }
        });

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
