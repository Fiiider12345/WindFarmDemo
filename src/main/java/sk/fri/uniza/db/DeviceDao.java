package sk.fri.uniza.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.core.Device;
import sk.fri.uniza.core.DeviceBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeviceDao extends AbstractDAO<Device> implements BasicDao<Device, Long> {

    private static final List<Device> deviceDB;
    private static DeviceDao deviceDao;

    static {
        Device device1 = new DeviceBuilder()
                .setName("TempNimnica")
                .setContent("Posli mi teplotu v Nimnici!")
                .createDevice();
        Device device2 = new DeviceBuilder()
                .setName("TempLondon")
                .setContent("Posli mi teplotu v Londyne!")
                .createDevice();

        deviceDB = Stream.generate(() -> new DeviceBuilder()
                .setName("Something")
                .setContent("Posli mi nieco!")
                .createDevice())
                .limit(10)
                .collect(Collectors.toList());
        deviceDB.add(device1);
        deviceDB.add(device2);
    }


    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public DeviceDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public static List<Device> getDeviceDB() {
        return deviceDB;
    }

    public static DeviceDao getDeviceDao() {
        return deviceDao;
    }

    public static DeviceDao createDeviceDao(SessionFactory sessionFactory) {
        if (deviceDao == null)
            deviceDao = new DeviceDao(sessionFactory);
        return deviceDao;
    }

    @Override
    public Optional<Device> findById(Long id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(get(id));
    }

    @Override
    public List<Device> getAll() {
        return list(namedQuery("sk.fri.uniza.core.Device.getAll"));
    }

    /*@Override
    public List<Device> getAll() {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<Device> criteriaQuery = builder.createQuery(Device.class);
        Root<Device> root = criteriaQuery.from(Device.class);
        criteriaQuery.select(root);
        List<Device> list = list(criteriaQuery);
        return list;
    }*/

    @Override
    public Paged<List<Device>> getAll(int limit, int page) {

        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<Long> cq = builder.createQuery(Long.class);
        Root<Device> root = cq.from(Device.class);
        cq.select(builder.count(root));
        Long countResults = currentSession().createQuery(cq).getSingleResult();

        if (countResults == 0) return null;

        CriteriaQuery<Device> criteriaQuery = builder.createQuery(Device.class);
        criteriaQuery.select(criteriaQuery.from(Device.class));
        List<Device> list = currentSession().createQuery(criteriaQuery)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .list();

        return new Paged<>(page, limit, countResults, list);

    }

    @Override
    public Long save(Device device) throws HibernateException {
        persist(device);
        return device.getId();
    }

    @Override
    public Long update(Device device, String[] params) {
        // Find person in DB and copy salt, secrete so the values will not be affected
        Optional<Device> deviceOptional = findById(device.getId());
        deviceOptional.ifPresent(device1 -> {
            //person.setSalt(person1.getSalt());
            //person.setSecrete(person1.getSecrete());
            currentSession().detach(device1);
        });
        persist(device);
        return device.getId();
    }

    @Override
    public void delete(Device device) {
        currentSession().delete(device);
    }
}
