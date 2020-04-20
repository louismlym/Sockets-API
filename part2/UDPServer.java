package part2;

import java.io.IOException;
import java.net.*;

public class UDPServer {
    private int listenPort;
    private DatagramSocket socket;

    public UDPServer(int listenPort) throws IOException {
        this.listenPort = listenPort;
        this.socket = new DatagramSocket(listenPort);
    }

    DatagramPacket receive() throws IOException {
        return receive(0);
    }

    DatagramPacket receive(int timeout) throws IOException {
        byte[] buffer = new byte[1400];
        socket.setSoTimeout(timeout);
        DatagramPacket receivingPacket = new DatagramPacket(buffer, 1400);
        socket.receive(receivingPacket);
        return receivingPacket;
    }

    void send(DatagramPacket packet) throws IOException {
        socket.send(packet);
    }

    public void close() {
        socket.close();
    }
}
