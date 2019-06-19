package sk.fri.uniza.core;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;


/**
 * Definuje objekt device, konstruktory, atributy a funkcie tak ako bude ulozeny v databaze
 * @author Pecho
 */
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

    /**
     * Hibernate need default constructor
     */
    public Device() {
    }

    /**
     * Konstruktor inicializuje parametre name, content
     * @param name String
     * @param content String
     */
    public Device(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * Konstruktor inicializuje parametre name, content, id
     * @param id Long
     * @param name String
     * @param content String
     */
    public Device(Long id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
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
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * setter
     * @param content String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * getter
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }
}
