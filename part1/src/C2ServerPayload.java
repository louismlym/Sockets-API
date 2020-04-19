import java.nio.ByteBuffer;

public class C2ServerPayload extends Payload {
    public int num2;
    public int len2;
    public int secretC;
    public byte c;

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
