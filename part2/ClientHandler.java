package part2;

import part1.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Random;

public class ClientHandler implements Runnable {
    private static final int INIT_SECRET = 0;
    private static final int DEFAULT_TIMEOUT = 3000;

    private final UDPServer server;
    private final DatagramPacket inPacket;
    private final Random random;

    private final int num;
    private final int len;
    private final int secretA;

    private final int secretB;

    private final int num2;
    private final int len2;
    private final int secretC;
    private final byte c;

    private final int secretD;

    private short studentId;

    public ClientHandler(UDPServer server, DatagramPacket inPacket) {
        this.server = server;
        this.inPacket = inPacket;
        this.random = new Random();
        this.num = random.nextInt(16) + 10;
        this.len = random.nextInt(31) + 50;
        this.secretA = random.nextInt(200);
        this.secretB = random.nextInt(200);
        this.num2 = random.nextInt(16) + 10;
        this.len2 = random.nextInt(31) + 50;
        this.secretC = random.nextInt(200);
        this.c = (byte) (random.nextInt(256) - 128);
        this.secretD = random.nextInt(200);
    }

    @Override
    public void run() {
        try {
            UDPServer serverA = partA();
            TCPServer serverB = partB(serverA);

            // Accepts the first connection from TCP
            TCPServerSocket tcpSocket = serverB.accept();
            partC(tcpSocket);
            partD(tcpSocket);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalStateException ex) {
            printError("Some errors has occurred: " + ex.getMessage());
        } finally {
            print("Closing Connection...");
        }
    }

    private UDPServer partA() throws IllegalStateException, IOException {
        print("=======================Part A=======================");
        // part a1: parse "hello world" from inPacket
        Packet packetA1;
        packetA1 = new Packet(inPacket, new PayloadFactory.A1ClientPayloadFactory());
        if (packetA1.getHeader().pSecret != INIT_SECRET) {
            throw new IllegalStateException("incorrect initial pSecret");
        }
        this.studentId = packetA1.getHeader().studentId;

        // part a2: send num, len, udp_port, secretA to the client
        //          and make a new UDP server for part B

        UDPServer serverA2;
        // find available port
        int udpPort;
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
        Header headerA2 = new Header(payloadA2.getLength(), INIT_SECRET, (short) 2, studentId);
        Packet packetA2 = new Packet(headerA2, payloadA2);
        server.sendPacket(packetA2, packetA1.getAddress(), packetA1.getPort());
        return serverA2;
    }

    private TCPServer partB(UDPServer serverA) throws IOException, IllegalStateException {
        print("=======================Part B=======================");
        // part b1: the client will transmit num UDP packets in order to the serverA on port udpPort.
        Packet packetB1 = null;

        for (int i = 0; i < num; i++) {
            // receive a Packet;
            packetB1 = serverA.receivePacket(new PayloadFactory.B1ClientPayloadFactory(), DEFAULT_TIMEOUT);

            // wrong pSecret
            if (packetB1.getHeader().pSecret != secretA) {
                throw new IllegalStateException("incorrect pSecret A");
            }

            // wrong packetId
            if (((B1ClientPayload) packetB1.getPayload()).packetId != i) {
               throw new IllegalStateException("incorrect packetId");
            }

            // randomly send an acknowledgement (ACK)
            if (random.nextBoolean()) {
                Payload payloadB1 = new B1ServerPayload(i);
                Header headerB1 = new Header(payloadB1.getLength(), secretA, (short) 2, studentId);
                Packet packetB1Ack = new Packet(headerB1, payloadB1);
                serverA.sendPacket(packetB1Ack, packetB1.getAddress(), packetB1.getPort());
            } else {
                // purposely skip sending an acknowledgement
                i--;
            }
        }

        // part b2:
        TCPServer serverB2;

        // find available port
        int tcpPort;
        while (true) {
            tcpPort = random.nextInt((1 << 16));
            try {
                serverB2 = new TCPServer(tcpPort);
                break;
            } catch (SocketException ex) {
                continue;
            }
        }

        Payload payloadB2 = new B2ServerPayload(tcpPort, secretB);
        Header headerB2 = new Header(payloadB2.getLength(), secretA, (short) 2, studentId);
        Packet packetB2 = new Packet(headerB2, payloadB2);
        serverA.sendPacket(packetB2, packetB1.getAddress(), packetB1.getPort());
        return serverB2;
    }

    private void partC(TCPServerSocket tcpSocket) throws IOException, IllegalStateException {
        print("=======================Part C=======================");
        // step c2: sends num2, len2, secretC, c
        Payload payloadC2 = new C2ServerPayload(num2, len2, secretC, c);
        Header headerC2 = new Header(payloadC2.getLength(), secretB, (short) 2, studentId);
        Packet packetC2 = new Packet(headerC2, payloadC2);
        tcpSocket.sendPacket(packetC2);
    }

    private void partD(TCPServerSocket tcpSocket) throws IOException, IllegalStateException {
        print("=======================Part D=======================");
        // step d1: receives num2 payloads, each payload of length len2 all containing byte c
        for (int i = 0; i < num2; i++) {
            Packet packetD1 = tcpSocket.receivePacket(new PayloadFactory.D1ClientPayloadFactory(), DEFAULT_TIMEOUT);
            D1ClientPayload payloadD1 = (D1ClientPayload) packetD1.getPayload();
            if (packetD1.getHeader().pSecret != secretC) {
                throw new IllegalStateException("incorrect pSecret C");
            }
            if (payloadD1.c != c || payloadD1.len2 != len2) {
                throw new IllegalStateException("incorrect c or len2");
            }
        }

        // step d2: sends secretD
        Payload payloadD2 = new D2ServerPayload(secretD);
        Header headerD2 = new Header(payloadD2.getLength(), secretB, (short) 2, studentId);
        Packet packetD2 = new Packet(headerD2, payloadD2);
        tcpSocket.sendPacket(packetD2);
    }

    private void print(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }

    private void printError(String message) {
        print(inPacket.getAddress() + " (" + inPacket.getPort() + "): " + message);
    }
}
