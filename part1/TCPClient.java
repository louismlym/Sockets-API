package part1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient extends AbstractClient {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public TCPClient(String host, int port) throws IOException {
        super(host, port);
    }

    @Override
    public void open() throws IOException {
        this.socket = new Socket(InetAddress.getByName(host), port);
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    @Override
    public void sendBytes(byte[] bytes, int length) throws IOException {
        outputStream.write(bytes, 0, length);
        outputStream.flush();
    }

    @Override
    public byte[] receiveBytes(int readLen, int timeout) throws IOException {
        byte[] buffer = new byte[readLen];
        socket.setSoTimeout(timeout);
        int offset = 0;
        while (offset < readLen) {
            int byteRead = inputStream.read(buffer, offset, readLen - offset);
            if (byteRead == -1) {
                break;
            }
            offset += byteRead;
        }
        return buffer;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}