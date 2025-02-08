

支持多轮对话

使用示例：src/main/java/com/tothefor/deepseekjava/demo.java
```java
public class demo {
    public static void main(String[] args) {
        DeepSeekClient deepSeekClient = new DeepSeekClient("http://localhost:11434/api/chat","deepseek-coder-v2",60);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(">>>> ");
            String input = scanner.nextLine();
            String reply = deepSeekClient.reply(input);
            System.out.println("Bot: " + reply);
        }
    }
}
```