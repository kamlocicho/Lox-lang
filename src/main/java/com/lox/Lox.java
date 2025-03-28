package com.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;

    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                System.out.println("Usage: Lox [script]");
                System.exit(64);
            } else if (args.length == 1) {
                runFile(args[0]);
            } else {
                runPrompt();
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error reading from file / console.");
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException {
        var input = new InputStreamReader(System.in);
        var reader = new BufferedReader(input);
        while (true) {
            System.out.print("> ");
            var line = reader.readLine();
            if (line.isBlank()) break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        var scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        tokens.forEach(System.out::println);
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.printf("[line %s] Error %s: %s%n", line, where, message);
        hadError = true;
    }
}