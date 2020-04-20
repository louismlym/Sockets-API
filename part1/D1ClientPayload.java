package part1;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class D1ClientPayload extends Payload {
    public byte c;
    public int len2;

    public D1ClientPayload(byte c, int len2) {
        this.c = c;
        this.len2 = len2;
        this.payloadLen = len2;

        this.payload = new byte[len2];
        Arrays.fill(this.payload, c);
    }

    public D1ClientPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        len2 = payloadLen;
        c = payload[0];
        buffer.position(buffer.position() + payloadLen);

        for (int i = 1; i < payloadLen; i++) {
            if (payload[i] != payload[i - 1]) {
                throw new IllegalStateException("bytes in payload were not equal");
            }
        }
    }

    @Override
    public String toString() {
        return "D1ClientPayload{" +
                "c=" + c +
                ", len2=" + len2 +
                '}';
    }
}
