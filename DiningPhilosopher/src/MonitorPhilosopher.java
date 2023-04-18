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
    private Panel panel;

    public MonitorPhilosopher(int i, Panel panel, int ticksPerSecond, JTextArea outputArea) {
        this.i = i;
        this.panel = panel;
        this.ticksPerSecond = ticksPerSecond;
        this.outputArea = outputArea;
    }

    public synchronized void take_forks(int i) {
        MonitorPhilosopher.state[i] = HUNGRY;
        panel.setStateText("Hungry", i);
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
        MonitorPhilosopher.state[i] = THINKING;
        panel.setStateText("Thinking", i);
        test(LEFT);
        test(RIGHT);
        this.notifyAll();
    }

    public synchronized void test(int i) {
        if (MonitorPhilosopher.state[i] == HUNGRY && MonitorPhilosopher.state[LEFT] != EATING
                && MonitorPhilosopher.state[RIGHT] != EATING) {
            MonitorPhilosopher.state[i] = EATING;
            panel.setStateText("Eating", i);
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
            if(ticksRemaining==1) {
                state[i]=HUNGRY;
                panel.setStateText("Hungry", i);
            }
            ticksRemaining--;
            outputArea.append("Philosopher " + (i+1) + " is thinking and \n wants to think for "+ ticksRemaining +" tick(s).\n");
        } else if(state[i]==HUNGRY) {
            take_forks(i);
        }

        if(state[i]==EATING){
            ticksRemaining--;
            outputArea.append("Philosopher " + (i+1) + " eats and \n wants to eat for "+ ticksRemaining +" tick(s).\n");
        }
        if(state[i]==EATING && ticksRemaining==0){
            put_forks(i);
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