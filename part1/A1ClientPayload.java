package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class A1ClientPayload extends Payload {
    private static final String EXPECTED_STR = "hello world";
    public String str;

    public A1ClientPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
        super(buffer, payloadLen);
    }

    public A1ClientPayload(String str) {
        this.str = str;

        ByteBuffer buffer = ByteBuffer.allocate(str.length() + 1);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put((str + '\0').getBytes(StandardCharsets.US_ASCII));

        this.payloadLen = buffer.position();
        this.payload = buffer.array();
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) throws IllegalStateException {
        byte[] bytes = new byte[this.payloadLen];
        buffer.get(bytes, 0, this.payloadLen);
        if (bytes[bytes.length - 1] != (byte) 0) {
            throw new IllegalStateException("string did not end with '\\0'");
        }
        str = new String(bytes, 0, this.payloadLen - 1, StandardCharsets.US_ASCII);

        if (!str.equals(EXPECTED_STR)) {
            throw new IllegalStateException("the client sent \"" + str
                    + "\" instead of \"" + EXPECTED_STR + "\"");
        }
    }

    @Override
    public String toString() {
        return "A1ClientPayload{" +
                "str='" + str + '\'' +
                '}';
    }
}
