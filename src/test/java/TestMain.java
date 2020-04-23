import com.epamThirdTask.CallCenter;
import com.epamThirdTask.client.Client;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;


public class TestMain {
    private static  CallCenter callCenter;
    private static BlockingQueue<Client> clients;
    private static Semaphore operators;
    private static final int COUNT_OF_CLIENTS = 10;


    @BeforeClass
    public static void  initial(){
        callCenter = new CallCenter();
        clients = new ArrayBlockingQueue<>(COUNT_OF_CLIENTS);
        operators = new Semaphore(2);
        for (int i = 0; i < COUNT_OF_CLIENTS; i++) {
            clients.add(new Client(operators,i));
        }
        clients.forEach(c->{
            c.setClients(clients);
        });
    }

    @Test
    public void shouldSetIsServiceToTrue() throws  InterruptedException {
        callCenter.start(clients);
        Thread.sleep(2000);
        assertEquals(Client.getJ().get(), COUNT_OF_CLIENTS);

    }


}
