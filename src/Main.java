import java.util.Scanner;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskList taskList = new TaskList();
        int id = 0;
        Scanner input = new Scanner(System.in);

        while (true) {  // Added loop to keep program running
            System.out.print("Enter command: ");
            String fullCommand = input.nextLine().trim();  // Read entire line

            // Split command into parts
            String[] parts = fullCommand.split(" ", 2);  // Split into max 2 parts
            String command = parts[0].toLowerCase();  // First word is command

            switch (command) {
                case "add":
                    if (parts.length < 2) {
                        System.out.println("Error: Missing task description");
                        break;
                    }
                    String description = parts[1];  // Rest is description
                    LocalDateTime now = LocalDateTime.now();
                    Task task = new Task(
                            ++id,  // Pre-increment to avoid needing +=
                            description,
                            now,
                            now
                    );
                    taskList.addTask(task);
                    System.out.println("Added: " + task.toString());
                    break;

                case "update":
                    System.out.println("task updated");
                    break;  // Added missing break

                case "delete":
                    System.out.println("task deleted");
                    break;  // Added missing break

                default:
                    System.out.println("Unknown command: " + command);
            }

            System.out.println("Current tasks: " + taskList.getAllTasks().toString());
        }
    }
}