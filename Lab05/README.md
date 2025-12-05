# Java Multithreading Lab

This project implements four lab tasks to practice multithreading, concurrency, thread pools, synchronization, and race-condition handling in Java.

## Folder Structure

```css
bunchOfFiles/              → Sample input files for Task 1
src/main/java/lab/task1/   → File processing with threads
src/main/java/lab/task2/   → Producer–Consumer with PriorityQueue
src/main/java/lab/task3/   → Web crawler simulation using thread pools
src/main/java/lab/task4/   → Bank account race condition demo
```

## Prerequisites
- JDK 17+
- VS Code (recommended)
- Java Extension Pack installed
- No Maven/Gradle required (plain Java is enough)

## How to Run

VS Code automatically detects your Java source folder:

```css
src/main/java
```
To run any task:
1. Open the corresponding TaskXMain.java
2. Click Run ▶ (top right)
or right-click → Run Java

## Task 1 – File Processing with Multithreading

### Setup
1. Create folder:
```
bunchOfFiles/
```
2. Add at least 5 text files (file1.txt ... file5.txt)
3. Add some non-text files (e.g., .png, .pdf) to test exception handling.
### Run
Open and run:
```css
src/main/java/lab/task1/Task1Main.java
```
### Output Includes
* Thread name
* Words, lines, characters counted
* Per-thread processing time
* Summary totals

## Task 2 – Producer–Consumer with Priority Queue

### Features
* PriorityQueue as shared buffer
* Buffer capacity = 10
* 2 Producers, 3 Consumers
* Producers generate random numbers with priorities
* Consumers always take highest priority first
* Runs for 30 seconds, then prints statistics
### Run
```css
src/main/java/lab/task2/Task2Main.java
```
### Output Includes
* Current buffer state after each operation
* Items produced/consumed per thread
* Total produced and consumed
## Task 3 – Thread Pool Web Crawler Simulation
### Features
* Simulated page download (Thread.sleep)
* 20 “URLs” processed
* Tested with 3 executor types:
    * FixedThreadPool
    * CachedThreadPool
    * SingleThreadExecutor
* Each page has timeout of 10 seconds
* Measures total execution time
### Run
```css
src/main/java/lab/task3/Task3Main.java
```
## Task 4 – Bank Account Race Condition Demo
### Features
* Demonstrates race condition using:
    * Unsafe version (no synchronization)
    * Safe version (synchronized)
* 10 threads × 100 transactions
* Initial balance = $1000
* Random deposit/withdraw operations
### Run
```css
src/main/java/lab/task4/Task4Main.java
```
### Output Includes
* First 20 transaction logs (unsafe & safe)
* Final balance for unsafe account (incorrect)
* Final balance for safe account (correct)
* Explanation of why the results differ