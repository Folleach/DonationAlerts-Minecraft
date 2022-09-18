package net.folleach.daintegrate;

public class EntityId {
    private final String modId;
    private final String entityId;
    private final String subsystem;
    private final String modUrl;

    public EntityId(String modId, String entityId, String subsystem, String modUrl) {

        this.modId = modId;
        this.entityId = entityId;
        this.subsystem = subsystem;
        this.modUrl = modUrl;
    }

    public String getModUrl() {
        return modUrl;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getModId() {
        return modId;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public String getUniqueId() {
        return modId + "/" + subsystem + "/" + entityId;
    }
}
