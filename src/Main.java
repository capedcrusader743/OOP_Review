import java.util.Scanner;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskList taskList = new TaskList();
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
                            java.util.UUID.randomUUID().toString(),
                            description,
                            now,
                            now
                    );
                    taskList.addTask(task);
                    System.out.println("Added: " + task.toString());
                    break;

                case "update":
                    String[] updateParts = parts[1].split(" ", 2); // Split ID and description
                    if (updateParts.length < 2) {
                        System.out.println("Usage: update <task-id> <new-description>");
                        break;
                    }
                    taskList.updateTask(updateParts[0], updateParts[1]);
                    break;

                case "delete":
                    String[] deleteParts = parts[1].split(" ", 2);
                    taskList.deleteTask(deleteParts[0]);
                    break;  // Added missing break

                case "mark-in-progress":

                    break;

                case "mark-done":
                    break;

                default:
                    System.out.println("Unknown command: " + command);
            }

            System.out.println("Current tasks: " + taskList.getAllTasks().toString());
        }
    }
}