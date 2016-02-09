package network;

/**
 * Created by robotics on 2/8/2016.
 */
public class MainClientTest {

    public static void main(String[] args) {
        NS.connect(System.out::println, System.err::println);
    }
}
