package com.example.panel1;

public class panel {

    private int id;
    private String nume;
    private String promotie;
    private String clasa;
    private byte[] image;

    public panel(String name, String promotie,String clasa,  byte[] image, int id) {
        this.nume = name;
        this.promotie = promotie;
        this.image = image;
        this.id = id;
        this.clasa=clasa;
    }


    public String getClasa() {
        return clasa;
    }

    public void setClasa(String clasa) {
        this.clasa = clasa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPromotie() {
        return promotie;
    }

    public void setPromotie(String promotie) {
        this.promotie = promotie;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}