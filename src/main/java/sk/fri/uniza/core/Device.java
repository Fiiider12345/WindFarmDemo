package sk.fri.uniza.core;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;

@Entity
@Table(name = "Device")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries(
        {
                @NamedQuery(
                        name = "sk.fri.uniza.core.Device.getAll",
                        query = "SELECT p FROM Device p"
                )
        })
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @JsonProperty
    @Column
    private String name;

    @JsonProperty
    @Column
    private String content;

    // Hibernate need default constructor
    public Device() {
    }

    public Device(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public Device(Long id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    //@JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //@JsonProperty
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
