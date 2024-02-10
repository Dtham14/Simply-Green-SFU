package com.example.simplygreensfuapp;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;


public class Waypoint {
    //Private variables
    private Marker marker = null;
    //type is used to filter the markers
    private String type = null;

    public Waypoint(Marker m, String t){
        this.marker = m;
        this.type = t;
    }

    /**
     * function to get the waypoint type
     * @return the waypoint type
     */
    public String getType(){
        return this.type;
    }

    /**
     * function to get the waypoint marker
     * @return the waypoint marker
     */
    public Marker getMarker(){
        return this.marker;
    }

    public GeoPoint getGeoPoint(){
        return this.marker.getPosition();
    }

}
