package com.example.user.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivityBase extends AppCompatActivity {

    private DataController mDataController;
    private DataController.DataChangeObserver mDataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText key = (EditText) findViewById(R.id.key);
        final EditText value = (EditText) findViewById(R.id.value);
        final View add = findViewById(R.id.add);
        final View delete = findViewById(R.id.delete);
        final View update = findViewById(R.id.update);
        final TextView values = (TextView) findViewById(R.id.values);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataController.add(key.getText().toString(), value.getText().toString());
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataController.delete(key.getText().toString());
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataController.update(key.getText().toString(), value.getText().toString());
            }
        });

        mDataController = DataController.getInstance(this);
        mDataObserver = new DataController.DataChangeObserver() {
            @Override
            public void onChanged(String newList) {
                values.setText(newList);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataController.fetchList();
        mDataController.addObserver(mDataObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDataController.removeObserver(mDataObserver);
    }
}
