import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);

        // Step 1: Send role
        System.out.print("Enter role (ADMIN / SELLER / BROKER): ");
        String role = scanner.nextLine();
        out.println(role);

        // Read messages from server
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Send messages
        while (true) {
            String message = scanner.nextLine();
            out.println(message);
        }
    }
}
