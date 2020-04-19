import java.nio.ByteBuffer;

public class B1ServerPayload extends Payload {
    public int ackedPacketId;

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
