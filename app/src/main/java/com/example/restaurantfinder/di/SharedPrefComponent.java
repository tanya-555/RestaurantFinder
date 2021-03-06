package com.example.restaurantfinder.di;

import com.example.restaurantfinder.DetailsActivity;
import com.example.restaurantfinder.LandingActivity;
import com.example.restaurantfinder.ListingActivity;
import com.example.restaurantfinder.MainActivity;
import com.example.restaurantfinder.SelectCityActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {SharedPrefModule.class})
public interface SharedPrefComponent {
    void inject(MainActivity mainActivity);

    void inject(SelectCityActivity selectCityActivity);

    void inject(LandingActivity landingActivity);

    void inject(ListingActivity listingActivity);

    void inject(DetailsActivity detailsActivity);
}
