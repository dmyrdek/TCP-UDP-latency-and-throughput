import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
}
