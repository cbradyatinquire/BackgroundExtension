package background

import org.nlogo.window.Widget.LoadHelper
import org.nlogo.window.{Widget, InterfaceColors}
import java.awt._
import image.BufferedImage
import javax.imageio.ImageIO
import java.io.File


/**
 * Created by IntelliJ IDEA.
 * User: cbrady
 * Date: 5/25/13
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
class ImageWidget extends Widget {

  setBackground(InterfaceColors.PLOT_BACKGROUND)

  val MIN_WIDTH = 50
  val DEFAULT_WIDTH = 100
  val MIN_HEIGHT = 100
  private var _width: Int = DEFAULT_WIDTH

  var scaleDraw = true
  private var bufferedImage: BufferedImage = null

  override def widgetWrapperOpaque = ! transparency

  def transparency = getBackground eq InterfaceColors.TRANSPARENT
  def transparency(trans: Boolean) {
    setBackground(if (trans) InterfaceColors.TRANSPARENT else InterfaceColors.TEXT_BOX_BACKGROUND)
    setOpaque(!trans)
  }

  //don't load or save at the moment.
  def save: String = {
    ""
  }
  def load(strings: Array[String], helper: LoadHelper): AnyRef = null


  def loadImage( file:File ) {
    bufferedImage = ImageIO.read( file )
    setBounds(0,0,bufferedImage.getWidth(null), bufferedImage.getHeight(null))
  }

  def setStretchable( stretch: Boolean ) {
    scaleDraw = stretch
  }

  override def setBounds(r: Rectangle) {
      if (r.width > 0) _width = r.width
      super.setBounds(r)
    }

  override def setBounds(x: Int, y: Int, width: Int, height: Int) {
    if (width > 0) this._width = width
    super.setBounds(x, y, width, height)
  }

  override def getMinimumSize = new Dimension(MIN_WIDTH, MIN_HEIGHT)
  override def getPreferredSize(font:Font): Dimension = {  //font is supplied, though we don't use it
    var height: Int = MIN_HEIGHT
    if (bufferedImage != null) {height = bufferedImage.getHeight}
    new Dimension(StrictMath.max(MIN_WIDTH, _width), height)
  }
  override def needsPreferredWidthFudgeFactor = false

  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    if (bufferedImage != null){
      if (scaleDraw) g.drawImage(bufferedImage, 0, 0, getWidth, getHeight, 0, 0, bufferedImage.getWidth, bufferedImage.getHeight, null)
      else g.drawImage(bufferedImage, 0, 0, null)
    }
  }

}
