package sk.fri.uniza.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.Calendar;


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

    public Data(Data other) {
        this.id = other.id;
        this.value = other.value;
        this.idDevice = other.idDevice;
        this.dateOfStart = other.dateOfStart;
    }

    // Hibernate need default constructor
    public Data() {
        Calendar cal=Calendar.getInstance();
        dateOfStart=  "Y:"+cal.get(Calendar.YEAR)+" M:"+(cal.get(Calendar.MONTH)+1)+" D:"+cal.get(Calendar.DAY_OF_MONTH)+
                " H:"+cal.get(Calendar.HOUR_OF_DAY)+" m:"+cal.get(Calendar.MINUTE)+" s:"+cal.get(Calendar.SECOND)+
                " ms:"+cal.get(Calendar.MILLISECOND);
    }

    public Data(int idDevice) {
        this.idDevice = idDevice;
        Calendar cal=Calendar.getInstance();
        dateOfStart=  "Y:"+cal.get(Calendar.YEAR)+" M:"+(cal.get(Calendar.MONTH)+1)+" D:"+cal.get(Calendar.DAY_OF_MONTH)+
                " H:"+cal.get(Calendar.HOUR_OF_DAY)+" m:"+cal.get(Calendar.MINUTE)+" s:"+cal.get(Calendar.SECOND)+
                " ms:"+cal.get(Calendar.MILLISECOND);
    }

    public Data(Float value, int idDevice) {
        this.value = value;
        this.idDevice = idDevice;
        Calendar cal=Calendar.getInstance();
        dateOfStart=  "Y:"+cal.get(Calendar.YEAR)+" M:"+(cal.get(Calendar.MONTH)+1)+" D:"+cal.get(Calendar.DAY_OF_MONTH)+
                " H:"+cal.get(Calendar.HOUR_OF_DAY)+" m:"+cal.get(Calendar.MINUTE)+" s:"+cal.get(Calendar.SECOND)+
                " ms:"+cal.get(Calendar.MILLISECOND);
    }

    public Data(Long id, Float value, int idDevice) {
        this.id = id;
        this.value = value;
        this.idDevice = idDevice;
        Calendar cal=Calendar.getInstance();
        dateOfStart=  "Y:"+cal.get(Calendar.YEAR)+" M:"+(cal.get(Calendar.MONTH)+1)+" D:"+cal.get(Calendar.DAY_OF_MONTH)+
                " H:"+cal.get(Calendar.HOUR_OF_DAY)+" m:"+cal.get(Calendar.MINUTE)+" s:"+cal.get(Calendar.SECOND)+
                " ms:"+cal.get(Calendar.MILLISECOND);
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
