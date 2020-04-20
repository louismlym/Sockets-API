package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Header is an implementation of the header of the packet.
 * It contains information of the packet.
 */
public class Header {
    // size of header in bytes
    public static final int SIZE = 12;

    public int payloadLen;
    public int pSecret;
    public short step;
    public short studentId;

    /**
     * Initialize fields to create a header
     *
     * @param payloadLen the length of payload (in bytes)
     * @param pSecret the psecret
     * @param step  the step number
     * @param studentId the last 3 digits of students number
     */
    public Header(int payloadLen, int pSecret, short step, short studentId) {
        this.payloadLen = payloadLen;
        this.pSecret = pSecret;
        this.step = step;
        this.studentId = studentId;
    }

    /**
     * Initialize header from @code{buffer}
     *
     * @param buffer the ByteBuffer that contains header's content.
     *               It requires that buffer.position() is at the
     *               beginning of header content.
     */
    public Header(ByteBuffer buffer) {
        this.payloadLen = buffer.getInt();
        this.pSecret = buffer.getInt();
        this.step = buffer.getShort();
        this.studentId = buffer.getShort();
    }

    /**
     * @return the length of payload that the packet contains (in bytes)
     */
    public int getPayloadLen() {
        return payloadLen;
    }

    /**
     * @return the header as an array of bytes
     */
    public byte[] asBytes() {
        // in bytes
        int bufferSize = SIZE;

        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        buffer.order(ByteOrder.BIG_ENDIAN); // ensure big-endianness

        buffer.putInt(payloadLen);
        buffer.putInt(pSecret);
        buffer.putShort(step);
        buffer.putShort(studentId);
        return buffer.array();
    }

    @Override
    public String toString() {
        return "Header{" +
                "payloadLen=" + payloadLen +
                ", pSecret=" + pSecret +
                ", step=" + step +
                ", studentId=" + studentId +
                '}';
    }
}
