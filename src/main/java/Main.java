import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    final static int trials = 5;

    static TCPclient tcpClient;
    static UDPclient udpClient;

    static int [] tcpRTT1Byte = new int [trials];
    static int [] tcpRTT64Byte = new int [trials];
    static int [] tcpRTT1024Byte = new int [trials];

    static int [] udpRTT1Byte = new int [trials];
    static int [] udpRTT64Byte = new int [trials];
    static int [] udpRTT1024Byte = new int [trials];


    static float [] tcpThroughput1KByte = new float [trials];
    static float [] tcpThroughput16KByte = new float [trials];
    static float [] tcpThroughput64KByte = new float [trials];
    static float [] tcpThroughput256KByte = new float [trials];
    static float [] tcpThroughput1MByte = new float [trials];



    public static void main(String[] args) throws IOException {
        int port = 2799;
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.print("Would you like to create a server or client? Enter \"s\" for a server or \"c\" for a client: ");

        while (true) {
            input = sc.next();
            if (input.equals("s") || input.equals("c")){
                break;
            }else{
                System.out.print("Sorry that input is incorrect, please enter \"s\" for a server or \"c\" for a client: ");
            }
        }

        if (input.equals("s")){
            int currentTrial = 0;

            getIPAddress();

            TCPserver tcpServer = new TCPserver(port);

            UDPserver udpServer = new UDPserver(port);

            for (; currentTrial < trials; currentTrial++) {
                tcpServer.startServer(1);
                tcpServer.startServer(64);
                tcpServer.startServer(1024);

                udpServer.startServer(1);
                udpServer.startServer(64);
                udpServer.startServer(1024);

                tcpServer.startServer(1024);
                tcpServer.startServer(16384);
                tcpServer.startServer(65536);
                tcpServer.startServer(262144);
                tcpServer.startServer(1048576);

            }


        } else if (input.equals("c")){
            int currentTrial = 0;

            System.out.print("Please enter the server IP Address: ");
            String ipAddress = sc.next();

            tcpClient = new TCPclient(ipAddress,port);
            udpClient = new UDPclient(ipAddress,port);

            for (; currentTrial < trials; currentTrial++) {
                tcpRTT1Byte[currentTrial] = sendTCPMessage("RTT for 1 byte:", 1);
                tcpRTT64Byte[currentTrial] = sendTCPMessage("RTT for 64 bytes:", 64);
                tcpRTT1024Byte[currentTrial] = sendTCPMessage("RTT for 1024 bytes:", 1024);

                udpRTT1Byte[currentTrial] = sendUDPMessage("RTT for 1 byte:", 1);
                udpRTT64Byte[currentTrial] = sendUDPMessage("RTT for 64 byte:", 64);
                udpRTT1024Byte[currentTrial] = sendUDPMessage("RTT for 1024 byte:", 1024);

                tcpThroughput1KByte[currentTrial] = measureTCPThroughput(1024);
                tcpThroughput16KByte[currentTrial] = measureTCPThroughput(16384);
                tcpThroughput64KByte[currentTrial] =  measureTCPThroughput(65536);
                tcpThroughput256KByte[currentTrial] = measureTCPThroughput(262144);
                tcpThroughput1MByte[currentTrial] = measureTCPThroughput(1048576);

            }
        }
    }

    public static int sendTCPMessage(String outputMessage, int numBytes) throws IOException {
        byte [] message = new byte[numBytes];
        Arrays.fill(message, (byte)1);
        long RTT = tcpClient.sendRTT(message);
        System.out.println(outputMessage +  " " + TimeUnit.MICROSECONDS.convert(RTT, TimeUnit.NANOSECONDS) + " microseconds");
        return (int)TimeUnit.MICROSECONDS.convert(RTT, TimeUnit.NANOSECONDS);
    }

    public static int sendUDPMessage(String outputMessage, int numBytes) throws IOException {
        byte [] message = new byte[numBytes];
        Arrays.fill(message, (byte)1);
        long RTT = udpClient.sendAndMeasureRTT(message);
        System.out.println(outputMessage +  " " + TimeUnit.MICROSECONDS.convert(RTT, TimeUnit.NANOSECONDS) + " microseconds");
        return (int)TimeUnit.MICROSECONDS.convert(RTT, TimeUnit.NANOSECONDS);
    }

    public static float measureTCPThroughput(int numBytes) throws IOException {
        byte [] message = new byte[numBytes];
        Arrays.fill(message, (byte)1);
        long RTT = tcpClient.sendRTT(message);

        //throughout here in bits/nanosecond
        int numBits = numBytes * 8 * 2;
        double throughput = (double)numBits/RTT;

        //convert to megabits/sec
        float throughputMBPS = (float)throughput*1000;

        System.out.println("Throughput for " + numBytes + " : " + throughputMBPS + " Mbps");
        return throughputMBPS;

    }

    public static void getIPAddress() throws IOException{
        String ipAddress = "";
        URL url_name = new URL("http://bot.whatismyipaddress.com");
        BufferedReader sc = new BufferedReader(new InputStreamReader(url_name.openStream()));
        ipAddress = sc.readLine().trim();
        System.out.println("Public IP Address: " + ipAddress);
    }
}
