package ma.tp.retrofitjson.beans;


import java.io.Serializable;

public class Compte  implements Serializable {
    private Integer id;
    private double solde;
    private String dateCreation;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "id=" + id +
                ", solde=" + solde +
                ", dateCreation='" + dateCreation + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

