import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws IOException {
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(10000);
        ds.connect(InetAddress.getByName("localhost"),6667);
        // sent
        byte[] data = "Hello".getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(data,data.length);
        ds.send(packet);
        // receive
        byte[] buffer = new byte[1024];
        packet = new DatagramPacket(buffer,buffer.length);

        ds.receive(packet);
        String resp = new String(packet.getData(),packet.getOffset(),packet.getLength());
        System.out.println(resp);

        ds.disconnect();
    }
}
