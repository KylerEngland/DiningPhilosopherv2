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
    JTextArea outputArea;
    Semaphore mutex = new Semaphore(1);
    Semaphore[] s = new Semaphore[N];
    SemaphorePhilosopher(int i, Panel panel, int ticksPerSecond, JTextArea outputArea){
        this.i=i; 
        this.ticksPerSecond = ticksPerSecond; 
        this.outputArea = outputArea; 
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
        state[i]=HUNGRY;
        test(i);
        mutex.release();
        s[i].acquire();
    }
    private void put_forks(int i) throws InterruptedException {
        mutex.acquire();
        state[i]=THINKING;
        test(LEFT);
        test(RIGHT);
        mutex.release();
    }
    private void test(int i){
        if(state[i]==HUNGRY && state[LEFT]!=EATING && state[RIGHT]!=EATING){
            state[i]=EATING;
            s[i].release();
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
    
}
