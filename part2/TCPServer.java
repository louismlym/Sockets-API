package part2;

import java.io.IOException;
import java.net.*;

/**
 * TCPServer is an implementation of TCP server that is bound to a
 * ServerSocket. It helps the server to receive and send a packet.
 * TCPServer must be closed when finish using.
 */
public class TCPServer {
    private int listenPort;
    private ServerSocket socket;

    /**
     * @param listenPort the local port to bind with
     * @throws SocketException if the socket could not bind to the specified local port.
     */
    public TCPServer(int listenPort) throws IOException {
        this.listenPort = listenPort;
        this.socket = new ServerSocket(listenPort);
    }

    /**
     * Accept the new client and open the new socket
     *
     * @return TCPServerSocket that handles this client
     * @throws IOException
     */
    public TCPServerSocket accept() throws IOException {
        return new TCPServerSocket(this.socket.accept());
    }

    /**
     * Close the TCP socket
     * @throws IOException
     */
    public void close() throws IOException {
        socket.close();
    }
}
