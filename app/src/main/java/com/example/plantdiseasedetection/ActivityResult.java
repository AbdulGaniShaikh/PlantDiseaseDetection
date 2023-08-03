package com.example.plantdiseasedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.skydoves.expandablelayout.ExpandableLayout;

public class ActivityResult extends AppCompatActivity {

    ImageView imageView;
    TextView result;
    MaterialToolbar toolbar;

    ExpandableLayout expandableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setupToolbar();

        String[] solution = getResources().getStringArray(R.array.plant_disease_solutions);
        String[] classes = getResources().getStringArray(R.array.plant_classes);
//        String[] classes = {
//                "apple scab"
//                ,"apple black rot"
//                ,"apple cedar apple rust"
//                ,"apple healthy"
//                ,"blueberry healthy"
//                ,"cherry including sour powdery mildew"
//                ,"cherry including sour healthy"
//                ,"corn maize cercospora gray leaf spot"
//                ,"corn maize common rust"
//                ,"corn maize northern leaf blight"
//                ,"corn maize healthy"
//                ,"grape black rot"
//                ,"grape esca black measles"
//                ,"grape leaf blight isariopsis leaf spot"
//                ,"grape healthy"
//                ,"orange haunglongbing (citrus greening)"
//                ,"peach bacterial spot"
//                ,"peach healthy"
//                ,"pepper bell bacterial spot"
//                ,"pepper bell healthy"
//                ,"potato early blight"
//                ,"potato late blight"
//                ,"potato healthy"
//                ,"raspberry healthy"
//                ,"soybean healthy"
//                ,"squash powdery mildew"
//                ,"strawberry leaf scorch"
//                ,"strawberry healthy"
//                ,"tomato bacterial spot"
//                ,"tomato early blight"
//                ,"tomato late blight"
//                ,"tomato leaf mold"
//                ,"tomato septoria leaf spot"
//                ,"tomato spider mites two spotted spider mite"
//                ,"tomato target spot"
//                ,"tomato tomato yellow leaf curl virus"
//                ,"tomato tomato mosaic virus"
//                ,"tomato healthy"
//        };

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        expandableLayout = findViewById(R.id.result_exp);

        expandableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandableLayout.isExpanded()) expandableLayout.collapse();
                else  expandableLayout.expand();
            }
        });

        Intent intent = getIntent();

        Bitmap image = (Bitmap) intent.getParcelableExtra("image");
        imageView.setImageBitmap(image);
        int maxCon = (int)(intent.getFloatExtra("perc",51)*100.0f);
        TextView tv = expandableLayout.getSecondLayout().findViewById(R.id.disease_detail);
        if (maxCon>50) {
            result.setText(classes[intent.getIntExtra("res", 0)]);
            tv.setText(solution[intent.getIntExtra("res", 0)]);
        }

    }

    private void setupToolbar(){
        if (getSupportActionBar()==null){
            toolbar = findViewById(R.id.toolbar_login);
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