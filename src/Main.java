import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        List<FutureTask<Integer>> futureTasks = new ArrayList<>(25);
        for (int tx = 0; tx < texts.length; tx++) {
            int numberOfThread = tx;
            texts[tx] = generateText("aab", 30_000);
            Callable<Integer> myCallable = ()-> {
                int maxSize = 0;
                for (int i = 0; i < texts[numberOfThread].length(); i++) {
                    for (int j = 0; j < texts[numberOfThread].length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (texts[numberOfThread].charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(texts[numberOfThread].substring(0, 100) + " -> " + maxSize);
                return maxSize;
            };
            FutureTask<Integer> integerFutureTask = new FutureTask<>(myCallable);
            futureTasks.add(integerFutureTask);
            new Thread(integerFutureTask).start();
        }

        int maxSize = 0;
        for (FutureTask<Integer> futureTask: futureTasks){
            try {
                int size  = futureTask.get();
                if (size > maxSize) {
                    maxSize = size;
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("maxSize is " + maxSize);

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}