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
     * Initialize header from @code{buffer}. Every subclasses that implements
     * a constructor with @code{buffer} and @{payloadLen} as paremeters must
     * call super(buffer, payloadLen)
     *
     * @param buffer the ByteBuffer that contains payload's content.
     *               It requires that buffer.position() is at the
     *               beginning of payload content.
     * @param payloadLen the length of this payload in bytes.
     * @throws IllegalStateException
     */
    public Payload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
        this.payloadLen = payloadLen;
        this.payload = new byte[payloadLen];

        // save payload as array of bytes
        int curPosition = buffer.position();
        buffer.get(this.payload, 0, this.payloadLen);
        buffer.position(curPosition);

        // buildPayload will make sure that each field is initialized
        // into this Payload
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
