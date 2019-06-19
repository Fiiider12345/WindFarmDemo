package sk.fri.uniza.core;

/**
 * Trieda pre vytvorenie objektu typu Device
 */
public class DeviceBuilder {

    private Long id = null;
    private String name;
    private String content;

    /**
     * setter
     * @param id Long
     * @return DeviceBuilder
     */
    public DeviceBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * setter
     * @param name String
     * @return DeviceBuilder
     */
    public DeviceBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * setter
     * @param content String
     * @return DeviceBuilder
     */
    public DeviceBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Vytvori objekt typu Device s nastavenymi hodnotami
     * @return new Device
     */
    public Device createDevice() {
        if (id == null)
            return new Device(name, content);
        else
            return new Device(id, name, content);
    }
}
