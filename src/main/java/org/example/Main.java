package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.example.chain.Chain;
import org.example.chain.ReadSubsNodeChain;
import org.example.chain.ReadWriteNodesChain;
import org.example.domain.cli.OptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);

        OptionsParser input = new OptionsParser();
        if (!input.parse(args)) {
            logger.warn("No connection string has benn specified... ");
            return;
        }

        try {
            new Chain(input)
                    .addLink(new ReadWriteNodesChain())
                    .addLink(new ReadSubsNodeChain())
                    .pass();
        } catch (UaException | InterruptedException | ExecutionException | IOException | NullPointerException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }

        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }

    public static void write(String fileName, Object object) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter(fileName, false);
        gson.toJson(object, fileWriter);
        fileWriter.close();
    }
}