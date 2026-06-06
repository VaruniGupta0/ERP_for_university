package edu.univ.erp.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CSVUtil {

    private static final String CSV_SEPARATOR = ",";
    private static final String CSV_QUOTE = "\"";

    public static boolean exportToCSV(String filePath, String[] headers, Object[][] data) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {


            StringBuilder headerLine = new StringBuilder();
            for (int i = 0; i < headers.length; i++) {
                if (i > 0) headerLine.append(CSV_SEPARATOR);
                headerLine.append(escapeCSVField(headers[i]));
            }
            writer.println(headerLine.toString());


            for (Object[] row : data) {
                StringBuilder dataLine = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) dataLine.append(CSV_SEPARATOR);
                    dataLine.append(escapeCSVField(String.valueOf(row[i])));
                }
                writer.println(dataLine.toString());
            }

            writer.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<List<String>> importFromCSV(String filePath) {
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> row = parseCSVLine(line);
                data.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }


    private static List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {

                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {

                    current.append('"');
                    i++;
                } else {

                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {

                fields.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }


        fields.add(current.toString().trim());

        return fields;
    }

    private static String escapeCSVField(String field) {
        if (field == null) return "";


        if (field.contains(CSV_SEPARATOR) || field.contains(CSV_QUOTE) || field.contains("\n")) {
            return CSV_QUOTE + field.replace(CSV_QUOTE, CSV_QUOTE + CSV_QUOTE) + CSV_QUOTE;
        }

        return field;
    }


    public static List<String> getColumn(List<List<String>> data, int columnIndex) {
        List<String> column = new ArrayList<>();

        if (data.isEmpty()) return column;


        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (columnIndex < row.size()) {
                column.add(row.get(columnIndex));
            }
        }

        return column;
    }


    public static List<String> getHeaders(List<List<String>> data) {
        if (data.isEmpty()) return new ArrayList<>();
        return data.get(0);
    }

    public static List<List<String>> getDataRows(List<List<String>> data) {
        if (data.size() <= 1) return new ArrayList<>();
        return data.subList(1, data.size());
    }
}
