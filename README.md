# Hubitat HTTP Momentary Switch 2 Driver

This driver implements the "Switch" and "Contact Sensor" capabilities.  It is used to create an HTTP POST, PUT, or GET to a user defined IP Address and Port. I updated this code from the orginal use-case to allow for a "Custom" body text option. 

Instructions for use

**Create Hubitat Driver**
- Open up the "http-switch2.groovy" driver from this repository (https://github.com/ccruise/hubitat-http-switch2/blob/master/Drivers/http-switch2.src/http-switch2.groovy).  Make sure you hit the "RAW" button, then select/highlight all of the source code, and COPY everything (Ctrl-C on Windows, or right click->Copy). 
- In your Hubitat Elevation Hub's web page, select the "Drivers Code" section and then click the "+ New Driver" button in the top right corner.  This will open a editor window for manipulating source code.
- Click in the editor window.  Then PASTE all of the code you copied in the first step.
- Click the SAVE button in the editor window.

Credit to https://github.com/ogiewon/Hubitat/blob/master/Drivers/http-momentary-switch.src/README.md from which this code was forked and adapted. 
