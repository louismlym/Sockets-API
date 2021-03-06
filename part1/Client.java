package part1;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class Client {

    private static final String HOSTNAME = "attu2.cs.washington.edu";
    private static final String LOCALHOST = "localhost";
    private static final short STEP = 1;
    private static final short STUDENT_ID = 736;

    public static void main(String[] args) throws IOException {
        Packet packetA = partA();
        Packet packetB = partB(packetA);

        // part c1: opens a TCP connection to the server
        B2ServerPayload payloadB2 = (B2ServerPayload) packetB.getPayload();
        TCPClient client = new TCPClient(HOSTNAME, payloadB2.tcpPort);

        Packet packetC = partC(packetB, client);
        Packet packetD = partD(packetC, client);

        printSummary(packetA, packetB, packetC, packetD);
    }

    private static Packet partA() throws IOException {
        System.out.println("=======================Part A=======================");
        UDPClient client = new UDPClient(HOSTNAME, 12235);

        Payload payloadA1 = new A1ClientPayload("hello world");
        Header headerA1 = new Header(payloadA1.getLength(), 0, STEP, STUDENT_ID);
        Packet packetA1 = new Packet(headerA1, payloadA1);

        // part a1: sends a single UDP packet containing the string "hello world"
        client.sendPacket(packetA1);

        // part a2: receives a UDP packet from the server containing num, len, udp_port, secretA
        Packet packetA2 = client.receivePacket(Header.SIZE + 16, new PayloadFactory.A2ServerPayloadFactory());
        client.close();
        return packetA2;
    }

    private static Packet partB(Packet packetA) throws IOException {
        System.out.println("=======================Part B=======================");
        A2ServerPayload payloadA2 = (A2ServerPayload) packetA.getPayload();
        UDPClient client = new UDPClient(HOSTNAME, payloadA2.udpPort);

        // part b1: send payloadA2.num packets which each packet's payload contains packet_id
        //          and payload of length payloadA2.len (all set to 0).
        //          receive an acknowledgement from the server which a payload containing
        //          acked_packet_id
        for (int packetId = 0; packetId < payloadA2.num; packetId++) {
            Payload payloadB1 = new B1ClientPayload(packetId, payloadA2.len);
            Header headerB1 = new Header(payloadB1.getLength(), payloadA2.secretA, STEP, STUDENT_ID);
            Packet packetB1 = new Packet(headerB1, payloadB1);

            while (true) {
                Packet receivedPacket;
                client.sendPacket(packetB1);
                try {
                    receivedPacket = client.receivePacket(Header.SIZE + 4,
                            500,
                            new PayloadFactory.B1ServerPayloadFactory());
                } catch (SocketTimeoutException ex) {
                    continue;
                }

                B1ServerPayload receivedPayload = (B1ServerPayload) receivedPacket.getPayload();
                if (receivedPayload.ackedPacketId == packetId) {
                    break;
                }
            }
        }

        // part b2: receives a UDP packet containing two integers: tcp_port, secretB
        Packet packetB2 = client.receivePacket(Header.SIZE + 8, new PayloadFactory.B2ServerPayloadFactory());
        client.close();
        return packetB2;
    }

    private static Packet partC(Packet packetB, TCPClient client) throws IOException {
        System.out.println("=======================Part C=======================");
        // part c2: receives a packet from the TCP server containing num2, len2, secretC, c
        Packet packetC2 = client.receivePacket(Header.SIZE + 16, new PayloadFactory.C2ServerPayloadFactory());

        return packetC2;
    }

    private static Packet partD(Packet packetC, TCPClient client) throws IOException {
        System.out.println("=======================Part D=======================");
        C2ServerPayload payloadC2 = (C2ServerPayload) packetC.getPayload();

        // part d1: sends num2 payloads of length payloadC2.len2 filled with payloadC2.c
        //          to the server
        for (int i = 0; i < payloadC2.num2; i++) {
            Payload payloadD1 = new D1ClientPayload(payloadC2.c, payloadC2.len2);
            Header headerD1 = new Header(payloadD1.getLength(), payloadC2.secretC, STEP, STUDENT_ID);
            Packet packetD1 = new Packet(headerD1, payloadD1);

            client.sendPacket(packetD1);
        }

        Packet receivedPacket = client.receivePacket(Header.SIZE + 4, new PayloadFactory.D2ServerPayloadFactory());
        client.close();
        return receivedPacket;
    }

    private static void printSummary(Packet packetA, Packet packetB, Packet packetC, Packet packetD) {
        System.out.println("----------------------------------------------------");
        System.out.println("Summary of packets received from each part");
        System.out.println("A: " + packetA);
        System.out.println("B: " + packetB);
        System.out.println("C: " + packetC);
        System.out.println("D: " + packetD);
    }
}