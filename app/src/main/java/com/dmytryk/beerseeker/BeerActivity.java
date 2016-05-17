package com.dmytryk.beerseeker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dmytryk.beerseeker.data.Sale;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeerActivity extends AppCompatActivity {

    private Socket client;
    private PrintWriter printwriter;
    private String messsage = "";

    ArrayList<Sale> listOfSales = new ArrayList<>();

    LinearLayout llSales;


    private void init(){
        if (listOfSales.size() == 0) {
            initTestSales();
            displaySales();
        }
    }

    private void initTestSales(){


        listOfSales.add(new Sale.Builder("Магазин Добра лавка")
                .address("Вул. Льва Толстого, 7")
                .info(Sale.SALE_SHOP)
                .coords(new Pair<Double, Double>(49.829136, 24.038508))
                .build());

        listOfSales.add(new Sale.Builder("Продукти")
                .address("Вул. Архипенка, 32")
                .info(Sale.SALE_SHOP)
                .coords(new Pair<Double, Double>(49.829777, 24.038085))
                .build());

        listOfSales.add(new Sale.Builder("Школа №28")
                .address("Вул. Тютюнників, 2")
                .info(Sale.SALE_PUB)
                .coords(new Pair<Double, Double>(49.830014, 24.036478))
                .build());


        listOfSales.add(new Sale.Builder("Два кроки")
                .address("Вул. Генерала Тарнавського, 104")
                .info(Sale.SALE_SHOP)
                .coords(new Pair<Double, Double>(49.826879, 24.042036))
                .build());

        listOfSales.add(new Sale.Builder("Продукти")
                .address("Вул. Генерала Тарнавського, 109")
                .info(Sale.SALE_SHOP)
                .coords(new Pair<Double, Double>(49.826569, 24.043309))
                .build());

        listOfSales.add(new Sale.Builder("Два кроки (Пекарня)")
                .address("Вул. Генерала Тарнавського, 104Б")
                .info(Sale.SALE_PUB)
                .coords(new Pair<Double, Double>(49.826611, 24.042041))
                .build());


    }

    private void displaySales(){

        for (Sale sale : listOfSales){
            TextView tvTitle = new TextView(BeerActivity.this);
            tvTitle.setPadding(36,22,6,4);
            tvTitle.setTextAppearance(this, R.style.tvTitleLarge);
            tvTitle.setText(sale.getTitle());
            TextView tvAddress = new TextView(BeerActivity.this);
            tvAddress.setTextAppearance(this, R.style.tvAddressMedium);
            tvAddress.setText(sale.getAddress());
            llSales.addView(tvTitle);
            llSales.addView(tvAddress);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        llSales = (LinearLayout) findViewById(R.id.llSales);

        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                messsage = "" + location.getLatitude() + "," + location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        //в 2016 ми називали це клієнт-сервером
        if (!messsage.equals("")){
            SendRequest sendRequest = new SendRequest();
            sendRequest.execute();
        }else {
            SendRequest sendRequest = new SendRequest();
            sendRequest.execute();
        }


        init();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BeerActivity.this, MapsActivity.class);
                intent.putParcelableArrayListExtra("sales", listOfSales);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_beer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*Якщо довго дивитись в Void, Void починає дивитись в тебе.*/
    private class SendRequest extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                client = new Socket("192.168.224.15", 3128);
                printwriter = new PrintWriter(client.getOutputStream(), true);
                printwriter.write("");//messsage);

                printwriter.flush();
                printwriter.close();
                client.close();
                init();
            }catch (UnknownHostException e){
                init();
            }catch (IOException e){
                init();
            }
            return null;
        }
    }
}
