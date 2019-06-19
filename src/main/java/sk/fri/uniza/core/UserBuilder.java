package sk.fri.uniza.core;

import java.util.Set;

/**
 * Trieda pre vytvorenie objektu typu User
 */
public class UserBuilder {
    private Long id = null;
    private String userName;
    private Set<String> roles;
    private String password;

    /**
     * setter
     * @param id Long
     * @return UserBuilder
     */
    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * setter
     * @param userName String
     * @return UserBuilder
     */
    public UserBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * setter
     * @param roles Set<String></>
     * @return UserBuilder
     */
    public UserBuilder setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    /**
     * setter
     * @param password String
     * @return UserBuilder
     */
    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Vytvori objekt typu User s nastavenymi hodnotami
     * @return new User
     */
    public User createUser() {
        if (id == null)
            return new User(userName, roles, password);
        else
            return new User(id, userName, roles, password);
    }
}