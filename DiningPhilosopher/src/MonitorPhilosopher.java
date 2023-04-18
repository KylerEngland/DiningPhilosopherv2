import javax.swing.JTextArea;

public class MonitorPhilosopher implements Runnable {

    final int N = 5;
    int i;
    final int LEFT = (i + N - 1) % N;
    final int RIGHT = (i + 1) % N;
    final int THINKING = 0;
    final int HUNGRY = 1;
    final int EATING = 2;

    static int[] state = new int[5];
    private int ticksRemaining;
    private int ticksPerSecond;
    private JTextArea outputArea;
    private boolean running = true;

    public MonitorPhilosopher(int i, int ticksRemaining, int ticksPerSecond, JTextArea outputArea) {
        this.i = i;
        this.ticksRemaining = ticksRemaining;
        this.ticksPerSecond = ticksPerSecond;
        this.outputArea = outputArea;
    }

    public synchronized void take_forks(int i) {
        MonitorPhilosopher.state[i] = HUNGRY;
        test(i);
        this.notifyAll();
        if (MonitorPhilosopher.state[i] != EATING) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void put_forks(int i) {
        state[i] = THINKING;
        test(LEFT);
        test(RIGHT);
        this.notifyAll();
    }

    public synchronized void test(int i) {
        if (MonitorPhilosopher.state[i] == HUNGRY && MonitorPhilosopher.state[LEFT] != EATING
                && MonitorPhilosopher.state[RIGHT] != EATING) {
            MonitorPhilosopher.state[i] = EATING;
        }
    }
    
    private void randomizeTicksRemaining(){
        int min = 1; 
        int max = 15; 
        // Set the number of remaining ticks to a random number
        ticksRemaining = (int)Math.floor(Math.random() * (max - min + 1) + min);
    }

    private void tick() throws InterruptedException {
        if(ticksRemaining<=0){
            randomizeTicksRemaining();
        }
        if(state[i]==THINKING){
            if(ticksRemaining==1) state[i]=HUNGRY;
            ticksRemaining--;
            outputArea.append("Philosopher " + i + " is thinking and \n wants to think for "+ ticksRemaining +" tick(s).\n");
        }
        else if(state[i]==HUNGRY) take_forks(i);
        if(state[i]==EATING){
            ticksRemaining--;
            outputArea.append("Philosopher " + i + " eats and \n wants to eat for "+ ticksRemaining +" tick(s).\n");
        }
        
        
    }
    public void run() {
        while (running) {
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

}