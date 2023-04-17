import javax.swing.JTextArea;

public class MonitorPhilosopher implements Runnable{

    final int N = 5;
    int i;
    final int LEFT = (i + N -1)%N;
    final int RIGHT = (i + 1)%N;
    final int THINKING = 0;
    final int HUNGRY = 1;
    final int EATING = 2;


    static int[] state = new int[5];
    private int ticksRemaining;
    private int ticksPerSecond;
    private JTextArea outputArea;
    private boolean running = true;


    public MonitorPhilosopher(int i, int ticksRemaining, int ticksPerSecond, JTextArea outputArea){
        this.i = i;
        this.ticksRemaining = ticksRemaining;
        this.ticksPerSecond = ticksPerSecond;
        this.outputArea = outputArea;
    }

    public synchronized void take_forks(int i){
        MonitorPhilosopher.state[i] = HUNGRY;
        test(i);
        this.notifyAll();
        if(MonitorPhilosopher.state[i] != EATING){
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void put_forks(int i){
        state[i] = THINKING;
        test(LEFT);
        test(RIGHT);
        this.notifyAll();
    }

    public synchronized void test(int i){
        if(MonitorPhilosopher.state[i] == HUNGRY && MonitorPhilosopher.state[LEFT] != EATING && MonitorPhilosopher.state[RIGHT] != EATING){
            MonitorPhilosopher.state[i] = EATING;
        }
    }


    public void tick() throws InterruptedException {
        //System.out.println("tick");
        
        // if(ticksRemaining<=0){
        //     int min = 1; 
        //     int max = 15; 
        //     // Set the number of remaining ticks to a random number
        //     ticksRemaining = (int)Math.floor(Math.random() * (max - min + 1) + min);
        //     // Change State
        //     if(state=="thinking") {
        //         setState("hungry");
        //         outputArea.append("Philosopher " + id + " is hungry and wants \n to eat for "+ ticksRemaining +" tick(s).\n");
        //     }
        //     else if(state=="eating"){
        //         //release forks
        //         leftFork.monitorPutDown();
        //         rightFork.monitorPutDown();
        //         setState("thinking");
        //         outputArea.append("Philosopher " + id + " is thinking and \n wants to think for "+ ticksRemaining +" tick(s).\n");
        //     } 
        // }
        // if(state=="thinking"){
        //     ticksRemaining--; 
        //     //output to log
        //     outputArea.append("Philosopher " + id + " thinks and \n wants to think for "+ ticksRemaining +" tick(s).\n");
        // }
        // else if(state=="eating"){
        //     //check if able to eat
        //     ticksRemaining--;
        //     //output to log
        //     outputArea.append("Philosopher " + id + " eats and \n wants to eat for "+ ticksRemaining +" tick(s).\n");
        // }
        // else if(state=="hungry"){
        //     //try to eat and if true it will lock the semephores
        //     if(canEat()==true){
        //         this.pickUpLeftFork();
        //         this.pickUpRightFork();
        //         //eat locking semephores
        //         setState("eating");
        //         ticksRemaining--;
        //         outputArea.append("Philosopher " + id + " eats and \n wants to eat for "+ ticksRemaining +" tick(s).\n");
        //     }
        //     else {
        //         outputArea.append("Philosopher " + id + " is unable to eat and \n wants to eat for "+ ticksRemaining +" tick(s).\n");
        //     }
        // }
        // Sleep according to how many ticks per second
        long sleepTime = 1000/ticksPerSecond; 
        Thread.sleep(sleepTime); 
    }

    public void run(){
        while(running){
            try {
                tick();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }
    }
    
}