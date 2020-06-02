package com.example.restaurantfinder.di;

import com.example.restaurantfinder.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SharedPrefModule.class})
public interface SharedPrefComponent {
    void inject(MainActivity mainActivity);
}
