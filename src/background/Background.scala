package background

import org.nlogo.api._
import org.nlogo.app.{WidgetWrapper, WidgetPanel, App}
import swing.event.MouseEvent
import java.awt.event
import event.MouseListener

/**
 * Created by IntelliJ IDEA.
 * User: cbrady
 * Date: 5/24/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
class Background extends org.nlogo.api.DefaultClassManager {

  override def load(primManager: PrimitiveManager) {
    primManager.addPrimitive("add", AddBackground)
  }

  object AddBackground extends DefaultCommand {

    override def getSyntax: Syntax = Syntax.commandSyntax( Array(Syntax.StringType, Syntax.BooleanType) )

    def perform(args: Array[Argument], context: Context) {

      val filepath     = args(0).getString
      val stretch      = args(1).getBooleanValue
      val widgetPanel  = App.app.workspace.getWidgetContainer.asInstanceOf[WidgetPanel]

      val methodArray  = Class.forName("org.nlogo.app.WidgetPanel").getDeclaredMethods
      val methodOpt    = methodArray.find( method => method.toString contains("addWidget") )
      val method       = methodOpt.getOrElse( throw new ExtensionException("Can't find the addWidget method") )

      val imageFile    = new java.io.File(filepath)
      val imageWidget  = new ImageWidget(stretch, imageFile)

      invokeLater{
        method.setAccessible(true)
        val imageWidgetWrapper = method.invoke(widgetPanel, imageWidget, Int.box(10), Int.box(10), Boolean.box(false), Boolean.box(false))
        imageWidget.addMouseListener(new java.awt.event.MouseAdapter() {
          override def mousePressed(e: event.MouseEvent) {
            widgetPanel.mousePressed(e)
          }
        })
        widgetPanel.moveToBack( imageWidgetWrapper.asInstanceOf[WidgetWrapper] )
      }
    }
  }

  def invokeLater(body: => Unit) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      override def run() { body }
    })
  }

}
