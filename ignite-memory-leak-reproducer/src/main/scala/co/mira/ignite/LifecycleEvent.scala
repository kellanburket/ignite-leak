package co.mira.ignite

import java.sql.Timestamp
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
		Executors.newSingleThreadExecutor.execute(() => {
			println("Running Data Streamer")
			val dataStreamer = ignite$.dataStreamer[DailyEventThetaKey, DailyEventTheta]("DailyEventTheta")
			val s2CellId = -8520149688496685056L
			while (true) {
				(0 until 5000).map(_ => {
					val bytes = Array.fill(2072)((scala.util.Random.nextInt(256) - 128).toByte)
					dataStreamer.addData(Map(
						DailyEventThetaKey(
							new Timestamp(System.currentTimeMillis()),
							s2CellId,
							0,
							scala.util.Random.nextInt(256).toByte
						) ->
						DailyEventTheta(bytes)
					).asJava)
				})
				println("Flushing Data Streamer")
				dataStreamer.flush()
				println("Data Streamer Flushed")
			}
		})
	}
}
