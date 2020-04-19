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

    /**
     * @param header the Header representing this packet
     * @param payload the Payload represneting this packet
     */
    public Packet(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    /**
     * @param bytes the array of bytes representing this packet
     * @param payloadFactory the payloadFactory used for creating a
     *                       new Payload. The client has an ability
     *                       to define the type of payload they want
     *                       to create.
     */
    public Packet(byte[] bytes, PayloadFactory payloadFactory) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
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
                "header=" + header.toString() +
                ", payload=" + payload.toString() +
                '}';
    }
}
