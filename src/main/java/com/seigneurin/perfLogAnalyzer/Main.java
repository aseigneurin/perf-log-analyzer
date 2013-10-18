package com.seigneurin.perfLogAnalyzer;

public class Main {

    public static void main(String[] args) throws Exception {

        String file = null;
        String format = "yyyy-MM-dd kk:mm:ss,S";
        long thresholdMillis = 10 * 1000;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--format".equals(arg) || "-f".equals(arg)) {
                if (args.length < i + 1)
                    printUsageAndExit("Invalid format option.");
                format = args[++i];
            } else if ("--threshold".equals(arg) || "-t".equals(arg)) {
                if (args.length < i + 1)
                    printUsageAndExit("Invalid threshold option.");
                thresholdMillis = Long.parseLong(args[++i]) * 1000;
            } else {
                if (file != null)
                    printUsageAndExit("Extra argument: " + args[i]);
                file = args[i];
            }
        }

        if (file == null)
            printUsageAndExit("Missing file argument.");

        Analyzer analyzer = new Analyzer(format, thresholdMillis);
        analyzer.process(file);
    }

    private static void printUsageAndExit(String reason) {
        if (reason != null) {
            System.out.println(reason);
            System.out.println();
        }

        System.out.println("Usage: ... [OPTION]... [FILE]");
        System.out.println("  -f, --format       Date-time format. Defaults to 'yyyy-MM-dd kk:mm:ss,S'.");
        System.out.println("  -t, --threshold    Threshold in seconds. Defaults to 10 seconds.");
        System.exit(1);
    }
}
