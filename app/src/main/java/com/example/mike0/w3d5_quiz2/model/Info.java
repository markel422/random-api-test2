package com.example.mike0.w3d5_quiz2.model;

public class Info {

    private String seed;
    private Integer results;
    private Integer page;
    private String version;

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Info{" +
                "seed='" + seed + '\'' +
                ", results=" + results +
                ", page=" + page +
                ", version='" + version + '\'' +
                '}';
    }
}
