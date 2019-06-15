package sk.fri.uniza.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.auth.Role;
import sk.fri.uniza.core.Device;
import sk.fri.uniza.db.DeviceDao;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@SwaggerDefinition(
        securityDefinition = @SecurityDefinition(
                oAuth2Definitions = @OAuth2Definition(description = "oauth2", scopes = {@Scope(name = Role.ADMIN, description = "Access to all resources"), @Scope(name = Role.USER_READ_ONLY, description = "Limited access")}, flow = OAuth2Definition.Flow.ACCESS_CODE, tokenUrl = "http://localhost:8085/api/login/token", key = "oauth2", authorizationUrl = "http://localhost:8085/api/login")
        )
)

@Api(value = "Device resource", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources"), @AuthorizationScope(scope = Role.USER_READ_ONLY, description = "Limited access")})})


@ApiImplicitParams(
        @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = false, dataType = "string", paramType = "header")
)

@Path("/device")
public class DeviceResource {
    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
    private DeviceDao deviceDao;

    public DeviceResource(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    @GET
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Role.ADMIN)

    // Swagger
    @ApiOperation(value = "Obtain list of devices", response = Device.class, responseContainer = "List", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources")})})
    public Response getListOfData(@QueryParam("limit") Integer limit, @QueryParam("page") Integer page) {
        if (page == null) page = 1;
        if (limit != null) {
            Paged<List<Device>> listPaged = deviceDao.getAll(limit, page);
            return Response.ok()
                    .entity(listPaged)
                    .build();

        } else {
            List<Device> dataList = deviceDao.getAll();
            return Response.ok()
                    .entity(dataList)
                    .build();
        }

    }
}
