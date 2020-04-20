package part1;

import java.nio.ByteBuffer;

/**
 * PayloadFactory is an abstract implementation that acts as a payload
 * creator. It helps Packet know which type of Payload it's containing.
 *
 * The Client needs to pass desired type of PayloadFactory into the Packet in
 * order to build a correct Payload. Types of available Payloads are listed
 * as static inner classes inside the PayloadFactory.
 */
public abstract class PayloadFactory {
    public abstract Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException;

    public static class A1ClientPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new A1ClientPayload(buffer, payloadLen);
        }
    }

    public static class A2ServerPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new A2ServerPayload(buffer, payloadLen);
        }
    }

    public static class B1ClientPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new B1ClientPayload(buffer, payloadLen);
        }
    }

    public static class B1ServerPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new B1ServerPayload(buffer, payloadLen);
        }
    }

    public static class B2ServerPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new B2ServerPayload(buffer, payloadLen);
        }
    }

    public static class C2ServerPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new C2ServerPayload(buffer, payloadLen);
        }
    }

    public static class D1ClientPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new D1ClientPayload(buffer, payloadLen);
        }
    }

    public static class D2ServerPayloadFactory extends PayloadFactory {
        @Override
        public Payload createPayload(ByteBuffer buffer, int payloadLen) throws IllegalStateException {
            return new D2ServerPayload(buffer, payloadLen);
        }
    }
}
