package com.example.plantdiseasedetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivitySearch extends AppCompatActivity {

    MaterialToolbar toolbar;
    RecyclerView recyclerView;
    List<String> list,filterList;
    EditText search;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.searchmain);
        setupToolbar();
        setupRecyclerView();
        String[] classes = getResources().getStringArray(R.array.plant_classes);
        list.addAll(Arrays.asList(classes));
        adapter.notifyDataSetChanged();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    private void setupToolbar(){
        if (getSupportActionBar()==null){
            toolbar = findViewById(R.id.toolbar_search);
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

    private void setupRecyclerView() {
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new MyAdapter(ActivitySearch.this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivitySearch.this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getApplicationContext(),ActivitySearchResult.class);
                intent.putExtra("disease",position);
                startActivity(intent);
            }
        });
    }


}