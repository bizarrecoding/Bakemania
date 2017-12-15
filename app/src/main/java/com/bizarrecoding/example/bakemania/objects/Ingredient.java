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
    int taken;

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

    public int getTaken() {
        return taken;
    }

    public void setTaken() {

        this.taken = taken == 0 ? 1 : 0;
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
            this.taken = 0;
            this.quantity = values.has("quantity") ? values.getInt("quantity") : 0;
            this.measure  = values.has("measure") ? values.getString("measure") : "";
            this.name  = values.has("ingredient") ? values.getString("ingredient") : "";
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        //Log.d("ingredient #"+this.getId(),taken+" "+name+"  "+quantity+measure.toLowerCase());
        return name+"  "+quantity+measure.toLowerCase();
    }

}
