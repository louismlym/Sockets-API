package part1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Packet is an implementation of a packet which encapsulates a Header
 * and a Payload. @code{getLength} and @code{asBytes} ensure that the
 * this Packet always return a representing of Payload in 4-byte aligned.
 */
public class Packet {
    private Header header;
    private Payload payload;
    private InetAddress address;
    private int port;

    /**
     * @param header the Header representing this packet
     * @param payload the Payload represneting this packet
     */
    public Packet(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
        this.address = null;
        this.port = -1;
    }

    /**
     * @param bytes the array of bytes representing this packet
     * @param payloadFactory the payloadFactory used for creating a
     *                       new Payload. The client has an ability
     *                       to define the type of payload they want
     *                       to create.
     */
    public Packet(byte[] bytes, PayloadFactory payloadFactory) throws IllegalStateException {
        this(bytes, 0, bytes.length, payloadFactory);
    }

    /**
     * @param packet the DatagramPacket which contains the content of this Packet
     * @param payloadFactory the payloadFactory used for creating a
     *                       new Payload. The client has an ability
     *                       to define the type of payload they want
     *                       to create.
     * @throws IllegalStateException
     */
    public Packet(DatagramPacket packet, PayloadFactory payloadFactory) throws IllegalStateException {
        this(packet.getData(), packet.getOffset(), packet.getLength(), payloadFactory);
        this.address = packet.getAddress();
        this.port = packet.getPort();
    }

    /**
     * @param bytes the array of bytes representing this packet
     * @param offset the offset that defines the beginning of data
     *               in {@code bytes}
     * @param length the length of actual content stored in {@code bytes}
     * @param payloadFactory the payloadFactory used for creating a
     *                       new Payload. The client has an ability
     *                       to define the type of payload they want
     *                       to create.
     */
    public Packet(byte[] bytes, int offset, int length, PayloadFactory payloadFactory) throws IllegalStateException {
        if (length % 4 != 0) {
            throw new IllegalStateException("Packet is not in 4-byte aligned");
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        header = new Header(buffer);
        payload = payloadFactory.createPayload(buffer, header.getPayloadLen());
    }

    /**
     * @return the Header of this packet
     */
    public Header getHeader() {
        return header;
    }

    /**
     * @return the Payload of this packet
     */
    public Payload getPayload() {
        return payload;
    }

    /**
     * @return the destination address
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     *
     * @return the port of {@code address} that this Packet belongs to
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the length of this packet in bytes. It's guaranteed that the length is
     * in multiple of 4.
     */
    public int getLength() {
        int padding = 0;
        if (payload.getLength() % 4 != 0) {
            padding = 4 - (payload.getLength() % 4);
        }
        return Header.SIZE + payload.getLength() + padding;
    }

    /**
     * @return a packet as an array of bytes. It's guaranteed that the return array is
     * 4-byte aligned.
     */
    public byte[] asBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getLength());
        buffer.put(header.asBytes(), 0, Header.SIZE);
        buffer.put(payload.asBytes(), 0, payload.getLength());
        buffer.put(new byte[getLength() - Header.SIZE - payload.getLength()]);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "Packet{" +
                "header=" + header +
                ", payload=" + payload +
                '}';
    }
}
