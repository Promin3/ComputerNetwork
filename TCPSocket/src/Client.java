import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket sock = new Socket("localhost",6666);
        try(InputStream input = sock.getInputStream()){
            try(OutputStream output = sock.getOutputStream()){
                handle(input,output);
            }
        }
        sock.close();
        System.out.println("disconnected");
    }

    private static void handle(InputStream input, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        System.out.println("[server]"+reader.readLine());
        Scanner sc = new Scanner(System.in);
        for (;;){
            System.out.print(">>>"); // 打印提示
            String s = sc.nextLine();
            writer.write(s);
            writer.newLine();
            writer.flush();
            String response  = reader.readLine();
            System.out.println("<<<" + response);
            if (response.equals("bye")){
                break;
            }
        }
    }
}


