package com.bizarrecoding.example.bakemania.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.json.JSONObject;
import java.io.Serializable;


public class Step extends SugarRecord{
    long sid;
    long rid;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    public long getSid(){return sid;}
    public int getIntSid(){return (int)sid;}
    public void setSid(long sid){ this.sid=sid;}
    public long getRid(){return rid;}
    public void setRid(long rid){ this.rid=rid;}
    public String getShortDescription() {
        return shortDescription;
    }
    public String getDescription() {
        return description;
    }
    public String getVideoURL() {
        return videoURL;
    }
    public String getThumbnail() {
        return thumbnailURL;
    }

    public Step(){}

    public Step(int recipeId, String shortDescription, String description, String videoURL, String thumbnail) {
        this.rid = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnail;
    }

    public Step(JSONObject values){
        try {
            this.description = values.has("description") ? values.getString("description") : "";
            this.shortDescription = values.has("shortDescription") ? values.getString("shortDescription") : "";
            this.videoURL = values.has("videoURL") ? values.getString("videoURL") : "";
            this.thumbnailURL = values.has("thumbnailURL") ? values.getString("thumbnailURL") : "";
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return "id:"+getId()
                +"\nStep id:"+getSid()
                +"\nRecipe id:"+getRid()
                +"\n"+shortDescription
                +"\n"+videoURL;
    }
}
