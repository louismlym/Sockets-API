package part1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class A1ClientPayload extends Payload {
    public A1ClientPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(("hello world" + '\0').getBytes(StandardCharsets.US_ASCII));
        payloadLen = buffer.position();
        payload = buffer.array();
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) { }

    @Override
    public String toString() {
        return "A1ClientPayload{}";
    }
}
