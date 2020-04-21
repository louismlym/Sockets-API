package part2;

import part1.Packet;
import part1.PayloadFactory;

import java.io.IOException;
import java.net.*;

/**
 * UDPServer is an implementation of UDP server that is bound to a
 * DatagramSocket. It helps the server to receive and send a packet.
 * UDPServer must be closed when finish using. See try-with-resources
 * for more details on how it can be closed automatically.
 */
public class UDPServer implements AutoCloseable {
    private static final int BUFFER_LEN = 1024;
    private DatagramSocket socket;

    /**
     * @param listenPort the local port to bind with
     * @throws SocketException if the socket could not bind to the specified local port.
     */
    public UDPServer(int listenPort) throws SocketException {
        this.socket = new DatagramSocket(listenPort);
    }

    Packet receivePacket(PayloadFactory payloadFactory) throws IOException, IllegalStateException {
        return receivePacket(payloadFactory, 0);
    }

    /**
     * Receive a packet from the client
     *
     * @param payloadFactory the payload factory which helps create a new payload of desired type
     * @param timeout the maximum time wait for packet to be received (in milliseconds)
     * @return a Packet received from the client which is extracted from DatagramPacket
     * @throws IOException
     * @throws IllegalStateException if the packet is not well-formatted / doesn't meet server's
     *                               conditions
     */
    Packet receivePacket(PayloadFactory payloadFactory, int timeout) throws IOException, IllegalStateException {
        DatagramPacket datagramPacket = receive(timeout);
        Packet packet = new Packet(datagramPacket, payloadFactory);
        System.out.println("[" + Thread.currentThread().getName() + "] <-- Received a packet from "
                + packet.getAddress() + " (" + packet.getPort() + ")...\n"
                + "      " + packet);
        return packet;
    }

    public DatagramPacket receive() throws IOException {
        return receive(0);
    }

    /**
     * Receive a DatagramPacket from the client
     *
     * @param timeout the maximum time wait for packet to be received (in milliseconds)
     * @return a DatagramPacket received from the client
     * @throws IOException
     */
    public DatagramPacket receive(int timeout) throws IOException {
        byte[] buffer = new byte[BUFFER_LEN];
        socket.setSoTimeout(timeout);
        DatagramPacket receivingPacket = new DatagramPacket(buffer, BUFFER_LEN);
        socket.receive(receivingPacket);
        return receivingPacket;
    }

    /**
     * Send a Packet to the client
     *
     * @param packet the packet to send
     * @param address the address of the client
     * @param port the port that the client uses
     * @throws IOException
     */
    public void sendPacket(Packet packet, InetAddress address, int port) throws IOException {
        System.out.println("[" + Thread.currentThread().getName() + "] --> Sending a packet to "
                + address + " (" + port + ")...\n"
                + "      " + packet);
        send(new DatagramPacket(packet.asBytes(),
                packet.getLength(),
                address,
                port));
    }

    /**
     * Send a UDP packet to a client
     *
     * @param packet a DatagramPacket to be send
     * @throws IOException
     */
    public void send(DatagramPacket packet) throws IOException {
        socket.send(packet);
    }

    /**
     * Close the UDP socket
     */
    public void close() {
        socket.close();
    }
}
