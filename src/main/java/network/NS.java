package network;

import network.packets.Packet;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class NS extends Thread implements Runnable {

    private static Socket serverConnection = null;
    private static ConsumerBlockingQueue<Packet, Consumer<Object>, Consumer<Object>> dataQueue = new ConsumerBlockingQueue<>();
    private static ObjectOutputStream os = null;

    @Override
    public void run() {
        while (true) {
            fireData(null, System.err::println);
        }
    }

    public synchronized static <T extends Packet> void addQueue(T t, Consumer<Object> success, Consumer<Object> failure) {
        dataQueue.add(t, success, failure);
    }

    public synchronized static void connect(Consumer<Object> success, Consumer<Object> failure) {
        try {
            serverConnection = new Socket("localhost", 7093);
            serverConnection.setKeepAlive(true);
            os = new ObjectOutputStream(serverConnection.getOutputStream());
        } catch (Exception e) {
            if (failure != null) {
                failure.accept(e.getMessage());
            } else {
                e.printStackTrace();
            }
            return;
        }
        if (success != null) {
            success.accept(null);
        }
    }

    public synchronized static void fireData(Consumer<Object> success, Consumer<Object> failure) {
        try {
            if (!dataQueue.isEmpty()) {
                Packet t = dataQueue.poll();
                os.writeObject(t);
                os.flush();
            }
        } catch (Exception e) {
            if (failure != null) {
                failure.accept(e);
            } else {
                e.printStackTrace();
            }
            return;
        }
        if (success != null) {
            success.accept(null);
        }
    }

    public synchronized static void close(Consumer<Object> success, Consumer<Object> failure) {
        try {
            serverConnection.getOutputStream().close();
            serverConnection.getInputStream().close();
            serverConnection.close();
        } catch (Exception e) {
            if (failure != null) {
                failure.accept(e);
            } else {
                e.printStackTrace();
            }
            return;
        }
        if (success != null) {
            success.accept(null);
        }

    }
}
