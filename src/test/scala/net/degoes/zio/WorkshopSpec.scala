package net.degoes.zio

import zio.ZIO
import zio.duration._
import zio.test._
import zio.test.environment._
import zio.test.Assertion._
import zio.test.TestAspect.{ignore, timeout}
import zio.test.mock.MockConsole._

object WorkshopSpec
    extends DefaultRunnableSpec({
      import TicTacToe._
      import BoardHelpers._

      suite("Workshop tests")(
        testM("HelloWorld") {
          for {
            value <- HelloWorld.run(Nil)
            output <- TestConsole.output
          } yield
            assert(value, equalTo(0)) &&
              assert(output, equalTo(Vector("Hello World!\n")))
        },
        testM("ErrorRecovery") {
          assertM(ErrorRecovery.run(Nil), equalTo(1))
        },
        testM("Looping") {
          for {
            value <- Looping.run(Nil)
            output <- TestConsole.output
          } yield
              assert(value, equalTo(0)) &&
                assert(output.size, equalTo(102)) // (100 to 0) + 1`)`
        },
        testM("EffectConversion") {
          for {
            value <- EffectConversion.run(Nil)
            output <- TestConsole.output
          } yield
              assert(value, equalTo(0)) &&
                assert(output, equalTo(Vector())) // for Vector(), TestConsole cannot capture the original `println`
        },
        // not posible to test it with stdin
        // testM("ErrorNarrowing") {
        //   for {
        //     value <- ErrorNarrowing.run(Nil)
        //     output <- TestConsole.output
        //   } yield
        //       assert(value, equalTo(0))
        // },
        testM("PromptName") {
          ZIO(assertCompletes)
        } @@ ignore,
        testM("AlarmApp") {
          ZIO(assertCompletes)
        } @@ ignore,
        suite("Board")(
          test("won horizontal first") {
            horizontalFirst(Mark.X) && horizontalFirst(Mark.O)
          },
          test("won horizontal second") {
            horizontalSecond(Mark.X) && horizontalSecond(Mark.O)
          },
          test("won horizontal third") {
            horizontalThird(Mark.X) && horizontalThird(Mark.O)
          },
          test("won vertical first") {
            verticalFirst(Mark.X) && verticalFirst(Mark.O)
          },
          test("won vertical second") {
            verticalSecond(Mark.X) && verticalSecond(Mark.O)
          },
          test("won vertical third") {
            verticalThird(Mark.X) && verticalThird(Mark.O)
          },
          test("won diagonal first") {
            diagonalFirst(Mark.X) && diagonalFirst(Mark.O)
          },
          test("won diagonal second") {
            diagonalSecond(Mark.X) && diagonalSecond(Mark.O)
          }
        )
      )
    })

object BoardHelpers {
  import TicTacToe._

  def horizontalFirst(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(chr, chr, chr),
          List(' ', ' ', ' '),
          List(' ', ' ', ' ')
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }

  def horizontalSecond(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(' ', ' ', ' '),
          List(chr, chr, chr),
          List(' ', ' ', ' ')
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }

  def horizontalThird(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(' ', ' ', ' '),
          List(' ', ' ', ' '),
          List(chr, chr, chr)
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }

  def verticalFirst(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(chr, ' ', ' '),
          List(chr, ' ', ' '),
          List(chr, ' ', ' ')
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }

  def verticalSecond(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(' ', chr, ' '),
          List(' ', chr, ' '),
          List(' ', chr, ' ')
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }

  def verticalThird(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(' ', ' ', chr),
          List(' ', ' ', chr),
          List(' ', ' ', chr)
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }

  def diagonalFirst(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(' ', ' ', chr),
          List(' ', chr, ' '),
          List(chr, ' ', ' ')
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }

  def diagonalSecond(mark: Mark) = {
    val chr = mark.renderChar

    assert(
      Board
        .fromChars(
          List(chr, ' ', ' '),
          List(' ', chr, ' '),
          List(' ', ' ', chr)
        )
        .flatMap(_.won),
      isSome(equalTo(mark))
    )
  }
}
