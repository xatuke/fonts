package com.crowdedgeek.fontboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.crowdedgeek.fontboard.utils.QRGenerator;

public class QRActivity extends AppCompatActivity {

    EditText input;
    ImageView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        getSupportActionBar().hide();
        input  = findViewById(R.id.input);
        output = findViewById(R.id.outputImg);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!input.getText().toString().equals("")) {
                    output.setVisibility(View.VISIBLE);
                    QRGenerator gen = new QRGenerator();
                    output.setImageBitmap(gen.makeQR(input.getText().toString()));
                } else {
                    output.setVisibility(View.GONE);
                }
            }
        });
    }
}
