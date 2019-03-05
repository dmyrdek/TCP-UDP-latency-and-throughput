import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPclient {

    String ip;
    int port;

    DatagramSocket socket;
    InetAddress address;

    public UDPclient(String ip, int port ) throws UnknownHostException {
        this.ip = ip;
        this.port = port;
        address = InetAddress.getByName(ip);
    }


    public long sendAndMeasureRTT(byte [] message) throws IOException {
        socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);

        long start = System.nanoTime();
        socket.send(packet);
        packet = new DatagramPacket(message, message.length);
        socket.receive(packet);
        long totalTime = System.nanoTime() - start;

        socket.close();

        return totalTime;
    }

}
