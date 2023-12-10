package bruteForce;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BruteForce {

    private static int validCombinationsCount = 0;

    public static void main(String[] args) {
        Task[] tasks = ExcelImporter.importTasksFromExcel("src/inputTasks.xlsx", 12, 0).toArray(new Task[0]);

        long startTime = System.nanoTime();
        Map<Task[], Integer> combinations = generatePossibleOptions(tasks, 0);
        Map<Task[], Integer> combinationsWithAccumulatedCost = calculateAccumulatedCost(combinations);
        printBestCombinations(combinationsWithAccumulatedCost);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Time to generate all combinations with accumulated cost: " + duration / 1000000 + " ms");
    }

    private static void printBestCombinations(Map<Task[], Integer> combinations) {
        int bestCost = Integer.MAX_VALUE;
        for (int value : combinations.values()) {
            bestCost = Math.min(bestCost, value);
        }

        System.out.println("Number of possible combinations: " + validCombinationsCount);
        // Print all items with the lowest value
        if (combinations.size() == 0) {
            System.out.println("No valid combinations found.");
        } else {
            System.out.println("Best solution(s):");
            for (Map.Entry<Task[], Integer> entry : combinations.entrySet()) {
                if (entry.getValue() == bestCost) {
                    System.out.println(Arrays.toString(entry.getKey()) + ", Accumulated Cost: " + entry.getValue());
                }
            }
        }
    }

    public static Map<Task[], Integer> calculateAccumulatedCost(Map<Task[], Integer> combinations) {
        HashMap<Task[], Integer> resultMap = new HashMap<>();
        for (Map.Entry<Task[], Integer> entry : combinations.entrySet()) {
            Task[] tasks = entry.getKey();
            int cost = calculateCost(tasks);
            resultMap.put(tasks, cost);
        }
        return resultMap;
    }

    public static int calculateCost(Task[] tasks) {
        int accumulatedCost = 0;

        // Iterate through tasks
        for (int i = 0; i < tasks.length; i++) {

            // Check if the remaining tasks could have been executed after executing the current task
            for (int j = i + 1; j < tasks.length; j++) {
                Task otherTask = tasks[j];

                // Check if the other task could have been executed (not blocked)
                if (!isBlocked(otherTask, Arrays.copyOfRange(tasks, 0, i), j)) {
                    // If the other task could have been executed, add its delay cost to the accumulated cost
                    accumulatedCost += otherTask.getDelayedCost();
                }
            }
        }

        return accumulatedCost;
    }

    public static boolean isBlocked(Task currentTask, Task[] executedTasks, int currentIndex) {
        for (Task dependency : currentTask.getDependencies()) {
            // Check if the dependency is not present in the executed tasks before or at the current index
            if (!isTaskExecuted(dependency, Arrays.copyOfRange(executedTasks, 0, currentIndex + 1))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTaskExecuted(Task currentTask, Task[] executedTasks) {
        // Check if the task is present in the executed tasks
        return Arrays.asList(executedTasks).contains(currentTask);
    }

    public static Map<Task[], Integer> generatePossibleOptions(Task[] tasks, int index) {
        Map<Task[], Integer> combinations = new HashMap<>();

        if (index == tasks.length - 1) {
            combinations.put(Arrays.copyOf(tasks, tasks.length), 0);
            validCombinationsCount++;
        } else {
            for (int i = index; i < tasks.length; i++) {
                if (isValidSwap(tasks, index, i)) {
                    swap(tasks, index, i);
                    combinations.putAll(generatePossibleOptions(tasks, index + 1));
                    swap(tasks, index, i); // backtrack to restore the original order
                }
            }
        }
        return combinations;
    }

    public static boolean isValidSwap(Task[] tasks, int index, int targetIndex) {
        // Check if the dependencies of the target task are satisfied after the swap
        for (Task dependency : tasks[targetIndex].getDependencies()) {
            if (!isTaskExecuted(dependency, Arrays.copyOfRange(tasks, 0, index))) {
                return false;
            }
        }

        // Check if the dependencies of the current task are satisfied after the swap
        for (Task dependency : tasks[index].getDependencies()) {
            if (!isTaskExecuted(dependency, Arrays.copyOfRange(tasks, 0, targetIndex + 1))) {
                return false;
            }
        }
        return true;
    }

    public static void swap(Task[] teams, int i, int j) {
        Task temp = teams[i];
        teams[i] = teams[j];
        teams[j] = temp;
    }
}
