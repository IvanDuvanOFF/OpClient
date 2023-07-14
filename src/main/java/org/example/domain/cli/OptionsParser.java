package org.example.domain.cli;

import org.apache.commons.cli.*;

public class OptionsParser {
    private String connectionString;
    private String rootNodeId;
    private String nodeId;
    private final Options options;

    public OptionsParser() {
        options = new Options();

        options.addOption("u", "url", true, "Connection string to server");
        options.addOption("r", "root", true, "Root node id. If there is none, then using Identifiers.Server");
        options.addOption("n", "node", true, "Node id");
        options.addOption("h", "help", false, "Print this help message");
    }

    public boolean parse(String[] args) {
        try {
            CommandLine cmd = (new DefaultParser()).parse(options, args);

            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("myapp", options);
                return false;
            }

            if (!cmd.hasOption("url"))
                return false;

            connectionString = cmd.getOptionValue("url");
            rootNodeId = cmd.getOptionValue("root");
            nodeId = cmd.getOptionValue("node");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getRootNodeId() {
        return rootNodeId;
    }

    public String getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return "OptionsParser{" +
                "connectionString='" + connectionString + '\'' +
                ", rootNodeId='" + rootNodeId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", options=" + options +
                '}';
    }
}
