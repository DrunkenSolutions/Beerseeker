package com.dmytryk.beerseeker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.dmytryk.beerseeker.data.Sale;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Sale> listOfSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        listOfSales = getIntent().getParcelableArrayListExtra("sales");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


   private void placeSales(){
       if (mMap != null){
           for (Sale sale: listOfSales ) {
               LatLng saleLatLng;
               if (sale.getLatitude() > 0.0){
                   saleLatLng = new LatLng(sale.getLatitude(), sale.getLongitude());
               } else continue;

               int saleInfo;
               try {
                   saleInfo = Integer.parseInt(sale.getInfo());
               }catch (NumberFormatException ex){
                   ex.printStackTrace();
                   continue;
               }

               switch (saleInfo){
                   case 1: mMap.addMarker(new MarkerOptions()
                           .icon(BitmapDescriptorFactory.fromResource(R.drawable.beer_bottle_42))
                           .title(sale.getTitle())
                           .flat(true)
                           .position(saleLatLng));
                       break;
                   case 0: mMap.addMarker(new MarkerOptions()
                           .icon(BitmapDescriptorFactory.fromResource(R.drawable.beer_42))
                           .title(sale.getTitle())
                           .position(saleLatLng));
                       break;
               }
           }
       }
   }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException secEx){
            Toast.makeText(this, "Permission was canceled or denied", Toast.LENGTH_LONG);
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.8275, 24.041484), 16));


        placeSales();
    }
}
