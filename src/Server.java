import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket server;
    private Socket client;
    private final int porta;
    private final ArrayList<Notizia> notizie;

    public Server() {
        server = null;
        client = null;
        porta = 1234;
        notizie = new ArrayList<Notizia>();
    }
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        @Override
        public void run () {
            try {
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream serverOutput = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String message = clientInput.readLine();
                    try {
                        message = message.replace("[", "").replace("]", "");
                        String[] splitMessage = message.split(", ");
                        notizie.add(new Notizia(splitMessage[0], splitMessage[1], splitMessage[2]));
                    }
                    catch(Exception e) {
                        serverOutput.writeBytes("Notizia non valida\n");
                    }

                    serverOutput.writeBytes("Notizie:\n");
                    for(Notizia n : notizie) {
                        serverOutput.writeBytes(n.toString() + "\n");
                    }
                    serverOutput.writeBytes("Fine\n");
                    serverOutput.flush();
                }
            } catch (IOException e) {
                System.out.println("Comunicazione interrotta con il client.");
            }
        }
    }

    public void attendi() {
        try {
            server = new ServerSocket(porta);
            server.setReuseAddress(true);
            System.out.println("Server in attesa di connessioni.");

            while(true) {
                client = server.accept();
                System.out.println("Nuova connessione: " + client);

                Thread clientHandler = new Thread(new ClientHandler(client));
                clientHandler.start();
            }

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Errore durante l'istanza del server.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.attendi();
    }
}