package com.example.movies.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.movies.Adapter.FilmsAdapter;
import com.example.movies.R;
import com.example.movies.Utilities.Utils;
import com.example.movies.ViewModel.FilmViewModel;
import com.example.movies.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

        private ActivityMainBinding mainBinding;
        private final Utils utils = new Utils();
        private FilmsAdapter filmsAdapter;
        private FilmViewModel filmViewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
                filmViewModel = ViewModelProviders.of(this).get(FilmViewModel.class);

                if (utils.isNetworkAvailable(this))
                        Toast.makeText(this, "Poor internet connection", Toast.LENGTH_SHORT).show();
                mainBinding.progressBar.setVisibility(View.VISIBLE);
                filmViewModel.init();
                filmViewModel.getFilmsLiveData().observe(this, movies -> {
                        initRecyclerView();
                        filmsAdapter.notifyDataSetChanged();
                        mainBinding.progressBar.setVisibility(View.GONE);
                });

                mainBinding.fabFav.setOnClickListener(v -> startActivity(new Intent(this, FavouriteMovies.class)));

                initSearchView();
        }

        private void initSearchView() {
                mainBinding.movieSearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                                //filmsAdapter.getFilter().filter(query);
                                return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                                return false;
                        }
                });
        }

        private void initRecyclerView() {
                filmsAdapter = new FilmsAdapter(Objects.requireNonNull(filmViewModel.getFilmsLiveData().getValue()), this);
                int orientation = this.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        mainBinding.movRecycler.setLayoutManager(new GridLayoutManager(this, 4));
                } else {
                        mainBinding.movRecycler.setLayoutManager(new GridLayoutManager(this, 2));
                }
                mainBinding.movRecycler.setAdapter(filmsAdapter);
        }
}