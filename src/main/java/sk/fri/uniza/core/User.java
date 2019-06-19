package sk.fri.uniza.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Set;

/**
 * Definuje objekt User, tak ako bude ulozeny v databaze
 */
@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries(
        {
                @NamedQuery(
                        name = "sk.fri.uniza.core.User.getAll",
                        query = "SELECT p FROM User p"
                )
        })

public class User implements Principal {
    private static Random rand = new Random((new Date()).getTime());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;
    @JsonProperty
    @Column(unique = true)
    private String userName;
    @Column
    @ElementCollection(targetClass = String.class)
    @JsonProperty
    private Set<String> roles;
    @Column

    @Lob
    @JsonIgnore
    private byte[] secrete;
    @Lob
    @Column
    @JsonIgnore
    private byte[] salt;

    /**
     * Konstruktor, nastavi hotnoty podla parametra
     * @param other User
     */
    public User(User other) {
        this.id = other.id;
        this.userName = other.userName;
        this.roles = other.roles;
        this.secrete = other.secrete;
        this.salt = other.salt;
    }

    /**
     * Hibernate need default constructor
     */
    public User() {
    }

    /**
     * Konstruktor, nastavi hotnoty podla parametrov
     * @param id Long
     * @param userName String
     * @param roles Set<String>
     * @param password String
     */
    public User(Long id, String userName, Set<String> roles, String password) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
        if (password != null)
            setNewPassword(password);
    }

    /**
     * Konstruktor, nastavi hotnoty podla parametrov
     * @param userName String
     * @param roles Set<String>
     * @param password String
     */
    public User(String userName, Set<String> roles, String password) {
        this.userName = userName;
        this.roles = roles;
        setNewPassword(password);
    }

    /**
     * Return MD5 hashed password with salt.
     *
     * @param salt     byte array of random generater salt
     * @param password String representing password
     * @return MD5 hashed password with salt
     * @throws NoSuchAlgorithmException
     */

    public static byte[] generateHashSecrete(byte[] salt, String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return md.digest(ArrayUtils.addAll(salt, password.getBytes()));
    }

    /**
     * getter
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setter
     * @param userName String
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * getter
     * @return byte[]
     */
    public byte[] getSecrete() {
        return secrete;
    }

    /**
     * setter
     * @param secrete byte[]
     */
    public void setSecrete(byte[] secrete) {
        this.secrete = secrete;
    }

    /**
     * getter
     * @return byte[]
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * setter
     * @param salt byte[]
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    /*
          Implement Principal interface
     */

    /**
     * getter
     * @return userName
     */
    @JsonProperty("userName")
    @Override
    public String getName() {
        return userName;
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
     * @return roles
     */
    @Column
    @ElementCollection(targetClass = String.class,fetch = FetchType.EAGER)
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * setter
     * @param roles Set<String>
     **/
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    /**
     * getter
     * @return String
     */
    @JsonIgnore
    public String getRolesString() {
        return String.join(",", roles);
    }

    /**
     * setter
     * @param newPassword String
     */
    public void setNewPassword(String newPassword) {
        setSalt(new byte[8]);
        rand.nextBytes(getSalt());
        setSecrete(User.generateHashSecrete(salt, newPassword));
    }

    /**
     * Test if entered password is correct
     */
    public boolean testPassword(String password) {
        byte[] bytes = generateHashSecrete(this.salt, password);
        return Arrays.equals(bytes, secrete);

    }

    /**
     * porovnava objecty
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        if (!userName.equals(user.userName)) return false;
        if (!roles.equals(user.roles)) return false;
        if (!Arrays.equals(secrete, user.secrete)) return false;
        return Arrays.equals(salt, user.salt);

    }

    /**
     * generuje hash
     * @return int result
     */
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + roles.hashCode();
        result = 31 * result + Arrays.hashCode(secrete);
        result = 31 * result + Arrays.hashCode(salt);
        return result;
    }
}
