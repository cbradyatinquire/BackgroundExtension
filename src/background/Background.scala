package background

import org.nlogo.api._
import org.nlogo.app.{WidgetWrapper, WidgetPanel, App}

/**
 * Created by IntelliJ IDEA.
 * User: cbrady
 * Date: 5/24/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
class Background extends org.nlogo.api.DefaultClassManager {
  /**
   * Loads the primitives in the extension. This is called once per model compilation.
   *
   * @param primManager The manager to transport the primitives to NetLogo
   */
  override def load(primManager: PrimitiveManager) {
    primManager.addPrimitive("add", AddBackground)
  }


  object AddBackground extends DefaultCommand {

    override def getSyntax: Syntax = Syntax.commandSyntax( Array(Syntax.StringType, Syntax.BooleanType) )

    def perform(args: Array[Argument], context: Context) {
      val filepath = args(0).getString
      val stretch = args(1).getBooleanValue
      val widgetPanel = App.app.workspace.getWidgetContainer.asInstanceOf[WidgetPanel]

      val methodArray = Class.forName("org.nlogo.app.WidgetPanel").getDeclaredMethods
      val addWidgetAsOpt = methodArray.find( method => method.toString contains("addWidget") )
      val addWidgetMethod = addWidgetAsOpt.getOrElse( throw new ExtensionException("Can't find the addWidget method") )

      val imageWidget = new ImageWidget

      try {
        val imageFile = new java.io.File(filepath)
        imageWidget.loadImage(imageFile)
        imageWidget.setStretchable( stretch )
        invokeLater( {
          addWidgetMethod.setAccessible(true)
          val imageWidgetWrapper = addWidgetMethod.invoke(widgetPanel, imageWidget, new java.lang.Integer(10), new java.lang.Integer(10), new java.lang.Boolean(false), new java.lang.Boolean(false))
          widgetPanel.moveToBack( imageWidgetWrapper.asInstanceOf[WidgetWrapper] )
        } )
      }
      catch {
        case e:Exception => throw new ExtensionException("Error in loading image file"); e.printStackTrace()
      }
    }

  }

  def invokeLater(body: => Unit) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      override def run() { body }
    })
  }
}
