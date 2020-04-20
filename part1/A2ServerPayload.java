package part1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class A2ServerPayload extends Payload {
    public int num;
    public int len;
    public int udpPort;
    public int secretA;

    public A2ServerPayload(int num, int len, int udpPort, int secretA) {
        this.num = num;
        this.len = len;
        this.udpPort = udpPort;
        this.secretA = secretA;

        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(num);
        buffer.putInt(len);
        buffer.putInt(udpPort);
        buffer.putInt(secretA);

        this.payload = buffer.array();
        this.payloadLen = buffer.position();
    }

    public A2ServerPayload(ByteBuffer buffer, int payloadLen) {
        super(buffer, payloadLen);
    }

    @Override
    protected void buildPayload(ByteBuffer buffer) {
        num = buffer.getInt();
        len = buffer.getInt();
        udpPort = buffer.getInt();
        secretA = buffer.getInt();
    }

    @Override
    public String toString() {
        return "A2ServerPayload{" +
                "num=" + num +
                ", len=" + len +
                ", udpPort=" + udpPort +
                ", secretA=" + secretA +
                '}';
    }
}
