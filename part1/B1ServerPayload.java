package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class B1ServerPayload extends Payload {
    public int ackedPacketId;

    public B1ServerPayload(int ackedPacketId) {
        this.ackedPacketId = ackedPacketId;

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(ackedPacketId);

        this.payload = buffer.array();
        this.payloadLen = buffer.position();
    }

    public B1ServerPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        ackedPacketId = buffer.getInt();
    }

    @Override
    public String toString() {
        return "B1ServerPayload{" +
                "ackedPacketId=" + ackedPacketId +
                '}';
    }
}
