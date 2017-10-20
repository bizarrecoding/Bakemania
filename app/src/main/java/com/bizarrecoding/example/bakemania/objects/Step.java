package com.bizarrecoding.example.bakemania.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONObject;
import java.io.Serializable;


public class Step implements Parcelable{
    int id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    public int getId() {
        return id;
    }
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

    public Step(int id, String shortDescription, String description, String videoURL, String thumbnail) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnail;
    }

    public Step(JSONObject values){
        try {
            this.id = values.has("id") ? values.getInt("id") : -1;
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
    public int describeContents() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
