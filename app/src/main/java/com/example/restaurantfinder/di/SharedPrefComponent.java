package com.example.restaurantfinder.di;

import com.example.restaurantfinder.MainActivity;
import com.example.restaurantfinder.SelectCityActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SharedPrefModule.class})
public interface SharedPrefComponent {
    void inject(MainActivity mainActivity);

    void inject(SelectCityActivity selectCityActivity);
}
