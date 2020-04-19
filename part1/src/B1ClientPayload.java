import java.nio.ByteBuffer;

public class B1ClientPayload extends Payload {
    public int packetId;

    public B1ClientPayload(int packetId, int len) {
        this.payloadLen = len + 4;
        this.packetId = packetId;

        ByteBuffer buffer = ByteBuffer.allocate(payloadLen);
        buffer.putInt(packetId);
        buffer.put(new byte[len]);

        payload = buffer.array();
    }

    public B1ClientPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {

    }

    @Override
    public String toString() {
        return "B1ClientPayload{" +
                "packetId=" + packetId +
                '}';
    }
}
