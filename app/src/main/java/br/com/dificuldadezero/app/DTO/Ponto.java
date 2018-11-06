package br.com.dificuldadezero.app.DTO;

public class Ponto {

    private double lat;
    private double longi;
    private String name;
    private String id;
    private String material;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Ponto(double longi, double lat, String name, String id, String material){ // name nao ira ser usado

        this.lat = lat;
        this.longi = longi;
        this.id = id;
        this.name = name;
        this.material = material;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }


}
