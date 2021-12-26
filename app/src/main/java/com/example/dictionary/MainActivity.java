package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.appsearch.GetByDocumentIdRequest;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionary.Adapters.MeaningsAdapter;
import com.example.dictionary.Adapters.PhoneticsAdapters;
import com.example.dictionary.Models.APIrespone;

public class MainActivity extends AppCompatActivity {

    SearchView Search_View;
    TextView textView_word;
    RecyclerView recycler_phonetics, recycler_meanings;
    ProgressDialog progressDialog;
    PhoneticsAdapters phoneticsAdapters;
    MeaningsAdapter meaningsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Search_View = findViewById(R.id.Search_View);
        textView_word = findViewById(R.id.textView_word);
        recycler_phonetics = findViewById(R.id.recycler_phonetics);
        recycler_meanings = findViewById(R.id.recycler_meanings);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Welcome to My Dictionary!!!");
        progressDialog.show();
        RequestManager manager = new RequestManager(MainActivity.this);
        manager.getWordMeaning(listener, "Coding");

        Search_View.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Fetching response for " + query);
                progressDialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getWordMeaning(listener, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private final OnFetchDataListener listener = new OnFetchDataListener() {
        @Override
        public void onFetchData(APIrespone apiRespone, String message) {
            progressDialog.dismiss();
            if(apiRespone==null)
            {
                Toast.makeText(MainActivity.this,"No data found!!!",Toast.LENGTH_SHORT).show();
                return;
            }
            showData(apiRespone);
        }

        @Override
        public void OnError(String message) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
        }
    };
    private void showData(APIrespone apiRespone) {
        textView_word.setText("Word: " + apiRespone.getWord());
        recycler_phonetics.setHasFixedSize(true);
        recycler_phonetics.setLayoutManager(new GridLayoutManager(this,1));
        phoneticsAdapters = new PhoneticsAdapters(this,apiRespone.getPhonetics());
        recycler_phonetics.setAdapter(phoneticsAdapters);

        recycler_meanings.setHasFixedSize(true);
        recycler_meanings.setLayoutManager(new GridLayoutManager(this,1));
        meaningsAdapter = new MeaningsAdapter(this,apiRespone.getMeanings());
        recycler_meanings.setAdapter(meaningsAdapter);
    }

}