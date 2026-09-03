import java.io.*;
import java.net.*;
import java.security.SecureRandom;

public class CaesarServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("[サーバー] 起動しました。クライアントの接続を待っています...");

            Socket socket = serverSocket.accept();
            System.out.println("[サーバー] クライアントが接続しました。");

            PrintWriter writer =
                new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

            SecureRandom random = new SecureRandom();
            int key = random.nextInt(25) + 1;
            System.out.println("[サーバー] 生成した鍵（ずらす数）: " + key);

            writer.println(key);
            System.out.println("[サーバー] 鍵をクライアントに送信しました。");

            String encrypted = reader.readLine();
            System.out.println("[サーバー] 受信した暗号文: " + encrypted);

            String decrypted = decrypt(encrypted, key);
            System.out.println("[サーバー] 復号した平文  : " + decrypted);

            reader.close();
            writer.close();
            socket.close();
            serverSocket.close();
            System.out.println("[サーバー] 通信を終了しました。");

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    static String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'a' && c <= 'z') {
                result.append((char) ((c - 'a' - key + 26) % 26 + 'a'));
            } else if (c >= 'A' && c <= 'Z') {
                result.append((char) ((c - 'A' - key + 26) % 26 + 'A'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
