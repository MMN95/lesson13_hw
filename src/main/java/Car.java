import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {

    private static int CARS_COUNT;
    private final Race race;
    private final int speed;
    private final String name;
    private static boolean winner;

    private static final CountDownLatch prepare = MainClass.prepare;
    private static final CountDownLatch finish = MainClass.finish;
    private static final CyclicBarrier start = MainClass.start;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    private synchronized void checkWinner(Car car){
        if(!winner){
            System.out.println(car.name + " WIN!");
            winner = true;
        }
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            prepare.countDown();
            System.out.println(this.name + " готов");
            start.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        checkWinner(this);
        finish.countDown();
    }
}
