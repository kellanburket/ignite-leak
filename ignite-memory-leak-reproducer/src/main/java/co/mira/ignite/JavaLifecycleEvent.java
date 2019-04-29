package co.mira.ignite;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;

public class JavaLifecycleEvent implements LifecycleBean {

	public void onLifecycleEvent(LifecycleEventType evt) {
		if(evt == LifecycleEventType.AFTER_NODE_START) {
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			Ignite ignite = Ignition.ignite();

			while(!ignite.cluster().active()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// Do Nothing
				}
			}

			executorService.execute(new Runnable() {
				@Override
				public void run() {
					Ignite ignite = Ignition.ignite();
					IgniteDataStreamer<JavaKey, JavaValue> dataStreamer = ignite.dataStreamer("Value");
					while(true) {
						Map<JavaKey, JavaValue> data = new HashMap<>();
						for(int i = 0; i < 50000; i++) {
							byte[] bytes = new byte[2072];
							new Random().nextBytes(bytes);

							JavaKey key = new JavaKey(
								new Timestamp(System.currentTimeMillis()),
								-8520149688496685056L,
								new Random().nextInt(256),
								-8520149688496685056L
							);
							JavaValue value = new JavaValue(
								UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " " +
									UUID.randomUUID().toString() + " "
							);
							data.put(key, value);
						}
						dataStreamer.addData(data);
						dataStreamer.flush();
					}
				}
			});
		}
	}
}