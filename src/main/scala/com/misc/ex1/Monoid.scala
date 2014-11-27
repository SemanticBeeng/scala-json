package com.misc.ex1

/**
 *
 */


abstract class Monoid[A] extends SemiGroup[A] {
  def unit: A
}