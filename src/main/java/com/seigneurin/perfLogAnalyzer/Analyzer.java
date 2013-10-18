package com.seigneurin.perfLogAnalyzer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Analyzer {

    private Calendar calendar;
    private String indent = "    ";

    private DateFormat dateFormat;
    private long thresholdMillis;

    private String fullLineA;
    private String fullLineB;
    private Date dateA;
    private Date dateB;

    protected Analyzer() {
        calendar = Calendar.getInstance();
    }

    public Analyzer(String dateFormat, long thresholdMillis) {
        this();
        this.dateFormat = new SimpleDateFormat(dateFormat);
        this.thresholdMillis = thresholdMillis;
    }

    public void process(String file) throws Exception {

        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {

            if (fullLineA == null && line.startsWith("\uFEFF"))
                line = line.substring(1);

            Date date = tryParseDate(line);

            if (date != null) {
                // new log line
                // move B to A
                fullLineA = indent + fullLineB;
                dateA = dateB;
                // keep the new log line (B)
                fullLineB = line;
                dateB = date;
                // check if the line A is of interest
                if (dateA != null) {
                    check();
                }

            } else {
                if (dateB == null) {
                    // leading line, do nothing
                } else {
                    // continuing log line
                    fullLineB += "\n" + indent + line;
                }
            }
        }

        bufferedReader.close();
    }

    private Date tryParseDate(String line) {
        ParsePosition pos = new ParsePosition(0);
        Date res = (Date) dateFormat.parseObject(line, pos);
        return res;
    }

    private void check() {
        calendar.setTime(dateA);
        long millisA = calendar.getTimeInMillis();

        calendar.setTime(dateB);
        long millisB = calendar.getTimeInMillis();

        long diff = millisB - millisA;
        if (diff >= thresholdMillis) {
            // System.out.println("### Took " + diff + " milliseconds to execute");
            System.out.println("### Took " + diff / 1000 + " seconds to execute");
            System.out.println(fullLineA);
            System.out.println();
        }
    }

}
