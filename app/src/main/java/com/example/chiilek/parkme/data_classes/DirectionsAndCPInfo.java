package com.example.chiilek.parkme.data_classes;

import android.util.Log;

import com.example.chiilek.parkme.data_classes.availability_classes.CarParkDatum;
import com.example.chiilek.parkme.data_classes.directions_classes.GoogleMapsDirections;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;

public class DirectionsAndCPInfo implements Serializable{
    private CarParkStaticInfo carParkStaticInfo;
    private GoogleMapsDirections googleMapsDirections;
    private CarParkDatum carParkDatum;
    private double distance;
    private int duration;
    private int availability = -1;
    private double distanceScore = 0;
    private double durationScore = 0;
    private double availabilityScore = 0;
    private double distanceScoreWeight = 0.25;
    private double durationScoreWeight = 0.25;
    private double availabilityScoreWeight = 0.50;

    public DirectionsAndCPInfo(CarParkStaticInfo cpInfo, GoogleMapsDirections gmapsDir, LatLng userChosenDestination){
        this.carParkStaticInfo = cpInfo;
        this.googleMapsDirections = gmapsDir;
        Log.d("DirectionsAndCPInfo", "gmapsDir status "+ gmapsDir.getStatus() + ", gmapsDir routes size " + gmapsDir.getRoutes().size());
//        distance = getDistanceFromLatLngInM(userChosenDestination.latitude,
//                userChosenDestination.longitude,
//                Double.parseDouble(cpInfo.getLatitude()),
//                Double.parseDouble(cpInfo.getLongitude()));
        distance = SphericalUtil.computeDistanceBetween(userChosenDestination,cpInfo.getLatLng());
        duration = gmapsDir.getRoutes().get(0).getLegs().get(0).getDuration().getValue();
    }


    //TODO: remove method after confirming that no issues
    private double getDistanceFromLatLngInM(double lat1, double lng1, double lat2, double lng2) {
        double radiusAtEquator = 6378000; //in meters
        double dLat = degToRad(lat2-lat1);  // deg2rad below
        double dLng = degToRad(lng2-lng1);

        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) *
                                Math.sin(dLng/2) * Math.sin(dLng/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = radiusAtEquator * c; // Distance in km
        return distance;
    }

    //TODO: remove too
    private double degToRad(double deg) {
        return deg * (Math.PI/180);
    }

    public double getOverallScore(){
        return distanceScoreWeight * distanceScore +
                durationScoreWeight * durationScore +
                availabilityScoreWeight * availabilityScore;
    }

    public LatLng getDestinationLatLng(){
        return carParkStaticInfo.getLatLng();
    }

    public void setCarParkDatum(CarParkDatum carParkDatum) {
        this.carParkDatum = carParkDatum;
        this.availability = carParkDatum.getCarParkInfo().get(0).getLotsAvailable();
    }

    public CarParkStaticInfo getCarParkStaticInfo() { return carParkStaticInfo; }

    public GoogleMapsDirections getGoogleMapsDirections() { return googleMapsDirections; }

    public CarParkDatum getCarParkDatum() { return carParkDatum; }

    public double getDistance(){ return distance; }

    public int getDuration(){ return duration; }

    public int getAvailability() { return availability; }

    public void setDistanceScore(double distanceScore) { this.distanceScore = distanceScore; }

    public void setDurationScore(double durationScore) { this.durationScore = durationScore; }

    public void setAvailabilityScore(double availabilityScore) { this.availabilityScore = availabilityScore; }
}
