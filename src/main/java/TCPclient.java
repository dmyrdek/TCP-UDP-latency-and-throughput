import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class TCPclient {

    String ipAddress;
    int port;

    public TCPclient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public long sendRTT(byte[] messageSize) throws IOException {
        byte[] response = new byte[messageSize.length];
        Socket socket = new Socket(ipAddress, port);
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        long start = System.nanoTime();
        output.write(messageSize);
        DataInputStream input = new DataInputStream(socket.getInputStream());

        for (int i =0; i<messageSize.length; i++) {
            response[i] = input.readByte();
        }

        long totalTime = System.nanoTime() - start;
        socket.close();
        output.close();
        input.close();

        return totalTime;
    }

    public long send1MB(int numMessages, int messageSize) throws IOException {
        byte [] responses = new byte [numMessages];
        byte [] message = new byte[messageSize];
        Arrays.fill(message, (byte)1);
        Socket socket = new Socket(ipAddress, port);
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream input = new DataInputStream(socket.getInputStream());
        long start = System.nanoTime();

        for (int messages = 0; messages <numMessages; messages++) {
            output.write(message);
            responses[messages] = input.readByte();
        }

        long totalTime = System.nanoTime() - start;
        socket.close();
        output.close();
        input.close();

        return totalTime;
    }
}
