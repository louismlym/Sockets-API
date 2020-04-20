package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class B2ServerPayload extends Payload {
    public int tcpPort;
    public int secretB;

    public B2ServerPayload(int tcpPort, int secretB) {
        this.tcpPort = tcpPort;
        this.secretB = secretB;

        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(tcpPort);
        buffer.putInt(secretB);

        this.payload = buffer.array();
        this.payloadLen = buffer.position();
    }

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
