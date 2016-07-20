package com.gnardini.testapplication.repository;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * SHARED PREFRENCES METHODS
 * <p/>
 * A bunch of shared preferences utils methods to get and set differnt types of values
 */
public class LocalStorage {

    //Vars
    private final SharedPreferences sharedPreferences;

    public LocalStorage(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void storeInSharedPreferences(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Integer value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Object object) {
        Gson gson = new GsonBuilder().create();
        sharedPreferences.edit().putString(key, gson.toJson(object)).apply();
    }

    public <T extends Serializable> void storeInSharedPreferences(
            String key, List<T> value) {
        Gson gson = new GsonBuilder().create();
        storeInSharedPreferences(key, gson.toJson(value));
    }

    public String getStringFromSharedPreferences(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public Integer getIntFromSharedPreferences(String key, Integer defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public Float getFloatFromSharedPreferences(String key, Float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    public Boolean getBooleanFromSharedPreferences(String key, Boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public Long getLongFromSharedPreferences(String key, Long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    public <T extends Serializable> List<T> getListFromSharedPreferences(
            String key, Type type) {
        String value = getStringFromSharedPreferences(key, null);
        Gson gson = new GsonBuilder().create();
        List<T> valueList = gson.fromJson(value, type);
        return valueList == null ? new LinkedList<T>() : valueList;
    }

    public void clearKey(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public boolean keyExists(String key) {
        return sharedPreferences.contains(key);
    }

}
