package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class B1ClientPayload extends Payload {
    public int packetId;
    public byte[] zeros;

    public B1ClientPayload(int packetId, int len) {
        this.packetId = packetId;
        this.zeros = new byte[len];

        ByteBuffer buffer = ByteBuffer.allocate(len + 4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(packetId);
        buffer.put(this.zeros);

        this.payloadLen = buffer.position();
        this.payload = buffer.array();
    }

    public B1ClientPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) throws IllegalStateException {
        packetId = buffer.getInt();
        zeros = new byte[payloadLen - 4];
        buffer.get(zeros, 0, payloadLen - 4);
        for (byte zero : zeros) {
            if (zero != (byte) 0) {
                throw new IllegalStateException("byte in payload wasn't set to 0");
            }
        }
    }

    @Override
    public String toString() {
        return "B1ClientPayload{" +
                "packetId=" + packetId +
                ", zeros.length=" + zeros.length +
                '}';
    }
}
