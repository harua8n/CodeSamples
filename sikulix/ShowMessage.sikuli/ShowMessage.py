# http://www.jython.org/jythonbook/en/1.0/GUIApplications.html
from java.awt import Component
from javax.swing import JTextArea, JFrame

import time

frame = JFrame('Message',
            defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE,
            size = (300, 300)
        )

t = JTextArea(text = 'Hello\nworld',
              editable = False,
              wrapStyleWord = True,
              lineWrap = True,
              alignmentX = Component.LEFT_ALIGNMENT,
              size = (300, 1)
             )
frame.add(t)
frame.visible = True
sleep(2)
frame.visible = False
sleep(0.5)
t.text = "{}\n{}".format("Hello",time.asctime())
frame.visible = True
sleep(2)
frame.visible = False
frame.dispose()
frame = None