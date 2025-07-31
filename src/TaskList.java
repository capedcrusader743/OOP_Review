import java.util.ArrayList;
import java.util.List;
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

    public void deleteTask(int taskId) {
        tasks.removeIf(task -> task.getId() == taskId);
    }

    public void updateTask(int taskId, Task update_description) {
        tasks.set(taskId, update_description);
    }

    public List<Task> filterByStatus(Task.Status status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

}
