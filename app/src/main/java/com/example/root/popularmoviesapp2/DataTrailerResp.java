package com.example.root.popularmoviesapp2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataTrailerResp {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<DataTrailer> results;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setResults(List<DataTrailer> results) {
        this.results = results;
    }

    public List<DataTrailer> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return
                "Trailer{" +
                        "id = '" + id + '\'' +
                        ",results = '" + results + '\'' +
                        "}";
    }
}