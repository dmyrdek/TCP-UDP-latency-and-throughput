import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class TCPserver {

    ServerSocket server;
    int port;

    public TCPserver(int port) {
        this.port = port;
    }

    public void startServer(int numBytes) throws IOException {
        server = new ServerSocket(this.port);
        byte[] message = new byte[numBytes];
        Socket connectionSocket = server.accept();
        DataInputStream input = new DataInputStream(connectionSocket.getInputStream());

        for (int i = 0; i < numBytes; i++) {
            message[i] = input.readByte();
        }

        DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
        output.write(message);

        server.close();
        connectionSocket.close();
        input.close();
        output.close();
    }

    public void echo1MBServer(int numMessages, int messageSize) throws IOException {
        server = new ServerSocket(this.port);
        byte[] message = new byte[messageSize];
        byte response = (byte) 1;
        Socket connectionSocket = server.accept();
        DataInputStream input = new DataInputStream(connectionSocket.getInputStream());
        DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());

        for (int messages = 0; messages < numMessages; messages++) {
            for (int i = 0; i < messageSize; i++) {
                message[i] = input.readByte();
            }
            output.write(response);
        }

        server.close();
        connectionSocket.close();
        input.close();
        output.close();
    }
}
