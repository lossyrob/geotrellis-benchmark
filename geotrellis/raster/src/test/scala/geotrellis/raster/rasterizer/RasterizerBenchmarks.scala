package benchmark.geotrellis.raster.rasterize

import geotrellis.raster._
import geotrellis.raster.rasterize._
import geotrellis.vector._
import geotrellis.vector.io.json._

import scaliper._

import scala.util.Random

object RasterizerBenchmarks {
  def randomRasterN(n: Int) = {
    val a = Array.ofDim[Int](n*n).map(a => Random.nextInt(255))
    IntArrayTile(a, n, n)
  }

}

class RasterizerBenchmarks extends Benchmarks with ConsoleReport with EndpointReport {
  benchmark("Rasterizer") { 
    run("rasterization against a travelshed polygon") {
      new Benchmark {

        var tile: Tile = null
        var transitPoly: Polygon = null
        var transitRasterExtent: RasterExtent = null

        def randomRasterN(n: Int) = {
          val a = Array.ofDim[Int](n*n).map(a => Random.nextInt(255))
          IntArrayTile(a, n, n)
        }


        override def setUp() = { 
          val size = 512
          tile = RasterizerBenchmarks.randomRasterN(size)
          transitPoly = GeoJson.fromFile[Polygon]("data/geojson/transitgeo.json")

          val Extent(xmin, ymin, xmax, ymax) = transitPoly.envelope
          val dx = (xmax - xmin) / 4
          val dy = (ymax - ymin) / 4
          val ext = Extent(xmin - dx, ymin - dy, xmax + dx, ymax + dy)
          transitRasterExtent = RasterExtent(ext, size, size)
        }

        def run() = {
          var s = 0
          Rasterizer.foreachCellByPolygon(transitPoly, transitRasterExtent) { (c, r) =>
            s += c
          }
          Some(s)
        }
      }
    }

    run("rasterization against a travelshed polygon") {
      new Benchmark {

        var tile: Tile = null
        var transitPoly: Polygon = null
        var transitRasterExtent: RasterExtent = null

        def randomRasterN(n: Int) = {
          val a = Array.ofDim[Int](n*n).map(a => Random.nextInt(255))
          IntArrayTile(a, n, n)
        }


        override def setUp() = { 
          val size = 512
          tile = RasterizerBenchmarks.randomRasterN(size)
          transitPoly = GeoJson.fromFile[Polygon]("data/geojson/transitgeo.json")

          val Extent(xmin, ymin, xmax, ymax) = transitPoly.envelope
          val dx = (xmax - xmin) / 4
          val dy = (ymax - ymin) / 4
          val ext = Extent(xmin - dx, ymin - dy, xmax + dx, ymax + dy)
          transitRasterExtent = RasterExtent(ext, size, size)
        }

        def run() = {
          var s = 0
          polygon.OldPolygonRasterizer.foreachCellByPolygon(transitPoly, transitRasterExtent) { (c, r) =>
            s += c
          }
          Some(s)
        }
      }
    }
  }
}
