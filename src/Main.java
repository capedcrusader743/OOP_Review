import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Main {

    private static void displayTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.println("\nTasks:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.printf("%d. %s\n", i + 1, task.toString());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        TaskList taskList = new TaskList();
        taskList.loadFromFile(); // Load existing tasks

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            taskList.saveToFile();
            System.out.println("Tasks saved to file.");
        }));

        Scanner input = new Scanner(System.in);

        while (true) {  // Added loop to keep program running
            System.out.print("Enter command: ");
            String fullCommand = input.nextLine().trim();  // Read entire line

            // Split command into parts
            String[] parts = fullCommand.split(" ", 2);  // Split into max 2 parts
            String command = parts[0].toLowerCase();  // First word is command

            switch (command) {

                case "list":
                    if (parts.length == 1) {
                        displayTasks(taskList.getAllTasks());
                    } else {
                        Task.Status status = switch(parts[1].toLowerCase()) {
                            case "todo" -> Task.Status.TODO;
                            case "in-progress" -> Task.Status.IN_PROGRESS;
                            case "done" -> Task.Status.DONE;
                            default -> null;
                        };
                        if (status != null) {
                            displayTasks(taskList.filterByStatus(status));
                        } else {
                            System.out.println("Invalid status filter");
                        }
                    }

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
                    System.out.println("Added task successfully.");
                    break;

                case "update":
                    if (parts.length < 2) {
                        System.out.println("Usage: update <task-id> <new-description>");
                        break;
                    }
                    String[] updateParts = parts[1].split(" ", 2); // Split ID and description
                    if (updateParts.length < 2) {
                        System.out.println("Error: Missing new description");
                        break;
                    }
                    if (taskList.findTaskById(updateParts[0]).isEmpty()) {
                        System.out.println("Error: Task ID not found");
                        break;
                    }
                    taskList.updateTask(updateParts[0], updateParts[1]);
                    System.out.println("Task updated successfully.");
                    break;

                case "delete":
                    if (parts.length < 2) {
                        System.out.println("Usage: delete <task-id>");
                        break;
                    }
                    String deleteId = parts[1].split(" ")[0]; // Split ID
                    if (taskList.findTaskById(deleteId).isEmpty()) {
                        System.out.println("Error: Task ID not found");
                        break;
                    }
                    taskList.deleteTask(deleteId);
                    break;
                    
                case "mark-in-progress":
                    if (parts.length < 2) {
                        System.out.println("Usage: mark-in-progress <task-id>");
                        break;
                    }
                    String inProgressId = parts[1].split(" ")[0];
                    if (taskList.findTaskById(inProgressId).isEmpty()) {
                        System.out.println("Error: Task ID not found");
                        break;
                    }
                    taskList.updateStatus(inProgressId, "in-progress");
                    break;

                case "mark-done":
                    if (parts.length < 2) {
                        System.out.println("Usage: mark-done <task-id>");
                        break;
                    }
                    String doneId = parts[1].split(" ")[0];
                    if (taskList.findTaskById(doneId).isEmpty()) {
                        System.out.println("Error: Task ID not found");
                        break;
                    }
                    taskList.updateStatus(doneId, "done");
                    break;

                default:
                    System.out.println("Unknown command: " + command);
            }

            System.out.println("Current tasks: " + taskList.getAllTasks().toString());
        }
    }
}