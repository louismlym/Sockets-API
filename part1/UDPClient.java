package part1;

import java.io.IOException;
import java.net.*;

public class UDPClient extends AbstractClient {
    private DatagramSocket socket;
    private InetAddress address;


    public UDPClient(String host, int port) throws IOException {
        super(host, port);
        this.address = InetAddress.getByName(host);
    }

    @Override
    public void open() throws IOException {
        this.socket = new DatagramSocket();
    }

    @Override
    public void sendBytes(byte[] bytes, int length) throws IOException {
        System.out.printf("[DEBUG] Sending %d bytes\n", length);
        DatagramPacket datagramPacket = new DatagramPacket(bytes, length, address, port);
        socket.send(datagramPacket);
    }

    @Override
    public byte[] receiveBytes(int readLen, int timeout) throws IOException {
        byte[] buffer = new byte[readLen];
        DatagramPacket receivingPacket = new DatagramPacket(buffer, readLen);

        socket.setSoTimeout(timeout);
        socket.receive(receivingPacket);

        return receivingPacket.getData();
    }

    @Override
    public void close() {
        socket.close();
    }
}