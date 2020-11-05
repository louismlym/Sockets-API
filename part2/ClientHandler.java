package part2;

import part1.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Random;

/**
 * ClientHandler handles a client that get connected to the server
 * in {@code Server}. It runs under a thread which means multiple
 * clients can be handle at a time.
 * It follows STAGE a-d in the project specifications.
 */
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

        // pre-generating values to send to the client
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
            serverB.close();
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
        checkHeader(packetA1.getHeader(), INIT_SECRET);
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
            // receive a Packet
            try {
                packetB1 = serverA.receivePacket(new PayloadFactory.B1ClientPayloadFactory(), DEFAULT_TIMEOUT);
            } catch (Exception ex) {
                serverA.close();
                throw ex;
            }

            checkHeader(packetB1.getHeader(), secretA);

            B1ClientPayload packetB1Payload = (B1ClientPayload)packetB1.getPayload();

            // wrong packetId
            if (packetB1Payload.packetId != i) {
               throw new IllegalStateException("incorrect packetId");
            }
            if (packetB1Payload.zeros.length != len) {
              throw new IllegalStateException("incorrect len");
            }

            // randomly send an acknowledgement (ACK)
            if (random.nextInt(100) > 20) {
                Payload payloadB1 = new B1ServerPayload(i);
                Header headerB1 = new Header(payloadB1.getLength(), secretA, (short) 2, studentId);
                Packet packetB1Ack = new Packet(headerB1, payloadB1);
                serverA.sendPacket(packetB1Ack, packetB1.getAddress(), packetB1.getPort());
            } else {
                // purposely skip sending an acknowledgement
                i--;
            }
        }

        // part b2: sends a UDP packet containing tcp_port and secretB
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
        serverA.close();
        return serverB2;
    }

    private void partC(TCPServerSocket tcpSocket) throws IOException, IllegalStateException {
        print("=======================Part C=======================");
        // part c2: sends num2, len2, secretC, c
        Payload payloadC2 = new C2ServerPayload(num2, len2, secretC, c);
        Header headerC2 = new Header(payloadC2.getLength(), secretB, (short) 2, studentId);
        Packet packetC2 = new Packet(headerC2, payloadC2);
        tcpSocket.sendPacket(packetC2);
    }

    private void partD(TCPServerSocket tcpSocket) throws IOException, IllegalStateException {
        print("=======================Part D=======================");
        // part d1: receives num2 payloads, each payload of length len2 all containing byte c
        for (int i = 0; i < num2; i++) {
            Packet packetD1;
            try {
                packetD1 = tcpSocket.receivePacket(new PayloadFactory.D1ClientPayloadFactory(), DEFAULT_TIMEOUT);
            } catch (Exception ex) {
                tcpSocket.close();
                throw ex;
            }
            D1ClientPayload payloadD1 = (D1ClientPayload) packetD1.getPayload();
            checkHeader(packetD1.getHeader(), secretC);
            if (payloadD1.c != c || payloadD1.len2 != len2) {
                throw new IllegalStateException("incorrect c or len2");
            }
        }

        // part d2: sends secretD
        Payload payloadD2 = new D2ServerPayload(secretD);
        Header headerD2 = new Header(payloadD2.getLength(), secretC, (short) 2, studentId);
        Packet packetD2 = new Packet(headerD2, payloadD2);
        tcpSocket.sendPacket(packetD2);
        tcpSocket.close();
    }

    private void checkHeader(Header header, int pSecret) {
      if (header.pSecret != pSecret) {
        throw new IllegalStateException("incorrect pSecret");
      }
      if (header.step != 1) {
        throw new IllegalStateException("incorrect step");
      }
    }

    private void print(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }

    private void printError(String message) {
        print(inPacket.getAddress() + " (" + inPacket.getPort() + "): " + message);
    }
}
