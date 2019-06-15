package sk.fri.uniza.core;

public class DeviceBuilder {

    private Long id = null;
    private String name;
    private String content;

    public DeviceBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public DeviceBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DeviceBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public Device createDevice() {
        if (id == null)
            return new Device(name, content);
        else
            return new Device(id, name, content);
    }
}
