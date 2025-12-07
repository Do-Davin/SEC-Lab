package com.example;

import com.example.util.NumberUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML
    private TextField lowerPrimeField;
    @FXML
    private TextField upperPrimeField;
    @FXML
    private TextArea outputPrimeArea;
    @FXML
    private Button generatePrimeBtn;
    @FXML
    private Button pausePrimeBtn;
    @FXML
    private Button resumePrimeBtn;
    @FXML
    private Button stopPrimeBtn;
    @FXML
    private Button restartPrimeBtn;
    @FXML
    private ProgressBar primeProgress;

    @FXML
    private TextField lowerFibField;
    @FXML
    private TextField upperFibField;
    @FXML
    private TextArea outputFibArea;
    @FXML
    private Button generateFibBtn;
    @FXML
    private Button pauseFibBtn;
    @FXML
    private Button resumeFibBtn;
    @FXML
    private Button stopFibBtn;
    @FXML
    private Button restartFibBtn;
    @FXML
    private ProgressBar fibProgress;

    private Thread primeThread;
    private Thread fibThread;

    private volatile boolean pausePrime = false;
    private volatile boolean stopPrime = false;

    private volatile boolean pauseFib = false;
    private volatile boolean stopFib = false;

    @FXML
    public void initialize() {

        generatePrimeBtn.setOnAction(e -> generatePrime());
        generateFibBtn.setOnAction(e -> generateFib());

        pausePrimeBtn.setOnAction(e -> pausePrime = true);
        resumePrimeBtn.setOnAction(e -> pausePrime = false);
        stopPrimeBtn.setOnAction(e -> stopPrime = true);
        restartPrimeBtn.setOnAction(e -> restartPrime());

        pauseFibBtn.setOnAction(e -> pauseFib = true);
        resumeFibBtn.setOnAction(e -> pauseFib = false);
        stopFibBtn.setOnAction(e -> stopFib = true);
        restartFibBtn.setOnAction(e -> restartFib());
    }

    private void restartPrime() {
        stopPrime = true;
        pausePrime = false;

        outputPrimeArea.clear();
        primeProgress.setProgress(0);

        generatePrime();
    }

    private void generatePrime() {

        if (primeThread != null && primeThread.isAlive()) return;

        pausePrime = false;
        stopPrime = false;

        int lower = lowerPrimeField.getText().isEmpty() ? 2 : Integer.parseInt(lowerPrimeField.getText());
        boolean noUpper = upperPrimeField.getText().isEmpty();
        int upper = noUpper ? Integer.MAX_VALUE : Integer.parseInt(upperPrimeField.getText());

        outputPrimeArea.clear();

        if (noUpper) {
            Platform.runLater(() -> primeProgress.setProgress(-1));
        } else {
            primeProgress.setProgress(0);
        }

        primeThread = new Thread(() -> {

            int totalRange = noUpper ? 0 : upper - lower;
            int count = 0;

            for (int n = lower; n <= upper; n++) {

                if (stopPrime) break;

                while (pausePrime) {
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {}
                }

                if (NumberUtils.isPrime(n)) {
                    int p = n;
                    Platform.runLater(() -> outputPrimeArea.appendText(p + "\n"));
                }

                if (!noUpper) {
                    int finalCount = count;
                    Platform.runLater(() ->
                        primeProgress.setProgress((double) finalCount / totalRange)
                    );
                    count++;
                }

                try { Thread.sleep(30); } catch (Exception e) {}
            }
        });

        primeThread.setDaemon(true);
        primeThread.start();
    }

    private void restartFib() {
        stopFib = true;
        pauseFib = false;

        outputFibArea.clear();
        fibProgress.setProgress(0);

        generateFib();
    }

    private void generateFib() {

        if (fibThread != null && fibThread.isAlive()) return;

        pauseFib = false;
        stopFib = false;

        int lower = lowerFibField.getText().isEmpty() ? 0 : Integer.parseInt(lowerFibField.getText());
        boolean noUpper = upperFibField.getText().isEmpty();
        int upper = noUpper ? Integer.MAX_VALUE : Integer.parseInt(upperFibField.getText());

        outputFibArea.clear();

        if (noUpper) {
            fibProgress.setProgress(-1);
        } else {
            fibProgress.setProgress(0);
        }

        fibThread = new Thread(() -> {

            long a = 0, b = 1;

            long min = lower;
            long max = upper;
            long total = noUpper ? 0 : (max - min);
            long count = 0;

            while (a <= upper) {

                if (stopFib) break;

                while (pauseFib) {
                    try {
                        Thread.sleep(50);
                    } catch (Exception e) {}
                }

                if (a >= lower) {
                    long f = a;
                    Platform.runLater(() -> outputFibArea.appendText(f + "\n"));
                }

                if (!noUpper) {
                    long finalA = a;
                    Platform.runLater(() -> {
                        if (finalA > 0 && upper > 0) {
                            double progress = Math.log(finalA) / Math.log(upper);
                            fibProgress.setProgress(progress);
                        }
                    });
                }

                long next = a + b;
                a = b;
                b = next;

                try { Thread.sleep(30); } catch (Exception ignored) {}
            }
        });

        fibThread.setDaemon(true);
        fibThread.start();
    }
}
