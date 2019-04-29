package co.mira.ignite

import org.apache.ignite.cache.query.annotations.QueryTextField

import scala.annotation.meta.field

case class ValueBytes(@(QueryTextField@field) theta: Array[Byte])