package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class A1ClientPayload extends Payload {
    public String str;

    public A1ClientPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    public A1ClientPayload(String str) {
        this.str = str;

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put((str + '\0').getBytes(StandardCharsets.US_ASCII));

        payloadLen = buffer.position();
        payload = buffer.array();
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        byte[] bytes = new byte[this.payloadLen];
        this.str = new String(bytes, StandardCharsets.US_ASCII);
    }

    @Override
    public String toString() {
        return "A1ClientPayload{" +
                "str='" + str + '\'' +
                '}';
    }
}
