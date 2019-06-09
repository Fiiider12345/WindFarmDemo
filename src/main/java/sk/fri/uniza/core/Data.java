package sk.fri.uniza.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;


@Entity
@Table(name = "Data")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries(
        {
                @NamedQuery(
                        name = "sk.fri.uniza.core.Data.getAll",
                        query = "SELECT p FROM Data p"
                )
        })

public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @JsonProperty
    @Column
    private Float value;

    @JsonProperty
    @Column
    private int idDevice;

    @JsonProperty
    @Column
    private String dateOfStart;

    public Data(Data other) {
        this.id = other.id;
        this.value = other.value;
        this.idDevice = other.idDevice;
        this.dateOfStart = other.dateOfStart;
    }

    // Hibernate need default constructor
    public Data() {
    }

    public Data(Float value, int idDevice, String dateOfStart) {
        this.value = value;
        this.idDevice = idDevice;
        this.dateOfStart = dateOfStart;
    }

    public Data(Long id, Float value, int idDevice, String dateOfStart) {
        this.id = id;
        this.value = value;
        this.idDevice = idDevice;
        this.dateOfStart = dateOfStart;
    }

    //@JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //@JsonProperty
    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    //@JsonProperty
    public int getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
    }

    //@JsonProperty
    public String getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(String dateOfStart) {
        this.dateOfStart = dateOfStart;
    }
}
