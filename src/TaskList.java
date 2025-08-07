import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TaskList {

    private static final String JSON_FILE = "tasks.json";

    private List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // CRUD Operations
    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(String taskId) {
        tasks.removeIf(task -> task.getId().equals(taskId));
    }

    public void updateTask(String taskId, String update_description) {
        findTaskById(taskId).ifPresent(task -> {
            task.setDescription(update_description);
            task.setUpdatedAt(LocalDateTime.now());
        });
    }

    public void updateStatus(String taskId, String status) {
        findTaskById(taskId).ifPresent(task -> {
            if (status.equals("in-progress")) {
                task.markAsInProgress();
            }
            else if (status.equals("done")) {
                task.markAsDone();
            }
        });
    }

    public List<Task> filterByStatus(Task.Status status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> findTaskById(String taskId) {
        return tasks.stream().filter(t -> t.getId().equals(taskId)).findFirst();
    }

    // Methods for JSON Handling
    public void saveToFile() {
        StringBuilder jsonBuilder = new StringBuilder("[\n");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            jsonBuilder.append(String.format(
                    "  {\n" +
                            "    \"id\": \"%s\",\n" +
                            "    \"description\": \"%s\",\n" +
                            "    \"status\": \"%s\",\n" +
                            "    \"createdAt\": \"%s\",\n" +
                            "    \"updatedAt\": \"%s\"\n" +
                            "  }%s\n",
                    task.getId(),
                    escapeJson(task.getDescription()),
                    task.getStatus(),
                    task.getCreatedAt(),
                    task.getUpdatedAt(),
                    i < tasks.size() - 1 ? "," : ""
            ));

            jsonBuilder.append("]");

            try {
                Files.write(Paths.get(JSON_FILE), jsonBuilder.toString().getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                System.err.println("Error saving tasks: " + e.getMessage());
            }
        }

    }

    public void loadFromFile() {
        try {
            if (!Files.exists(Paths.get(JSON_FILE))) return;

            String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
            content = content.replaceAll("[\\n\\\t]", "").trim();

            if (content.length() < 3) return; // Empty or invalid

            // Simple JSON parsing (without library)
            String[] taskEntries = content.substring(2, content.length() -2).split("\\},\\s*\\{");

            for (String entry : taskEntries) {
                Task task = parseTask(entry);
                if (task != null) tasks.add(task);
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    private Task parseTask(String jsonEntry) {
        try {
            String id = extractValue(jsonEntry, "id");
            String description = unescapeJson(extractValue(jsonEntry, "description"));
            Task.Status status =  Task.Status.valueOf(extractValue(jsonEntry, "status"));
            LocalDateTime createdAt = LocalDateTime.parse(extractValue(jsonEntry, "createdAt"));
            LocalDateTime updatedAt = LocalDateTime.parse(extractValue(jsonEntry, "updatedAt"));
            return new Task(id, description, createdAt, updatedAt);
        } catch (Exception e) {
            System.err.println("Failed to parse task: " + e.getMessage());
            return null;
        }
    }

    // Helper methods
    private String extractValue(String json, String key) {
        String pattern = "\"" + key + "\":\\s*\"([^\"]*)\"";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(json);
        return m.find() ? m.group(1) : "";
    }

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }

    private String unescapeJson(String input) {
        return input.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\\\", "\\");
    }

}
