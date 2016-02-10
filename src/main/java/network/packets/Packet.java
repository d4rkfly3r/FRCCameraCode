package network.packets;

import java.io.Serializable;

/**
 * Created by robotics on 2/8/2016.
 */
public abstract class Packet<T extends Packet> implements Serializable {

    protected Object innerData;
    protected String senderName;
    protected Type dataType;


    public T setDataType(Type dataType) {
        this.dataType = dataType;
        return (T) this;
    }

    public Type getDataType() {
        return dataType;
    }

    public enum Type {
        MESSAGE, JSON, SERIALIZED, UNKNOWN
    }

}
