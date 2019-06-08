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

public class DataDao extends AbstractDAO<Data> implements BasicDao<Data, Long> {

    private static DataDao dataDao;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public DataDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Data> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(get(id));
    }

    @Override
    public List<Data> getAll() {
        return list(namedQuery("sk.fri.uniza.core.Data.getAll"));
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
        persist(data);
        return data.getId();
    }

    @Override
    public Long update(Data data, String[] params) {

        // Find person in DB and copy salt, secrete so the values will not be affected
        Optional<Data> dataOptional = findById(data.getId());
        dataOptional.ifPresent(data1 -> {
            currentSession().detach(data1);
        });
        persist(data);
        return data.getId();
    }

    @Override
    public void delete(Data data) {
        currentSession().delete(data);
    }


    public static DataDao createDataDao(SessionFactory sessionFactory) {
        if (dataDao == null)
            dataDao = new DataDao(sessionFactory);
        return dataDao;
    }

}
