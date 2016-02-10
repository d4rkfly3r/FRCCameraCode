package network;

import network.packets.Packet;
import network.packets.Packet01;

/**
 * Created by robotics on 2/8/2016.
 */
public class MainClientTest {

    public static void main(String[] args) {
        new NS().start();
        NS.connect(null, System.err::println);
        NS.addQueue(new Packet01().setDataType(Packet.Type.SERIALIZED), null, null);
    }
}
