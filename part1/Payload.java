package part1;

import java.nio.ByteBuffer;

/**
 * Payload is an abstract implementation of the payload that lives inside
 * a Packet. Payload may contains different content depending on the design
 * from client or server. The underlying structures of Payload is just an
 * array of bytes. See @code{PayloadFactory} on how to create a new Payload
 * of desired Payload type.
 */
public abstract class Payload {
    protected byte[] payload;
    protected int payloadLen;

    public Payload() {
        payload = null;
        payloadLen = 0;
    }

    /**
     * Initialize header from @code{buffer}
     *
     * @param buffer the ByteBuffer that contains payload's content.
     *               It requires that buffer.position() is at the
     *               beginning of payload content.
     * @param payloadLen the length of this payload in bytes.
     */
    public Payload(ByteBuffer buffer, int payloadLen) {
        this.payloadLen = payloadLen;
        buildPayload(buffer);
    }

    /**
     * Build a payload from the given @code{buffer}. Different type of Payload
     * has its own unique structures of the payload. Making this abstract
     * will help @code{Packet} to build a payload generically.
     *
     * @param buffer the ByteBuffer that contains payload's content. It requires
     *               that buffer.position() is at the beginning of payload content.
     */
    protected abstract void buildPayload(ByteBuffer buffer);

    /**
     * @return the length of the payload
     */
    public int getLength() {
        return payloadLen;
    }

    /**
     * @return the payload as an array of bytes
     */
    public byte[] asBytes() {
        return payload;
    }

    /**
     * @return a String representing this Payload class
     */
    public abstract String toString();
}
