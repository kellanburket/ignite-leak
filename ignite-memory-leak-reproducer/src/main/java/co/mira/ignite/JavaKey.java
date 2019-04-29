package co.mira.ignite;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.sql.Timestamp;

public class JavaKey implements Serializable {
	JavaKey(Timestamp eventDate, Long s2CellId, Integer eventHour, Long parentS2CellId) {
		this.eventDate = eventDate;
		this.s2CellId = s2CellId;
		this.eventHour = eventHour;
		this.parentS2CellId = parentS2CellId;
	}

	@QuerySqlField(
		orderedGroups={
			@QuerySqlField.Group(name = "pk_public_dailyeventtheta", order=0)
		}
	) private Timestamp eventDate;

	@QuerySqlField(
		orderedGroups={
			@QuerySqlField.Group(name = "pk_public_dailyeventtheta", order=1)
		}
	) private Long s2CellId;

	@QuerySqlField private Integer eventHour;

	@AffinityKeyMapped
	@QuerySqlField private Long parentS2CellId;
}
