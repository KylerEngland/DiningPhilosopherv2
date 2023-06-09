import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JTextArea;

public class MonitorPhilosopher implements Runnable {

    final int N = 5;
    int i;
    final int THINKING = 0;
    final int HUNGRY = 1;
    final int EATING = 2;

    static int[] state = new int[5];
    private int ticksRemaining;
    private int ticksPerSecond;
    private JTextArea outputArea;
    private Panel panel;
    private ReentrantLock lock;

    public MonitorPhilosopher(int i, Panel panel, int ticksPerSecond, JTextArea outputArea, ReentrantLock lock) {
        this.i = i;
        this.panel = panel;
        this.ticksPerSecond = ticksPerSecond;
        this.outputArea = outputArea;
        this.lock = lock;
        randomizeTicksRemaining();
    }

    public synchronized void take_forks(int i) {
        randomizeTicksRemaining();
        MonitorPhilosopher.state[i] = HUNGRY;
        setState(HUNGRY);
        test(i);
        if (MonitorPhilosopher.state[i] != EATING) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void put_forks(int i) {
        System.out.println("Setting P" + (i + 1) + " state to thinking");
        randomizeTicksRemaining();
        MonitorPhilosopher.state[i] = THINKING;
        setState(THINKING);
        test(getLeft(i));
        test(getRight(i));
    }

    public synchronized void test(int i) {
        if (MonitorPhilosopher.state[i] == HUNGRY && MonitorPhilosopher.state[getLeft(i)] != EATING
                && MonitorPhilosopher.state[getRight(i)] != EATING) {
            MonitorPhilosopher.state[i] = EATING;
            setState(EATING);
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }


    private void randomizeTicksRemaining() {
        int min = 1;
        int max = 15;
        // Set the number of remaining ticks to a random number
        ticksRemaining = (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    
    private void tick() throws InterruptedException {

        if (state[i] == THINKING) {
            ticksRemaining--;
            outputArea.append("Philosopher " + (i + 1) + " is thinking and \n wants to think for " + ticksRemaining
                    + " tick(s).\n");

        }
        if (state[i] == HUNGRY || (ticksRemaining <= 0 && state[i] == THINKING))
            take_forks(i);
        if (state[i] == EATING) {
            ticksRemaining--;
            outputArea.append(
                    "Philosopher " + (i + 1) + " eats and \n wants to eat for " + ticksRemaining + " tick(s).\n");
        }
        if (state[i] == EATING && ticksRemaining <= 0) {
            put_forks(i);
        }

    }

    public void run() {
        while (true) {
            try {
                tick();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Sleep according to how many ticks per second
            long sleepTime = 1000 / ticksPerSecond;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setState(int newState) {
        String newStateString = "Null";
        switch (newState) {
            case THINKING:
                newStateString = "Thinking";
                break;
            case HUNGRY:
                newStateString = "Hungry";
                break;
            case EATING:
                newStateString = "Eating";
                break;
        }
        panel.setStateText(newStateString, i + 1);
    }

    public int getLeft(int i) {
        return (i + N - 1) % N;
    }

    public int getRight(int i) {
        return (i + 1) % N;
    }

    public void setTicksPerSecond(int ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
    }

}