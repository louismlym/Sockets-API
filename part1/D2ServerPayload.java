package part1;

import java.nio.ByteBuffer;

public class D2ServerPayload extends Payload {
    public int secretD;

    public D2ServerPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        secretD = buffer.getInt();
    }

    @Override
    public String toString() {
        return "D2ServerPayload{" +
                "secretD=" + secretD +
                '}';
    }
}
