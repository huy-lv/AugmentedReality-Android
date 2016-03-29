package com.freedom.augmentedreality.model;

/**
 * Created by hienbx94 on 3/21/16.
 */
public class Marker {
    int id;
    String name;
    String image;
    String iset;
    String fset;
    String fset3;

    public Marker() {
    }

    public Marker(int id, String name, String image, String iset, String fset, String fset3) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.iset = iset;
        this.fset = fset;
        this.fset3 = fset3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIset() {
        return iset;
    }

    public void setIset(String iset) {
        this.iset = iset;
    }

    public String getFset() {
        return fset;
    }

    public void setFset(String fset) {
        this.fset = fset;
    }

    public String getFset3() {
        return fset3;
    }

    public void setFset3(String fset3) {
        this.fset3 = fset3;
    }
}
