package com.udolgc.mustknowscriptures;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "scripture_table")
public class ScriptureEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "scripture")
    private String scripture;

    @ColumnInfo(name = "favourite")
    private String favourite;

//    private boolean flipped = false;

    public boolean isFlipped;

    public ScriptureEntity(int id, String title, String scripture) {
        this.id = id;
        this.title = title;
        this.scripture = scripture;
    }

    public ScriptureEntity(String title, String scripture) {
        this.title = title;
        this.scripture = scripture;
    }

    public ScriptureEntity(String title, String scripture, String favourite) {
        this.title = title;
        this.scripture = scripture;
        this.favourite = favourite;
    }

    public ScriptureEntity(int id) {
        this.id = id;
    }

    public ScriptureEntity() {

    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScripture() {
        return scripture;
    }

    public void setScripture(String scripture) {
        this.scripture = scripture;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    //    public boolean isFlipped() {
//        return flipped;
//    }
//
//    public void setFlipped(boolean flipped) {
//        this.flipped = flipped;
//    }
}
