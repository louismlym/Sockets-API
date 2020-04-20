package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class C2ServerPayload extends Payload {
    public int num2;
    public int len2;
    public int secretC;
    public byte c;

    public C2ServerPayload(int num2, int len2, int secretC, byte c) {
        this.num2 = num2;
        this.len2 = len2;
        this.secretC = secretC;
        this.c = c;

        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(num2);
        buffer.putInt(len2);
        buffer.putInt(secretC);
        buffer.putInt(c);

        this.payload = buffer.array();
        this.payloadLen = buffer.position();
    }

    public C2ServerPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        num2 = buffer.getInt();
        len2 = buffer.getInt();
        secretC = buffer.getInt();
        c = buffer.get();
    }

    @Override
    public String toString() {
        return "C2ServerPayload{" +
                "num2=" + num2 +
                ", len2=" + len2 +
                ", secretC=" + secretC +
                ", c=" + c +
                '}';
    }
}
