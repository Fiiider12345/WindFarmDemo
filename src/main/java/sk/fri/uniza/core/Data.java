package sk.fri.uniza.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Definuje objekt data, konstruktory, atributy a funkcie tak ako budu ulozene v databaze
 * @author Pecho
 */

@Entity
@Table(name = "data")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries(
        {
                @NamedQuery(
                        name = "sk.fri.uniza.core.Data.getAll",
                        query = "SELECT d FROM Data d"
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

    @JsonProperty
    @Column
    private int idUser;

    /**
     * Konstruktor vrati objekt Data z parametra
     * @param other typ Data
     */
    public Data(Data other) {
        this.id = other.id;
        this.value = other.value;
        this.idDevice = other.idDevice;
        this.dateOfStart = other.dateOfStart;
        this.idUser = other.idUser;
    }

    /**
     * Hibernate need default constructor
     */
    public Data() {
        Calendar cal=Calendar.getInstance();
        dateOfStart = cal.get(Calendar.DAY_OF_MONTH)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.YEAR)+
                "  " + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    }

    /**
     * Konstruktor, inicializuje premennu dateOfStart a parameter idDevice
     * @param idDevice typ int
     */
    public Data(int idDevice, int idUser) {
        this.idDevice = idDevice;
        this.idUser = idUser;
        Calendar cal=Calendar.getInstance();
        dateOfStart = cal.get(Calendar.DAY_OF_MONTH)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.YEAR)+
                "  " + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    }

    /**
     * Konstruktor, inicializuje premennu dateOfStart a parametre value, idDevice
     * @param value Float
     * @param idDevice int
     */
    public Data(Float value, int idDevice, int idUser) {
        this.value = value;
        this.idDevice = idDevice;
        this.idUser = idUser;
        Calendar cal=Calendar.getInstance();
        dateOfStart = cal.get(Calendar.DAY_OF_MONTH)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.YEAR)+
                "  " + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    }

    /**
     * Konstruktor, inicializuje premennu dateOfStart a parametre id, value, idDevice
     * @param id Long
     * @param value Float
     * @param idDevice int
     */
    public Data(Long id, Float value, int idDevice, int idUser) {
        this.id = id;
        this.value = value;
        this.idDevice = idDevice;
        this.idUser = idUser;
        Calendar cal=Calendar.getInstance();
        dateOfStart = cal.get(Calendar.DAY_OF_MONTH)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.YEAR)+
                "  " + cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
    }

    /**
     * getter
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * setter
     * @param id Long
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getter
     * @return value
     */
    public Float getValue() {
        return value;
    }

    /**
     * setter
     * @param value Float
     */
    public void setValue(Float value) {
        this.value = value;
    }

    /**
     * getter
     * @return idDevice
     */
    public int getIdDevice() {
        return idDevice;
    }

    /**
     * setter
     * @param idDevice int
     */
    public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
    }

    /**
     * getter
     * @return dateOfStart
     */
    public String getDateOfStart() {
        return dateOfStart;
    }

    /**
     * setter
     * @param dateOfStart String
     */
    public void setDateOfStart(String dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
