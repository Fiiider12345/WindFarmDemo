package sk.fri.uniza.core;

/**
 * Trieda pre vytvorenie objektu typu Data
 */
public class DataBuilder {

    private Long id = null;
    private Float value;
    private int idDevice;
    private int idUser;

    /**
     * setter
     * @param id Long
     * @return DataBuilder
     */
    public DataBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * setter
     * @param value Float
     * @return DataBuilder
     */
    public DataBuilder setValue(Float value) {
        this.value = value;
        return this;
    }

    /**
     * setter
     * @param idDevice int
     * @return DataBuilder
     */
    public DataBuilder setIdDevice(int idDevice) {
        this.idDevice = idDevice;
        return this;
    }

    public DataBuilder setIdUser(int idUser) {
        this.idUser = idUser;
        return this;
    }

    /**
     * Vytvori objekt typu Data s nastavenymi hodnotami
     * @return new Data
     */
    public Data createData() {
        if (id == null)
            return new Data(value, idDevice, idUser);
        else
            return new Data(id, value, idDevice, idUser);
    }
}
