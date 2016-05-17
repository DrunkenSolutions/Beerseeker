package com.dmytryk.beerseeker.data;

/**
 * Created by dmytryk on 15.05.16.
 */
public class Beer {

    private String beerTitle;
    private String distributor;
    private String strength;
    private String imgUrl;
    private int stars;
    private int id;


    public String getBeerTitle() {
        return beerTitle;
    }

    public String getDistributor() {
        return distributor;
    }

    public String getStrength() {
        return strength;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Beer(String title){
        beerTitle = title;
    }

    public Beer(String title, String distr, String strength){
        this(title);
        distributor = distr;
        this.strength = strength;
    }

    public Beer(String title, String distr, String strength, String url){
        this(title, distr, strength);
        imgUrl = url;
    }




}
