package com.bizarrecoding.example.bakemania.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONObject;
import java.io.Serializable;

public class Ingredient implements Parcelable{
    int quantity;
    String measure;
    String name;

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getName() {
        return name;
    }

    public Ingredient(int quantity, String measure, String name) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(measure);
        dest.writeString(name);
    }

    protected Ingredient(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        name = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
