package sk.fri.uniza.resources;

import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.core.Device;
import sk.fri.uniza.db.DeviceDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
