package edu.univ.erp.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileUtil {

    public static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            return null;
        }
    }


    public static boolean writeFile(String filePath, String content) {
        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(content);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("Error writing file: " + filePath);
            return false;
        }
    }


    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return lines;
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public static boolean createDirectory(String dirPath) {
        try {
            Files.createDirectories(Paths.get(dirPath));
            return true;
        } catch (IOException e) {
            System.err.println("Error creating directory: " + dirPath);
            return false;
        }
    }

    public static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }


    public static File chooseBackupFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Backup File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("SQL Backup Files", "sql"));

        File backupDir = new File("backups");
        if (backupDir.exists()) {
            fileChooser.setCurrentDirectory(backupDir);
        }

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }


    public static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        else if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        else if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024.0));
        else return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
    }

    public static String formatDate(Date date) {
        if (date == null) return "Unknown";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static boolean isFileValid(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    public static List<File> getFilesByExtension(String directoryPath, String extension) {
        List<File> files = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] allFiles = directory.listFiles();
            if (allFiles != null) {
                for (File file : allFiles) {
                    if (file.isFile() && getFileExtension(file.getName()).equalsIgnoreCase(extension)) {
                        files.add(file);
                    }
                }
            }
        }

        return files;
    }

    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            return file.delete();
        } catch (Exception e) {
            System.err.println("Error deleting file: " + filePath + " - " + e.getMessage());
            return false;
        }
    }


    public static boolean copyFile(String sourcePath, String destPath) {
        try (InputStream in = new FileInputStream(sourcePath);
             OutputStream out = new FileOutputStream(destPath)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
            return false;
        }
    }

    public static Date getFileCreationDate(File file) {
        try {
            return new Date(file.lastModified());
        } catch (Exception e) {
            System.err.println("Error getting file creation date: " + e.getMessage());
            return new Date();
        }
    }

    public static boolean isDirectoryWritable(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return dir.canWrite();
    }

    public static long getAvailableDiskSpace(String path) {
        File file = new File(path);
        return file.getFreeSpace();
    }

    public static File createTempFile(String content, String prefix, String suffix) {
        try {
            File tempFile = File.createTempFile(prefix, suffix);
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(content);
            }
            return tempFile;
        } catch (IOException e) {
            System.err.println("Error creating temp file: " + e.getMessage());
            return null;
        }
    }

    public static byte[] readFileToByteArray(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading file to byte array: " + filePath);
            return null;
        }
    }

    public static boolean writeByteArrayToFile(String filePath, byte[] data) {
        try {
            Files.write(Paths.get(filePath), data);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing byte array to file: " + filePath);
            return false;
        }
    }
}