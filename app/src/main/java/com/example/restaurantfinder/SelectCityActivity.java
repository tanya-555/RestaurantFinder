package com.example.restaurantfinder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.restaurantfinder.databinding.SelectCityLayoutBinding;

public class SelectCityActivity extends AppCompatActivity {

    private SelectCityLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.select_city_layout,null, false);
        setContentView(binding.getRoot());
    }
}
