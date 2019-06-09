package sk.fri.uniza.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.core.Data;
import sk.fri.uniza.core.DataBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataDao extends AbstractDAO<Data> implements BasicDao<Data, Long> {

    private static final List<Data> dataDB;
    private static DataDao dataDao;

    static {
        Data data1 = new DataBuilder()
                .setValue(15.02f)
                .setIdDevice(3)
                .setDateOfStart("dnes")
                .createData();
        Data data2 = new DataBuilder()
                .setValue(18.02f)
                .setIdDevice(4)
                .setDateOfStart("zajtra")
                .createData();

        dataDB = Stream.generate(() -> new DataBuilder()
                .setValue(15.02f)
                .setIdDevice(3)
                .setDateOfStart("dnes")
                .createData())
                .limit(100)
                .collect(Collectors.toList());

        dataDB.add(data1);
        dataDB.add(data2);
    }

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public DataDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static List<Data> getDataDB() {
        return dataDB;
    }

    public static DataDao getDataDao() {
        return dataDao;
    }

    public static DataDao createDataDao(SessionFactory sessionFactory) {
        if (dataDao == null)
            dataDao = new DataDao(sessionFactory);
        return dataDao;
    }

    @Override
    public Optional<Data> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(get(id));
    }

    @Override
    public List<Data> getAll() {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<Data> criteriaQuery = builder.createQuery(Data.class);
        Root<Data> root = criteriaQuery.from(Data.class);
        criteriaQuery.select(root);
        List<Data> list = list(criteriaQuery);
        return list;
    }

    @Override
    public Paged<List<Data>> getAll(int limit, int page) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<Data> root = cq.from(Data.class);
        cq.select(builder.count(root));
        Long countResults = currentSession().createQuery(cq).getSingleResult();

        if (countResults == 0) return null;

        CriteriaQuery<Data> criteriaQuery = builder.createQuery(Data.class);
        criteriaQuery.select(criteriaQuery.from(Data.class));
        List<Data> list = currentSession().createQuery(criteriaQuery)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .list();

        return new Paged<>(page, limit, countResults, list);

    }

    @Override
    public Long save(Data data) throws HibernateException {
        return persist(data).getId();
    }

    @Override
    public Long update(Data data, String[] params) {

        // Find person in DB and copy salt, secrete so the values will not be affected
        Optional<Data> dataOptional = findById(data.getId());
        dataOptional.ifPresent(data1 -> {
            //person.setSalt(person1.getSalt());
            //person.setSecrete(person1.getSecrete());
            currentSession().detach(data1);
        });
        persist(data);
        return data.getId();
    }

    @Override
    public void delete(Data data) {
        currentSession().delete(data);
    }


}
