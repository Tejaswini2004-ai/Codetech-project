import java.io.*;
import java.util.Scanner;

public class FileHandler {

    public static void writeFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            System.out.println("✅ File written successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error writing file: " + e.getMessage());
        }
    }

    public static void readFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            System.out.println("📄 File Contents:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
    }

    public static void modifyFile(String filename, String newText) {
        try {
            writeFile(filename, newText);
            System.out.println("✅ File modified (overwritten).");
        } catch (Exception e) {
            System.out.println("❌ Modification failed: " + e.getMessage());
        }
    }

    public static void appendToFile(String filename, String textToAppend) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(textToAppend);
            writer.newLine();
            System.out.println("✅ Text appended successfully.");
        } catch (IOException e) {
            System.out.println("❌ Error appending to file: " + e.getMessage());
        }
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("🗑️ File deleted successfully.");
            } else {
                System.out.println("❌ File deletion failed.");
            }
        } else {
            System.out.println("❌ File not found.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String filename = "sample.txt";
        int choice;

        do {
            System.out.println("\n🔧 File Handling Utility");
            System.out.println("1. Write to File");
            System.out.println("2. Read File");
            System.out.println("3. Modify (Overwrite) File");
            System.out.println("4. Append to File");
            System.out.println("5. Delete File");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter text to write: ");
                    writeFile(filename, sc.nextLine());
                    break;

                case 2:
                    readFile(filename);
                    break;

                case 3:
                    System.out.print("Enter new text (overwrite): ");
                    modifyFile(filename, sc.nextLine());
                    break;

                case 4:
                    System.out.print("Enter text to append: ");
                    appendToFile(filename, sc.nextLine());
                    break;

                case 5:
                    deleteFile(filename);
                    break;

                case 6:
                    System.out.println("👋 Exiting... Thank you!");
                    break;

                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
        } while (choice != 6);

        sc.close();
    }
}
