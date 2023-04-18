import java.util.concurrent.Semaphore;

import javax.swing.JTextArea;

import java.lang.Thread;

public class SemaphorePhilosopher implements Runnable{
    final int N = 5; 
    final static int THINKING = 0; 
    final static int HUNGRY = 1; 
    final static int EATING = 2;
    static int state[] = new int[5]; 
    static{
        for(int index=0;index<5;index++){
            state[index] = THINKING; 
        }
    }
    int i; 
    int ticksPerSecond; 
    int ticksRemaining = 0;
    Panel panel;
    JTextArea outputArea;
    Semaphore mutex = new Semaphore(1);
    Semaphore[] s = new Semaphore[N];
    
    SemaphorePhilosopher(int i, Panel panel, int ticksPerSecond, JTextArea outputArea){
        this.i=i; 
        this.panel = panel; 
        this.ticksPerSecond = ticksPerSecond; 
        this.outputArea = outputArea;
        for(int index=0;index<N;index++){
            s[index] = new Semaphore(1); 
        }
    }
    final int LEFT = (i + N - 1)%N;
    final int RIGHT = (i + 1)%N;
    @Override
    public void run() {
        while(true){
            try {
                tick();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Sleep according to how many ticks per second
            long sleepTime = 1000/ticksPerSecond; 
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   
        }
    }
    private void take_forks(int i) throws InterruptedException {
        mutex.acquire();
        System.out.println("Setting p" + (i+1) + " state to Hungry");
        state[i]=HUNGRY;
        setState(HUNGRY);
        test(i);
        mutex.release();
        s[i].acquire();
    }
    private void put_forks(int i) throws InterruptedException {
        mutex.acquire();
        System.out.println("Setting p" + (i+1) + " state to Thinking");
        state[i]=THINKING;
        setState(THINKING);
        test(LEFT);
        test(RIGHT);
        mutex.release();
    }
    private void test(int i){
        if(state[i]==HUNGRY && state[LEFT]!=EATING && state[RIGHT]!=EATING){
            System.out.println("Setting p" + (i+1) + "  state to eating");
            state[i]=EATING;
            setState(EATING);
            s[i].release();
        }
    }
    private void randomizeTicksRemaining(){
        int min = 1; 
        int max = 15; 
        // Set the number of remaining ticks to a random number
        ticksRemaining = (int)Math.floor(Math.random() * (max - min + 1) + min);
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
                newStateString = "Eatting";
                break;
        }
        panel.setStateText(newStateString, i+1);
    }
    private void tick() throws InterruptedException {
        if(ticksRemaining<=0){
            randomizeTicksRemaining();
        }
        if(state[i]==THINKING){
            if(ticksRemaining==1){
                state[i]=HUNGRY;
                setState(HUNGRY);
            }
            ticksRemaining--;
            outputArea.append("Philosopher " + (i+1) + " is thinking and \n wants to think for "+ ticksRemaining +" tick(s).\n");
        }
        else if(state[i]==HUNGRY) take_forks(i);
        if(state[i]==EATING){
            ticksRemaining--;
            outputArea.append("Philosopher " + (i+1) + " eats and \n wants to eat for "+ ticksRemaining +" tick(s).\n");
        }
        if(state[i]==EATING && ticksRemaining==0){
            put_forks(i);
        }

    }
    
}
