package com.bizarrecoding.example.bakemania.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONObject;
import java.io.Serializable;

public class Ingredient extends SugarRecord{

    long idingredient;
    long rid;
    int quantity;
    String measure;
    String name;

    public long getIdingredient() {
        return idingredient;
    }

    public void setIdingredient(long idingredient) {
        this.idingredient = idingredient;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getName() {
        return name;
    }

    public Ingredient(){}

    public Ingredient(long recipeId, int quantity, String measure, String name) {
        this.rid = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    public Ingredient(JSONObject values){
        try{
            this.quantity = values.has("quantity") ? values.getInt("quantity") : 0;
            this.measure  = values.has("measure") ? values.getString("measure") : "";
            this.name  = values.has("name") ? values.getString("name") : "";
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
    }
}
