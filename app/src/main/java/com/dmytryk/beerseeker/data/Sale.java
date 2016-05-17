package com.dmytryk.beerseeker.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

/**
 * Created by dmytryk on 15.05.16.
 */
public class Sale implements Parcelable{

    public static final String SALE_PUB = "0";
    public static final String SALE_SHOP = "1";


    private String title;
    private String info = "";
    private String address;
    private double latitude;
    private double longitude;
    private int id;



    private Sale(String stitle, String sinfo, String saddress){
        title = stitle;
        info = sinfo;
        address = saddress;
    }

    private Sale(String stitle, String  sinfo, String  saddress, double x, double y){
        this(stitle, sinfo, saddress);
        latitude = x;
        longitude = y;
    }

    private Sale(String stitle, String saddress, double x, double y){
        this(stitle, "", saddress, x, y);
    }

    private Sale(String stitle, String saddress, Pair<Double, Double> coords){
        this(stitle, "", saddress, coords.first, coords.second);
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public static Sale getInstance(String title, String address, double x, double y, String type) {
        if (type.equals(Sale.SALE_PUB) || type.equals(SALE_SHOP))
            return new Sale(title, address, type, x, y);
        else return new Sale(title, address, SALE_SHOP, x, y);
    }
    //
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(info);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        //не я дебіл, так нада
        //dest.writeDouble(y);
        //dest.writeDouble(x);

    }

    public static final Parcelable.Creator<Sale> CREATOR = new Parcelable.Creator<Sale>(){
        @Override
        public Sale createFromParcel(Parcel source) {
            return new Sale(source);
        }

        @Override
        public Sale[] newArray(int size) {
            return new Sale[size];
        }

    };

    public static class Builder {
        private final String title;
        private String info = "";
        private String address;
        private double latitude;
        private double longitude;

        public Builder(String stitle){
            title = stitle;
        }

        public Builder address(String saddress){
            address = saddress;
            return this;
        }

        public Builder info(String sinfo){
            info = sinfo;
            return this;
        }

        public Builder latitude(double slatitude){
            latitude = slatitude;
            return this;
        }

        public Builder longtitude(double slongtitude){
            longitude = slongtitude;
            return this;
        }

        public Builder coords(Pair<Double, Double> coords){
            latitude = coords.first;
            longitude = coords.second;
            return this;
        }

        public Sale build(){
            return new Sale(this);
        }
    }

    private Sale(Builder builder){
        title = builder.title;
        address = builder.address;
        info = builder.info;
        latitude = builder.latitude;
        longitude = builder.longitude;
    }

    private Sale(Parcel parcel){
        title = parcel.readString();
        info = parcel.readString();
        address = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();

    }
}
