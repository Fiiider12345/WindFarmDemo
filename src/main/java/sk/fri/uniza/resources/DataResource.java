package sk.fri.uniza.resources;


import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.core.Data;
import sk.fri.uniza.db.DataDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
}
