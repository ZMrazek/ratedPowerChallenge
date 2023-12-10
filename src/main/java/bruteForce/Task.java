package bruteForce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task {
    private int taskId;
    String name;
    List<Task> dependencies = new ArrayList<>();
    Integer delayedCost;

    public Task(int taskId, int delayedCost, List<Task> dependencies) {
        this.taskId = taskId;
        this.delayedCost = delayedCost;
        this.dependencies = dependencies;
    }

    public Task(String name) {
        this.name = name;
        this.dependencies = new ArrayList<>();
    }

    public Task(String name, Integer delayedCost) {
        this.name = name;
        this.delayedCost = delayedCost;
    }

    public Task(String name, Integer delayedCost, Task... dependencies) {
        this.name = name;
        this.delayedCost = delayedCost;
        this.dependencies = new ArrayList<>(Arrays.asList(dependencies));
    }

    public Task(String name, Task... dependencies) {
        this.name = name;
        this.dependencies = new ArrayList<>(Arrays.asList(dependencies));
    }

    public void addDependency(Task dependency) {
        this.dependencies.add(dependency);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Task> dependencies) {
        this.dependencies = dependencies;
    }

    public Integer getDelayedCost() {
        return delayedCost;
    }

    public void setDelayedCost(Integer delayedCost) {
        this.delayedCost = delayedCost;
    }

    @Override
    public String toString() {
        return name;
    }
}
