package sk.fri.uniza.auth;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.core.*;
import sk.fri.uniza.db.DataDao;
import sk.fri.uniza.db.DeviceDao;
import sk.fri.uniza.db.UsersDao;

import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class OAuth2Authenticator implements Authenticator<String, User> {
    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Authenticator.class);
    private UsersDao usersDao;
    private DataDao datasDao;
    private DeviceDao devicesDao;
    private Key key = null;


    public OAuth2Authenticator(UsersDao usersDao, DataDao datasDao, DeviceDao devicesDao, Key key) {
        this.usersDao = usersDao;
        this.datasDao = datasDao;
        this.devicesDao = devicesDao;
        //We will sign our JWT with our ApiKey secret
        this.key = key;

    }


    @UnitOfWork
    public void generateUsers() {
        UsersDao.getUserDB().forEach(usersDao::save);
        DataDao.getDataDB().forEach(datasDao::save);
        DeviceDao.getDeviceDB().forEach(devicesDao::save);
     }

    @Override
    public Optional<User> authenticate(String jwtToken) throws AuthenticationException {
        Claims claimsJws;

        try {
            claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            //OK, we can trust this JWT
        } catch (SignatureException e) {
            //don't trust the JWT!
            LOG.error("Failed to authenticate token!", e);
            throw new AuthenticationException("Failed to authenticate token!", e);
        }

        Set<String> roles = parseRolesClaim(claimsJws);
        return Optional.of(
                new UserBuilder()
                        .setUserName(claimsJws.getSubject())
                        .setRoles(roles)
                        .setId(Long.valueOf(claimsJws.getId()))
                        .createUser());
    }


    private Set<String> parseRolesClaim(Claims claims) throws AuthenticationException {
        String scopesObject = claims.get("scope", String.class);
        String[] scopes = {};
        if (scopesObject != null) {
            scopes = scopesObject.split(" ");
        }
        return ImmutableSet.copyOf(Arrays.asList(scopes));
    }

}
