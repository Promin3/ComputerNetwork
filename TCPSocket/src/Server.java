import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666);
        System.out.println("server is running");
        for (; ; ) {
            // 注意到代码ss.accept()表示每当有新的客户端连接进来后，
            // 就返回一个Socket实例，这个Socket实例就是用来和刚连接的客户端进行通信的。
            // 由于客户端很多，要实现并发处理，我们就必须为每个新的Socket创建一个新线程来处理，
            // 这样，主线程的作用就是接收新的连接，每当收到新连接后，就创建一个新线程进行处理。
            Socket sock = ss.accept();
            System.out.println("connected form" + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
        }
        try {
            this.sock.close();
        } catch (IOException ioe) {
        }
        System.out.println("client disconnected");
    }



    private void handle(InputStream input, OutputStream output) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("Connect established\n");
        // 如果不调用flush()，我们很可能会发现，客户端和服务器都收不到数据，这并不是Java标准库的设计问题，
        // 而是我们以流的形式写入数据的时候，并不是一写入就立刻发送到网络，而是先写入内存缓冲区，
        // 直到缓冲区满了以后，才会一次性真正发送到网络，这样设计的目的是为了提高传输效率。
        // 如果缓冲区的数据很少，而我们又想强制把这些数据发送到网络，就必须调用flush()强制把缓冲区数据发送出去。
        writer.flush();
        for (; ; ) {
            String s = reader.readLine();
            if (s.equals("bye")) {
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("server received " + s + '\n');
            writer.flush();
        }
    }
}
