package com.example.plantdiseasedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.skydoves.expandablelayout.ExpandableLayout;

public class ActivitySearchResult extends AppCompatActivity {

    ImageView imageView;
    TextView name;
    MaterialToolbar toolbar;

    TextView cause,solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        setupToolbar();

        String[] solutionArray = getResources().getStringArray(R.array.plant_disease_solutions);
        String[] causesArray = getResources().getStringArray(R.array.plant_causes);
        String[] classes = getResources().getStringArray(R.array.plant_classes);

        imageView = findViewById(R.id.imageView_sres);
        name = findViewById(R.id.name_sres);
        cause = findViewById(R.id.cause_sres);
        solution = findViewById(R.id.solution_sres);

        Intent intent = getIntent();
        int pos=intent.getIntExtra("disease",0);

        name.setText(classes[pos]);
        solution.setText(solutionArray[pos]);
        cause.setText(causesArray[pos]);

    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
            toolbar = findViewById(R.id.toolbar_sres);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}