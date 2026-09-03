import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class CaesarClient extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new CaesarClient();
    }

    JTextArea textArea = new JTextArea(8, 30);
    JTextField inputField = new JTextField(20);
    JButton button = new JButton("暗号化して送信");

    Socket socket;
    PrintWriter writer;
    BufferedReader reader;
    int key;

    CaesarClient() {
        setTitle("シーザー暗号 送信クライアント");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);

        JPanel panel = new JPanel();
        panel.add(new JLabel("平文:"));
        panel.add(inputField);
        panel.add(button);
        getContentPane().add(BorderLayout.SOUTH, panel);

        button.addActionListener(this);

        setSize(450, 250);
        setVisible(true);

        try {
            socket = new Socket("127.0.0.1", 5000);
            textArea.append("サーバーに接続しました。\r\n");

            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

            key = Integer.parseInt(reader.readLine());
            textArea.append("サーバーから鍵を受信しました: " + key + "\r\n");

        } catch (IOException e) {
            textArea.append("エラー: " + e + "\r\n");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String message = inputField.getText();
            String encrypted = encrypt(message, key);
            textArea.append("平文        : " + message + "\r\n");
            textArea.append("暗号化した文字列: " + encrypted + "\r\n");

            writer.println(encrypted);
            textArea.append("暗号文をサーバーに送信しました。\r\n");

            reader.close();
            writer.close();
            socket.close();
            textArea.append("通信を終了しました。\r\n");

            button.setEnabled(false);

        } catch (IOException e) {
            textArea.append("エラー: " + e + "\r\n");
        }
    }

    static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'a' && c <= 'z') {
                result.append((char) ((c - 'a' + key) % 26 + 'a'));
            } else if (c >= 'A' && c <= 'Z') {
                result.append((char) ((c - 'A' + key) % 26 + 'A'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
