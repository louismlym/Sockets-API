package part2;

import part1.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Random;

public class ClientHandler implements Runnable {
    private static final int INIT_SECRET = 0;
    private final UDPServer server;
    private final DatagramPacket inPacket;
    private final Random random;
    private final int num;
    private final int len;
    private int udpPort;
    private final int secretA;

    public ClientHandler(UDPServer server, DatagramPacket inPacket) {
        this.server = server;
        this.inPacket = inPacket;
        this.random = new Random();
        this.num = random.nextInt(16) + 10;
        this.len = random.nextInt(31) + 50;
        this.secretA = random.nextInt(200);
    }

    @Override
    public void run() {
        try {
            UDPServer serverA = partA();
            UDPServer serverB = partB(serverA);
        } catch (IllegalStateException ex) {
            printError("closing the connection");
            return;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private UDPServer partA() throws IllegalStateException, IOException {
        System.out.println("=======================Part A=======================");
        // part a1: parse "hello world" from inPacket
        Packet packetA1;
        try {
            packetA1 = new Packet(inPacket, new PayloadFactory.A1ClientPayloadFactory());
            if (packetA1.getHeader().pSecret != INIT_SECRET) {
                printError("wrong pSecret at stage a.");
                throw new IllegalStateException("wrong pSecret at stage a.");
            }
        } catch(IllegalStateException ex) {
            printError("didn't send valid packet at Part a1.");
            throw ex;
        }

        // part a2: send num, len, udp_port, secretA to the client
        //          and make a new UDP server for part B

        UDPServer serverA2;
        // find available port
        while (true) {
            udpPort = random.nextInt((1 << 16));
            try {
                serverA2 = new UDPServer(udpPort);
                break;
            } catch (SocketException ex) {
                continue;
            }
        }
        Payload payloadA2 = new A2ServerPayload(num, len, udpPort, secretA);
        Header headerA2 = new Header(payloadA2.getLength(), INIT_SECRET, (short) 2, packetA1.getHeader().studentId);
        Packet packetA2 = new Packet(headerA2, payloadA2);
        server.sendPacket(packetA2, packetA1.getAddress(), packetA1.getPort());
        return serverA2;
    }

    private UDPServer partB(UDPServer serverA) throws IOException, IllegalStateException {
        System.out.println("=======================Part B=======================");
        // part b1: the client will transmit num UDP packets in order to the serverA on port udpPort.
        for (int i = 0; i < num; i++) {
            // receive a Packet;
            Packet packetB1;
            try {
               packetB1 = serverA.receivePacket(new PayloadFactory.B1ClientPayloadFactory());
            } catch (IllegalStateException ex) {
               printError("didn't send valid packet at Part a1.");
               throw ex;
            }

            // wrong packetId
            if (((B1ClientPayload) packetB1.getPayload()).packetId != i) {
               printError("packetId was incorrect.");
               throw new IllegalArgumentException("packetId was incorrect.");
            }

            // randomly send an acknowledgement (ACK)
            if (random.nextBoolean()) {
                Payload payloadB1 = new B1ServerPayload(i);
                Header headerB1 = new Header(payloadB1.getLength(), secretA, (short) 2, packetB1.getHeader().studentId);
                Packet packetB1Ack = new Packet(headerB1, payloadB1);
                serverA.sendPacket(packetB1Ack, packetB1.getAddress(), packetB1.getPort());
            } else {
                i--;
            }
        }

        // part b2:
        return null;
    }

    private void printError(String message) {
        System.out.println(inPacket.getAddress() + " (" + inPacket.getPort() + "): " + message);

    }
}
