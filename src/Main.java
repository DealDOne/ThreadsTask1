import java.util.*;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        List<Thread> threads = new ArrayList<>(25);
        long startThreads = System.currentTimeMillis();
        for (int tx = 0; tx < texts.length; tx++) {
            int numberOfThread = tx;
            texts[tx] = generateText("aab", 30_000);
            Runnable myRunnable = () -> {
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
            };

            threads.add(new Thread(myRunnable));
            threads.get(tx).start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        long endThreads = System.currentTimeMillis();

        System.out.println("Time: " + (endThreads - startThreads) + "ms");

        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            int maxSize = 0;
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < text.length(); j++) {
                    if (i >= j) {
                        continue;
                    }
                    boolean bFound = false;
                    for (int k = i; k < j; k++) {
                        if (text.charAt(k) == 'b') {
                            bFound = true;
                            break;
                        }
                    }
                    if (!bFound && maxSize < j - i) {
                        maxSize = j - i;
                    }
                }
            }
            System.out.println(text.substring(0, 100) + " -> " + maxSize);
        }
        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");
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