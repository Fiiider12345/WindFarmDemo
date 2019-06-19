package sk.fri.uniza.core;

import sk.fri.uniza.api.Person;

import java.util.Set;

/**
 * Trieda pre vytvorenie objektu typu Person
 */
public class PersonBuilder {
    private String userName;
    private Set<String> roles;
    private String password;
    private String firstName;
    private String lastName;
    private String email;


    /**
     * setter
     * @param userName String
     * @return PersonBuilder
     */
    public PersonBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * setter
     * @param roles Set<String></>
     * @return PersonBuilder
     */
    public PersonBuilder setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    /**
     * setter
     * @param password String
     * @return PersonBuilder
     */
    public PersonBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * setter
     * @param firstName String
     * @return PersonBuilder
     */
    public PersonBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * setter
     * @param lastName String
     * @return PersonBuilder
     */
    public PersonBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * setter
     * @param email String
     * @return PersonBuilder
     */
    public PersonBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Vytvori novy objekt typu Person s nastavenymi hodnotami
     * @return Person
     */
    public Person createPerson() {
        return new Person(userName, roles, password, firstName, lastName, email);
    }
}