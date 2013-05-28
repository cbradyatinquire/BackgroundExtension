package background

import org.nlogo.window.Widget.LoadHelper
import org.nlogo.window.{Widget, InterfaceColors}
import java.awt._
import javax.imageio.ImageIO
import java.io.File
import org.nlogo.api.ExtensionException


/**
 * Created by IntelliJ IDEA.
 * User: cbrady
 * Date: 5/25/13
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
class ImageWidget(scaleDraw: Boolean, file: File) extends Widget {

  private val bufferedImage = {
    try {
      ImageIO.read(file)
    }
    catch {
      case ex: Exception => throw new ExtensionException( "Could not load image." , ex)
    }
  }

  setBounds(0,0,bufferedImage.getWidth(null), bufferedImage.getHeight(null))
  setBackground(InterfaceColors.TRANSPARENT)

  override def widgetWrapperOpaque = getBackground != InterfaceColors.TRANSPARENT

  //don't load or save at the moment.
  def save: String = { "" }
  def load(strings: Array[String], helper: LoadHelper): AnyRef = null

  override def needsPreferredWidthFudgeFactor = false
  override def getMinimumSize = new Dimension(bufferedImage.getWidth, bufferedImage.getHeight)
  //font is supplied, though we don't use it
  override def getPreferredSize(font: Font): Dimension = getMinimumSize
  
  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    if (scaleDraw) g.drawImage(bufferedImage, 0, 0, getWidth, getHeight, 0, 0, bufferedImage.getWidth, bufferedImage.getHeight, null)
    else g.drawImage(bufferedImage, 0, 0, null)
  }

}
