package part2;

import part1.Packet;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;

public class ClientHandler implements Runnable {
    private UDPServer server;
    private DatagramPacket inPacket;

    public ClientHandler(UDPServer server, DatagramPacket inPacket) {
        this.server = server;
        this.inPacket = inPacket;
    }

    @Override
    public void run() {
        // Most tasks here

        // a1 parse hello world from inPacket

        Packet packetA1;

        // a2 send num, len, udp_port, secretA
        DatagramPacket a2 = new DatagramPacket(packetA1.asBytes(), packetA1.getLength(),
                inPacket.getAddress(), inPacket.getPort());

        // server.send(DatagramPacket);

        // b1
        try {
            UDPServer serverB1 = new UDPServer(1234);
            serverB1.receive(300);


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
