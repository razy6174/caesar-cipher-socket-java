import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("CaesarClient.java");
            BufferedReader br = new BufferedReader(fr);

            FileWriter fw = new FileWriter("Client_output.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            String s;
            int lineNumber = 1;
            
            char openBrace = 0x7B;
            char closeBrace = 0x7D;

            while ((s = br.readLine()) != null) {
                boolean hasOpen = s.indexOf(openBrace) != -1;
                boolean hasClose = s.indexOf(closeBrace) != -1;
                
                String symbol = "  ";
                if (hasOpen && hasClose) {
                    symbol = "* ";
                } else if (hasOpen) {
                    symbol = "+ ";
                } else if (hasClose) {
                    symbol = "- ";
                }
                
                // 教科書の模範解答にならい、最後に %n を入れて改行も一緒にフォーマットする
                String outLine = String.format("%04d:%s%s%n", lineNumber, symbol, s);
                
                // 既に改行コードが含まれているので、printlnではなくprintを使う
                System.out.print(outLine);
                
                bw.write(outLine); // newLine() は不要に！
                
                lineNumber++; // 模範解答の(3)と全く同じ形
            }
            
            br.close();
            bw.close();
            
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}