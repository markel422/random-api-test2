package com.example.mike0.w3d5_quiz2.model;

import java.util.List;

public class RandomAPI {

    private List<Result> results = null;
    private Info info;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "RandomAPI{" +
                "results=" + results +
                ", info=" + info +
                '}';
    }
}
