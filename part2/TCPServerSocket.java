package part2;

import part1.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TCPServerSocket {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public TCPServerSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    /**
     * Send a packet to the client
     *
     * @param packet the packet to send
     * @throws IOException
     */
    public void sendPacket(Packet packet) throws IOException {
        System.out.println("[" + Thread.currentThread().getName() + "] --> Sending a packet to "
                + socket.getInetAddress() + " (" + socket.getPort() + ")...\n"
                + "      " + packet);
        sendBytes(packet.asBytes(), packet.getLength());
    }

    /**
     * Receive a packet from the client
     *
     * @param payloadFactory the payload factory which helps create a new payload of desired type
     * @param timeout the maximum time wait for packet to be received (in milliseconds)
     * @return a Packet received from the server
     * @throws IOException
     */
    public Packet receivePacket(PayloadFactory payloadFactory, int timeout) throws IOException {
        ByteBuffer headerBuffer = ByteBuffer.wrap(receiveBytes(Header.SIZE, timeout));
        Header header = new Header(headerBuffer);

        int payloadLen = header.payloadLen;
        int padding = 0;
        if (payloadLen % 4 != 0) {
            padding = 4 - (payloadLen % 4);
        }
        ByteBuffer payloadBuffer = ByteBuffer.wrap(receiveBytes(payloadLen + padding, timeout));
        Payload payload = payloadFactory.createPayload(payloadBuffer, payloadLen);

        Packet packet = new Packet(header, payload);
        System.out.println("[" + Thread.currentThread().getName() + "] <-- Received a packet from "
                + socket.getInetAddress() + " (" + socket.getPort() + ")...\n"
                + "      " + packet);
        return packet;
    }

    public void close() throws IOException {
        socket.close();
    }

    private void sendBytes(byte[] bytes, int length) throws IOException {
        outputStream.write(bytes, 0, length);
        outputStream.flush();
    }

    private byte[] receiveBytes(int readLen, int timeout) throws IOException {
        byte[] buffer = new byte[readLen];
        socket.setSoTimeout(timeout);
        int offset = 0;
        while (offset < readLen) {
            int byteRead = inputStream.read(buffer, offset, readLen - offset);
            if (byteRead == -1) {
                break;
            }
            offset += byteRead;
        }
        return buffer;
    }
}
