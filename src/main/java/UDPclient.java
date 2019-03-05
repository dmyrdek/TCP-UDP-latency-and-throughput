import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

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


    public long sendRTT(byte [] message) throws IOException {
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

    public long send1MB(int numMessages, int messageSize) throws IOException {
        byte [] message = new byte[messageSize];
        Arrays.fill(message, (byte)1);
        byte [] response = new byte [1];
        socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
        long start = System.nanoTime();

        for (int messages = 0; messages<numMessages; messages++) {
            socket.send(packet);
            DatagramPacket resp = new DatagramPacket(response, response.length);
            socket.receive(resp);
        }

        long totalTime = System.nanoTime() - start;
        socket.close();

        return totalTime;

    }

}
