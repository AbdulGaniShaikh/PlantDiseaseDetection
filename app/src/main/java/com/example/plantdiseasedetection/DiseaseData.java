package com.example.plantdiseasedetection;

public class DiseaseData {

    int image;
    String title,despcription;

    public DiseaseData(int image, String title, String despcription) {
        this.image = image;
        this.title = title;
        this.despcription = despcription;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDespcription() {
        return despcription;
    }
}
