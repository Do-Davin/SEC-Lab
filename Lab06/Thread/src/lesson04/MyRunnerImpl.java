package lesson04;

public class MyRunnerImpl {
    public static void main(String[] args) {
        useMyRun((String name) ->
            System.out.println("Hello, " + name));
    }

    public static void useMyRun(MyRunner runner) {
        runner.resolveMe("Davin");
    }
}
