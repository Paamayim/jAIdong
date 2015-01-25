package jaidong.mgr

import bwapi.UnitType.{None => Unknown_None, _}
import bwapi.{Unit => BWUnit, _}
import bwta.BaseLocation
import bwta.BWTA
import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.util.{Success, Failure}

import jaidong._
import jaidong.Implicits._
import jaidong.util._

class MacroMgr {
  def game = Bot.game
  def self = Bot.self

  var supplyIncoming: Int = 0

  def needsSupply: Boolean = {
    Bot.available.supply + supplyIncoming <= 2
  }

  def onNewHatchery(hatch: BWUnit) = {
  }

  def onNewLarva(larva: BWUnit) = {
    if (needsSupply) {
      val extraSupply = Zerg_Overlord.supplyProvided
      supplyIncoming += extraSupply

      Zerg_Overlord.allocate(90).flatMap { voucher =>
        println("money for ovie")
        voucher.cash()
        Bot.morph(larva, Zerg_Overlord)
      }.map { ovie =>
        supplyIncoming -= extraSupply
        println("ovie done")
      }
    } else {
      Zerg_Drone.allocate(50).map { voucher =>
        voucher.cash()
        larva.morph(Zerg_Drone)
      }
    }
  }
}
