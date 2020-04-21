package part2;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * This server will wait to receive a packet from the client.
 * Once the client has communicated with this server, it will
 * assign a new thread to handle that client.
 *
 * See {@code ClientHandler} for more details.
 */
public class Server {
    private static final int PORT = 12235;

    public static void main(String[] args) throws IOException {
        try(UDPServer server = new UDPServer(PORT)) {
            System.out.println("The server starts listening on port " + PORT);
            while (true) {
                DatagramPacket datagramPacket = server.receive();
                System.out.println("New client: " + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
                (new Thread(new ClientHandler(server, datagramPacket))).start();
            }
        }
    }
}
