package co.mira.ignite

import java.sql.Timestamp
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import org.apache.ignite.lifecycle.{LifecycleBean, LifecycleEventType}

import scala.collection.JavaConverters._
import org.apache.ignite.scalar._
import scalar._

import scala.concurrent.ExecutionContext

class LifecycleEvent extends LifecycleBean {
	override def onLifecycleEvent(evt: LifecycleEventType): Unit = if(evt == LifecycleEventType.AFTER_NODE_START) {
		while(!ignite$.cluster().active()) Thread.sleep(1000)
		val valueType = System.getenv("VALUE_TABLE")
		Executors.newSingleThreadExecutor.execute(() => {
			if (valueType == "ValueLong") {
				println("Running VALUE_TABLE: ValueLong")
				val dataStreamer = ignite$.dataStreamer[Key, ValueLong]("Value")
				while (true) {
					val data = (0 until 50000).map(_ => (
						Key(
							new Timestamp(System.currentTimeMillis()),
							-8520149688496685056L,
							0,
							scala.util.Random.nextInt(256).toByte
						), ValueLong(0)
					)).toMap.asJava
					dataStreamer.addData(data)
					dataStreamer.flush()
				}
			} else if (valueType == "ValueString") {
				println("Running VALUE_TABLE: ValueString")
				val dataStreamer = ignite$.dataStreamer[Key, ValueString]("Value")
				while (true) {
					val data = (0 until 5000).map(_ => (
						Key(
							new Timestamp(System.currentTimeMillis()),
							-8520149688496685056L,
							0,
							scala.util.Random.nextInt(256).toByte
						), ValueString(
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " " +
							UUID.randomUUID().toString + " "
						)
					)).toMap.asJava
					dataStreamer.addData(data)
					dataStreamer.flush()
				}
			} else {
				println("Running VALUE_TABLE: ValueBytes")
				val dataStreamer = ignite$.dataStreamer[Key, ValueBytes]("Value")
				while (true) {
					val data = (0 until 5000).map(_ => (
						Key(
							new Timestamp(System.currentTimeMillis()),
							-8520149688496685056L,
							0,
							scala.util.Random.nextInt(256).toByte
						), ValueBytes(Array.fill(2072)((scala.util.Random.nextInt(256) - 128).toByte))
					)).toMap.asJava
					dataStreamer.addData(data)
					dataStreamer.flush()
				}
			}
		})
	}
}
