package sk.fri.uniza.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.auth.Role;
import sk.fri.uniza.core.Device;
import sk.fri.uniza.core.User;
import sk.fri.uniza.db.DeviceDao;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@SwaggerDefinition(
        securityDefinition = @SecurityDefinition(
                oAuth2Definitions = @OAuth2Definition(description = "oauth2", scopes = {@Scope(name = Role.ADMIN, description = "Access to all resources"), @Scope(name = Role.USER_READ_ONLY, description = "Limited access")}, flow = OAuth2Definition.Flow.ACCESS_CODE, tokenUrl = "http://localhost:8085/api/login/token", key = "oauth2", authorizationUrl = "http://localhost:8085/api/login")
        )
)

@Api(value = "Device resource", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources"), @AuthorizationScope(scope = Role.USER_READ_ONLY, description = "Limited access")})})


@ApiImplicitParams(
        @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = false, dataType = "string", paramType = "header")
)

@Path("/devices")
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

    @DELETE
    @UnitOfWork
    @RolesAllowed(Role.ADMIN)
    @ApiOperation(value = "Delete device", response = Device.class, authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources")})})
    public Response deleteDevice(@ApiParam(hidden = true) @Auth User user, @QueryParam("id") Long id) {
        //if (user.getId() != id) {
        Optional<Device> device1 = deviceDao.findById(id);

        return device1.map(device -> {
            deviceDao.delete(device);
            return Response.ok().build();
        }).get();

        //}
        //return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Role.ADMIN, Role.USER_READ_ONLY})
    @ApiOperation(value = "Find device by ID", response = Device.class, authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources"), @AuthorizationScope(scope = Role.USER_READ_ONLY, description = "Limited access")})})
    public Device getDeviceInfo(@ApiParam(hidden = true) @Auth User user, @PathParam("id") Long id) {

        if (!user.getRoles().contains(Role.ADMIN)) {
            if (!user.getId().equals(id)) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }

        Optional<Device> device = deviceDao.findById(id);
        return device.orElseThrow(() -> {
            throw new WebApplicationException("Wrong device ID!", Response.Status.BAD_REQUEST);
        });

    }

    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Role.ADMIN, Role.USER_READ_ONLY})
    @ApiOperation(value = "Save or update device", response = Device.class, authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources"), @AuthorizationScope(scope = Role.USER_READ_ONLY, description = "Limited access")})})
    public Device setDeviceInfo(@ApiParam(hidden = true) @Auth User user, @Valid Device device) {

        if (!user.getRoles().contains(Role.ADMIN)) {
            //if (user.getId() != data.getId()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            //}
        }

        if (device.getId() == null)
            // If no id is presented, person is saved as new instance
            try {
                deviceDao.save(device);
            } catch (HibernateException e) {
                throw new WebApplicationException(e.getMessage(), 422); //422 Unprocessable Entity - validation like errors
            }
        else
            deviceDao.update(device, null);

        return device;
    }
}
