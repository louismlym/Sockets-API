package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class D2ServerPayload extends Payload {
    public int secretD;

    public D2ServerPayload(int secretD) {
        this.secretD = secretD;

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(secretD);

        this.payload = buffer.array();
        this.payloadLen = buffer.position();
    }

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
