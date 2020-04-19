import java.nio.ByteBuffer;

public class B2ServerPayload extends Payload {
    public int tcpPort;
    public int secretB;

    public B2ServerPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        tcpPort = buffer.getInt();
        secretB = buffer.getInt();
    }

    @Override
    public String toString() {
        return "B2ServerPayload{" +
                "tcpPort=" + tcpPort +
                ", secretB=" + secretB +
                '}';
    }
}
