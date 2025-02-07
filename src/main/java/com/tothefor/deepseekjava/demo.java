package com.tothefor.deepseekjava;

import com.tothefor.deepseekjava.client.DeepSeekClient;

import java.util.Scanner;

/**
 * @author DragonOne
 * @since 2025/2/7 11:06
 */
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
