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

    @Override
    protected void buildPayload(ByteBuffer buffer) { }

    @Override
    public String toString() {
        return "D1ClientPayload{" +
                "c=" + c +
                ", len2=" + len2 +
                '}';
    }
}
