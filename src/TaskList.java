import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskList {

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

    public List<Task> filterByStatus(Task.Status status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    private Optional<Task> findTaskById(String taskId) {
        return tasks.stream().filter(t -> t.getId().equals(taskId)).findFirst();
    }

}
