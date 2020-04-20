package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class B1ClientPayload extends Payload {
    public int packetId;
    public byte[] zeros;

    public B1ClientPayload(int packetId, int len) {
        this.payloadLen = len + 4;
        this.packetId = packetId;
        this.zeros = new byte[len];

        ByteBuffer buffer = ByteBuffer.allocate(payloadLen);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(packetId);
        buffer.put(this.zeros);

        payload = buffer.array();
    }

    public B1ClientPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        this.packetId = buffer.getInt();
        zeros = new byte[payloadLen - 4];
        buffer.get(zeros, 0, payloadLen - 4);
    }

    @Override
    public String toString() {
        return "B1ClientPayload{" +
                "packetId=" + packetId +
                "zeros.length=" + zeros.length +
                '}';
    }
}
