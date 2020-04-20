package part2;

import java.io.IOException;
import java.net.*;

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

    public TCPServerSocket accept() throws IOException {
        return new TCPServerSocket(this.socket.accept());
    }

    public void close() throws IOException {
        socket.close();
    }
}
