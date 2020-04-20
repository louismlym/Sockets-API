package part2;

import part1.Packet;
import part1.PayloadFactory;

import java.io.IOException;
import java.net.*;

public class UDPServer {
    private int listenPort;
    private DatagramSocket socket;

    /**
     * @param listenPort the local port to bind with
     * @throws SocketException if the socket could not bind to the specified local port.
     */
    public UDPServer(int listenPort) throws SocketException {
        this.listenPort = listenPort;
        this.socket = new DatagramSocket(listenPort);
    }

    Packet receivePacket(PayloadFactory payloadFactory) throws IOException, IllegalStateException {
        DatagramPacket datagramPacket = receive(0);
        Packet packet = new Packet(datagramPacket, payloadFactory);
        System.out.println("<-- Received a packet...");
        System.out.println("      " + packet);
        return packet;
    }

    public DatagramPacket receive() throws IOException {
        return receive(0);
    }

    public DatagramPacket receive(int timeout) throws IOException {
        byte[] buffer = new byte[1400];
        socket.setSoTimeout(timeout);
        DatagramPacket receivingPacket = new DatagramPacket(buffer, 1400);
        socket.receive(receivingPacket);
        return receivingPacket;
    }

    public void sendPacket(Packet packet, InetAddress address, int port) throws IOException {
        System.out.println("--> Sending a packet...");
        System.out.println("      " + packet);
        send(new DatagramPacket(packet.asBytes(),
                packet.getLength(),
                address,
                port));
    }

    public void send(DatagramPacket packet) throws IOException {
        socket.send(packet);
    }

    public void close() {
        socket.close();
    }
}
