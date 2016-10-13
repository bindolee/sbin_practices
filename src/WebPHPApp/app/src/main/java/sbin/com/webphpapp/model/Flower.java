package sbin.com.webphpapp.model;

import android.graphics.Bitmap;

/**
 * Created by sbin on 10/12/2016.
 */

public class Flower {
    // This is from http://services.hanselandpetal.com/feeds/flowers.xml ...xml format parsing.
    private int productId;
    private String category;
    private String name;
    private String instructions;
    private double price;
    private String photo;

    //Bitmat is needed to download the image from web and save it to this class
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public  int    getProductId() {
        return productId;
    }
    public void   setProductId(int productId) {
        this.productId = productId;
    }
    public String getName() {
        return name;
    }
    public void   setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void   setCategory(String category) {
        this.category = category;
    }
    public String getInstructions() {
        return instructions;
    }
    public void   setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public double getPrice() {
        return price;
    }
    public void   setPrice(double price) {
        this.price = price;
    }
    public String getPhoto() {
        return photo;
    }
    public void   setPhoto(String photo) {
        this.photo = photo;
    }


}
