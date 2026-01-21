import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {

    private static final Map<Role, List<ClientHandler>> clients = new HashMap<>();

    public static void main(String[] args) throws IOException {

        for (Role role : Role.values()) {
            clients.put(role, new ArrayList<>());
        }

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started on port 5000");

        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket).start();
        }
    }

    static class ClientHandler extends Thread {

        private Socket socket;
        private Role role;
        private BufferedReader in;
        private PrintWriter out;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Step 1: Read role
                role = Role.valueOf(in.readLine().toUpperCase());
                clients.get(role).add(this);

                System.out.println("Client connected as " + role);
                out.println("Connected as " + role);

                String message;
                while ((message = in.readLine()) != null) {
                    handleMessage(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handleMessage(String message) {
            System.out.println("[" + role + "]: " + message);

            // Example routing logic
            if (role == Role.ADMIN) {
                broadcastToAll("[ADMIN]: " + message);
            } else {
                sendToAdmins("[" + role + "]: " + message);
            }
        }

        private void sendToAdmins(String message) {
            for (ClientHandler admin : clients.get(Role.ADMIN)) {
                admin.out.println(message);
            }
        }

        private void broadcastToAll(String message) {
            for (List<ClientHandler> list : clients.values()) {
                for (ClientHandler client : list) {
                    client.out.println(message);
                }
            }
        }
    }
}
