package com.example.chiilek.parkme.ViewMap;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.example.chiilek.parkme.Location;
import com.example.chiilek.parkme.data_classes.CarParkDatum;
import com.example.chiilek.parkme.repository.Repository;

import java.util.List;

public class ViewMapViewModel extends AndroidViewModel{
    private MutableLiveData<Location> msearchTerm;
    //list of nearest carparks to search term
    private LiveData<List<CarParkDatum>> mcarParkList;
    private Location currentLocation;
    private Repository mRepository;

    public ViewMapViewModel(Application application){
        super(application);
        this.mRepository = Repository.getInstance(this.getApplication());
        msearchTerm = new MutableLiveData<>();
        mcarParkList = mRepository.searchNearby(currentLocation);

        //searches nearby everytime msearchterm changes, when called by VMMP.setSearchTerm()
        mcarParkList = Transformations.switchMap(msearchTerm, (Location newDestination)->
                mRepository.searchNearby(newDestination));
    }
    //called by button in ViewMapActivity and triggers transformation
    public void setSearchTerm(Location searchTerm){
        msearchTerm.setValue(searchTerm);
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    /*
        // put this in the ViewMapActivity GMAP fragment with the search button to update searchTerm

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        searchButton.setOnClickListener(item -> {
        model.setSearchTerm(item);
        //create new SelectRouteActivity
        }
     */

    //displays popup carpark info
    //TODO find out what google maps returns when you press on a pin
    public CarParkDatum getCarParkInfo(String toFind){
        if (mcarParkList != null){
            for ( CarParkDatum carpark: mcarParkList.getValue()){
                if (carpark.getCarParkNumber().equals(toFind))
                    return carpark;
            }
        }
        return null;
    }

    public LiveData<Location> getSearchTerm() {
        return msearchTerm;
    }

    public LiveData<List<CarParkDatum>> getCarParkList() {
        return mcarParkList;
    }


}

