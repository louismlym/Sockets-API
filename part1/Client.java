package part1;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class Client {

    public static void main(String[] args) throws IOException {
        Packet packetA = partA();
        Packet packetB = partB(packetA);

        // part c1: opens a TCP connection to the server
        B2ServerPayload payloadB2 = (B2ServerPayload) packetB.getPayload();
        TCPClient client = new TCPClient("attu2.cs.washington.edu", payloadB2.tcpPort);

        Packet packetC = partC(packetB, client);
        Packet packetD = partD(packetC, client);

        printSummary(packetA, packetB, packetC, packetD);
    }

    private static Packet partA() throws IOException {
        System.out.println("=======================Part A=======================");
        UDPClient client = new UDPClient("attu2.cs.washington.edu", 12235);

        Payload payloadA1 = new A1ClientPayload();
        Header headerA1 = new Header(payloadA1.getLength(), 0, (short) 1, (short) 736);
        Packet packetA1 = new Packet(headerA1, payloadA1);

        // part a1: sends a single UDP packet containing the string "hello world"
        printPacket("Sending", packetA1);
        client.sendPacket(packetA1.asBytes(), packetA1.getLength());

        // part a2: receives a UDP packet from the server containing num, len, udp_port, secretA
        Packet packetA2 = client.receivePacket(Header.SIZE + 16, new PayloadFactory.A2ServerPayloadFactory());
        client.close();

        printPacket("Receiving", packetA2);
        return packetA2;
    }

    private static Packet partB(Packet packetA) throws IOException {
        System.out.println("=======================Part B=======================");
        A2ServerPayload payloadA2 = (A2ServerPayload) packetA.getPayload();
        UDPClient client = new UDPClient("attu2.cs.washington.edu", payloadA2.udpPort);

        // part b1: send payloadA2.num packets which each packet's payload contains packet_id
        //          and payload of length payloadA2.len (all set to 0).
        //          receive an acknowledgement from the server which a payload containing
        //          acked_packet_id
        for (int packetId = 0; packetId < payloadA2.num; packetId++) {
            Payload payloadB1 = new B1ClientPayload(packetId, payloadA2.len);
            Header headerB1 = new Header(payloadB1.getLength(), payloadA2.secretA, (short) 1, (short) 736);
            Packet packetB1 = new Packet(headerB1, payloadB1);

            while (true) {
                Packet receivedPacket;
                printPacket("Sending", packetB1);
                client.sendPacket(packetB1.asBytes(), packetB1.getLength());
                try {
                    receivedPacket = client.receivePacket(Header.SIZE + 4,
                            500,
                            new PayloadFactory.B1ServerPayloadFactory());
                } catch (SocketTimeoutException ex) {
                    continue;
                }
                printPacket("Receiving", receivedPacket);

                B1ServerPayload receivedPayload = (B1ServerPayload) receivedPacket.getPayload();
                if (receivedPayload.ackedPacketId == packetId) {
                    break;
                }
            }
        }

        // part b2: receives a UDP packet containing two integers: tcp_port, secretB
        Packet packetB2 = client.receivePacket(Header.SIZE + 8, new PayloadFactory.B2ServerPayloadFactory());
        printPacket("Receiving", packetB2);
        client.close();
        return packetB2;
    }

    private static Packet partC(Packet packetB, TCPClient client) throws IOException {
        System.out.println("=======================Part C=======================");
        // part c2: receives a packet from the TCP server.
        Packet packetC2 = client.receivePacket(Header.SIZE + 16, new PayloadFactory.C2ServerPayloadFactory());
        printPacket("Receiving", packetC2);

        return packetC2;
    }

    private static Packet partD(Packet packetC, TCPClient client) throws IOException {
        System.out.println("=======================Part D=======================");
        C2ServerPayload payloadC2 = (C2ServerPayload) packetC.getPayload();

        // part d1: sends num2 payloads of length payloadC2.len2 filled with payloadC2.c
        //          to the server
        for (int i = 0; i < payloadC2.num2; i++) {
            Payload payloadD1 = new D1ClientPayload(payloadC2.c, payloadC2.len2);
            Header headerD1 = new Header(payloadD1.getLength(), payloadC2.secretC, (short) 1, (short) 736);
            Packet packetD1 = new Packet(headerD1, payloadD1);
            
            printPacket("Sending", packetD1);
            client.sendPacket(packetD1.asBytes(), packetD1.getLength());
        }

        Packet receivedPacket = client.receivePacket(Header.SIZE + 4, new PayloadFactory.D2ServerPayloadFactory());
        printPacket("Receiving", receivedPacket);
        client.close();
        return receivedPacket;
    }

    private static void printSummary(Packet packetA, Packet packetB, Packet packetC, Packet packetD) {
        System.out.println("----------------------------------------------------");
        System.out.println("Summary of packets received from each part");
        System.out.println("A: " + packetA.toString());
        System.out.println("B: " + packetB.toString());
        System.out.println("C: " + packetC.toString());
        System.out.println("D: " + packetD.toString());
    }

    private static void printPacket(String action, Packet packet) {
        if (action.equals("Sending")) {
            System.out.print("--> ");
        } else if (action.equals("Receiving")) {
            System.out.print("<-- ");
        }
        System.out.println(action + " a packet...");
        System.out.println("      " + packet.toString());
    }
}