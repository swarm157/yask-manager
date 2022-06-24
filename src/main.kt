import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.event.ActionEvent
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.swing.*


var window = JFrame("Yask manager")
var panel = JPanel()
var kill = JButton("kill")
var infoL = JLabel("meta")
var info = JLabel("meta")
var processesShow = JList<String>()
var scroll = JScrollPane(processesShow)
var procNumber = JLabel("meta")

class KillAction : AbstractAction() {
    override fun actionPerformed(e: ActionEvent?) {
        var pid = processesShow.selectedValue.split(" ").filter {p -> p != "" }[1]
        //for (value in processesShow.selectedValue.split(" ").filter {p -> p != "" }) {
          //  println(value)
        //}
        Runtime.getRuntime().exec("kill -9 $pid")
        var array = getListOfVerboseProcessReport()
        procNumber.text = "processes ${array.size}"
        //processesShow.setListData(File("/").list())
        processesShow.setListData(array)
        infoL.text = getMemoryInfo()[0]
        info.text = getMemoryInfo()[1]
        //println(pid)
    }
}

fun main(args: Array<String>) {

    scroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
    scroll.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
    scroll.minimumSize = Dimension(550, 420);
    scroll.preferredSize = Dimension(550, 420);
    scroll.maximumSize = Dimension(550, 420);

    window.add(panel)
    panel.add(scroll)
    kill.addActionListener(KillAction())
    panel.add(kill)
    panel.add(infoL)
    panel.add(info)
    panel.add(procNumber)
    window.setSize(550, 520)
    window.setLocation(MouseInfo.getPointerInfo().location.x - 200, MouseInfo.getPointerInfo().location.y - 300)
    window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    window.isResizable = false
    window.isVisible = true
    while (true) {
        var array = getListOfVerboseProcessReport()
        procNumber.text = "processes ${array.size}"
        //processesShow.setListData(File("/").list())
        processesShow.setListData(array)
        infoL.text = getMemoryInfo()[0]
        info.text = getMemoryInfo()[1]
        Thread.sleep(3000)
    }
}

@Throws(Exception::class)
private fun getListOfVerboseProcessReport() : Array<String?> {
    val process = Runtime.getRuntime()
        .exec("ps -e --sort=-pcpu -o pcpu,pid,size,flags,lim,lstart,nice,rss,start,state,tt,uid,cmd")
    val r = BufferedReader(InputStreamReader(process.inputStream))
    var line: String? = null
    var buffer = ArrayList<String>()
    while (r.readLine().also { line = it } != null) {
        buffer.add(line!!)
    }
    buffer.toArray()
    var out = arrayOfNulls<String>(buffer.size)
    buffer.toArray(out)

    return out
}

@Throws(Exception::class)
private fun getMemoryInfo() : Array<String?> {
    val process = Runtime.getRuntime()
        .exec("free")
    val r = BufferedReader(InputStreamReader(process.inputStream))
    var line: String? = null
    var buffer = ArrayList<String>()
    while (r.readLine().also { line = it } != null) {
        buffer.add(line!!)
    }
    buffer.toArray()
    var out = arrayOfNulls<String>(buffer.size)
    buffer.toArray(out)

    return out
}