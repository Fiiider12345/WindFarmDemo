package sk.fri.uniza.core;

public class DataBuilder {

    private Long id = null;
    private Float value;
    private int idDevice;
    //private String dateOfStart;

    public DataBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public DataBuilder setValue(Float value) {
        this.value = value;
        return this;
    }

    public DataBuilder setIdDevice(int idDevice) {
        this.idDevice = idDevice;
        return this;
    }

    /*public DataBuilder setDateOfStart(String dateOfStart) {
        this.dateOfStart = dateOfStart;
        return this;
    }*/

    public Data createData() {
        if (id == null)
            return new Data(value, idDevice);
        else
            return new Data(id, value, idDevice);
    }
}
