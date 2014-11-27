package com.misc.ex1

/**
 *
 */
abstract class SemiGroup[A] {
  def add(x: A, y: A): A
}