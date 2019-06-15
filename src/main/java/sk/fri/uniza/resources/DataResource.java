package sk.fri.uniza.resources;


import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.auth.Role;
import sk.fri.uniza.core.Data;
import sk.fri.uniza.core.User;
import sk.fri.uniza.db.DataDao;

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

@Api(value = "Data resource", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources"), @AuthorizationScope(scope = Role.USER_READ_ONLY, description = "Limited access")})})


@ApiImplicitParams(
        @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = false, dataType = "string", paramType = "header")
)

@Path("/data")
public class DataResource {
    private final Logger myLogger = LoggerFactory.getLogger(this.getClass());
    private DataDao dataDao;

    public DataResource(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    @GET
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Role.ADMIN)

    // Swagger
    @ApiOperation(value = "Obtain list of datas", response = Data.class, responseContainer = "List", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources")})})
    public Response getListOfData(@QueryParam("limit") Integer limit, @QueryParam("page") Integer page) {
        if (page == null) page = 1;
        if (limit != null) {
            Paged<List<Data>> listPaged = dataDao.getAll(limit, page);
            return Response.ok()
                    .entity(listPaged)
                    .build();

        } else {
            List<Data> dataList = dataDao.getAll();
            return Response.ok()
                    .entity(dataList)
                    .build();
        }

    }

    @DELETE
    @UnitOfWork
    @RolesAllowed(Role.ADMIN)
    @ApiOperation(value = "Delete data", response = Data.class, authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources")})})
    public Response deleteData(@ApiParam(hidden = true) @Auth User user, @QueryParam("id") Long id) {
        //if (user.getId() != id) {
            Optional<Data> data1 = dataDao.findById(id);

            return data1.map(data -> {
                dataDao.delete(data);
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
    @ApiOperation(value = "Find data by ID", response = Data.class, authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources"), @AuthorizationScope(scope = Role.USER_READ_ONLY, description = "Limited access")})})
    public Data getDataInfo(@ApiParam(hidden = true) @Auth User user, @PathParam("id") Long id) {

        if (!user.getRoles().contains(Role.ADMIN)) {
            if (user.getId() != id) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
        }

        Optional<Data> data = dataDao.findById(id);
        return data.orElseThrow(() -> {
            throw new WebApplicationException("Wrong data ID!", Response.Status.BAD_REQUEST);
        });

    }

    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Role.ADMIN, Role.USER_READ_ONLY})
    @ApiOperation(value = "Save or update data", response = Data.class, authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = Role.ADMIN, description = "Access to all resources"), @AuthorizationScope(scope = Role.USER_READ_ONLY, description = "Limited access")})})
    public Data setDataInfo(@ApiParam(hidden = true) @Auth User user, @Valid Data data) {

        if (!user.getRoles().contains(Role.ADMIN)) {
            //if (user.getId() != data.getId()) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            //}
        }

        if (data.getId() == null)
            // If no id is presented, person is saved as new instance
            try {
                dataDao.save(data);
            } catch (HibernateException e) {
                throw new WebApplicationException(e.getMessage(), 422); //422 Unprocessable Entity - validation like errors
            }
        else
            dataDao.update(data, null);

        return data;
    }
}
