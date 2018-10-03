package battleship.utils.display

trait Display[T] {
  def show(t: T): Unit
}
