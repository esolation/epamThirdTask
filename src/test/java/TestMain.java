import com.epamThirdTask.CallCenter;
import com.epamThirdTask.client.Client;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;


public class TestMain {
    static public CallCenter callCenter;
    static BlockingQueue<Client> clients;
    static Semaphore operators;


    @BeforeClass
    public static void  initial(){
        callCenter = new CallCenter();
        clients = new LinkedBlockingDeque<>();
        operators = new Semaphore(2,true);
        for (int i = 0; i < 10; i++) {
            clients.add(new Client(operators,i));
        }
        clients.forEach(c->{
            c.setClients(clients);
        });
    }

    @Test
    public void shouldSetIsServiceToTrue() throws ExecutionException, InterruptedException {
       callCenter.start(clients);
        Thread.sleep(2000);
       assertEquals(Client.j.get(), new AtomicInteger(10).get());

    }


}
