package co.mira.ignite

import org.apache.ignite.cache.query.annotations.QueryTextField

import scala.annotation.meta.field

case class ValueLong(
	@(QueryTextField@field) theta: Long
)