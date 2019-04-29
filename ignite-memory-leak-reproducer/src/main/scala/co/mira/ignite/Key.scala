package co.mira.ignite

import java.sql.Timestamp

import org.apache.ignite.cache.affinity.AffinityKeyMapped
import org.apache.ignite.cache.query.annotations.QuerySqlField

import scala.annotation.meta.field

case class Key(
	@(QuerySqlField@field)(
		orderedGroups = Array(
			new (QuerySqlField.Group@field)(
				name = "pk_public_dailyeventtheta",
				order = 0
			)
		)
	) eventDate: Timestamp,
	@(QuerySqlField@field)(
		orderedGroups = Array(
			new (QuerySqlField.Group@field)(
				name = "pk_public_dailyeventtheta",
				order = 1
			)
		)
	) s2CellId: Long,
	@(QuerySqlField@field) eventHour: Int,
	@(AffinityKeyMapped@field)
	@(QuerySqlField@field) parentS2CellId: Long
)