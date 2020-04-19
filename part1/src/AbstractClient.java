import java.io.IOException;
import java.net.SocketOptions;

/**
 * AbstractClient is an abstract implementation of the client wrapper.
 * It aids the client to send or receive packet to a specific host.
 */
public abstract class AbstractClient {
    protected String host;
    protected int port;

    /**
     * Create and open the client connection to the server
     *
     * @param host the host or the server that the client wants to make a connection
     * @param port the port on the host that the client wants to connect
     * @throws IOException
     */
    public AbstractClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        open();
    }

    /**
     * Open a connection to the server
     * @throws IOException
     */
    public abstract void open() throws IOException;

    /**
     * Send a packet to the server
     *
     * @param packet the array of bytes containing a packet to send
     * @param length the length of @{packet} in bytes
     * @throws IOException
     */
    public abstract void sendPacket(byte[] packet, int length) throws IOException;

    public Packet receivePacket(int readLen, PayloadFactory payloadFactory) throws IOException {
        return receivePacket(readLen, SocketOptions.SO_TIMEOUT, payloadFactory);
    }

    /**
     * Receive a packet from the server
     *
     * @param readLen the length of packet that the client wants to read
     * @param timeout the maximum time wait for packet to be received (in milliseconds)
     * @param payloadFactory the payload factory which helps create a new payload of desired type
     * @return a Packet received from the server
     * @throws IOException
     */
    public Packet receivePacket(int readLen, int timeout, PayloadFactory payloadFactory) throws IOException {
        return new Packet(receiveBytes(readLen, timeout), payloadFactory);
    }

    public byte[] receiveBytes(int readLen) throws IOException {
        return receiveBytes(readLen, SocketOptions.SO_TIMEOUT);
    }

    /**
     * Receive a packet from the server as an array of bytes
     *
     * @param readLen the length of packet that the client wants to read
     * @param timeout the maximum time wait for packet to be received (in milliseconds)
     * @return an array of bytes representing a packet received from the server
     * @throws IOException
     */
    public abstract byte[] receiveBytes(int readLen, int timeout) throws IOException;

    /**
     * Close a connection to the server
     * @throws IOException
     */
    public abstract void close() throws IOException;
}
