# daqtester
a quick hacked client for testing the datacollector

### Download:

To download for installation, 
[click here](https://github.com/MSOE-Supermileage/daqtester/releases/download/1.0.0/daqtester.apk)
from your phone's browser.

### Install

Runs on android phones with android v5.1 or newer (Lollipop). You can find this in your `Settings > About`. 
It might run on older phones, but it wasn't tested or built for them.

You can install this bad boy by checking a checkbox in Settings > Security > Allow Unkown Sources or if you need more
help, by following [this tutorial](http://www.ubergizmo.com/how-to/how-to-install-apk-files-sideloading-on-android/)

Once downloaded, installed, and running and you should get a black screen with some text. Once you connect your 
phone to the Raspberry Pi, you should get a  big ole' dump of data. 

If the app doesn't start dumping a ton of text on the screen that vaguely looks like 
```
connecting...
{speed:"40.0",rpm:"100.0",clown_total:"1.0",flux_capacitor_charge:"50.0"}
```
something is wrong with the DAQ.

You can hit the `parse?` checkbox to test 
data parsing to make sure the phone can read it if you are currently having issues with the heads up display app. 

You can also hit the `clear` button if you want. This is a good idea if you let it run too long as the text on
the screen does not truncate untill you hit `clear`.

### Potential causes of not receiving data:

1. **Your phone is not connected**

   Check both ends of the cable.

2. **File system (sdcard) corrupt / kernel panic**

   Call the fire department. The sdcard will need to be reimaged or swapped.

3. **The daq can't find the hall effect sensing arduino and it can't run without it connected**

   Recommended recourse is to connect the hall effect arduino, even if you don't care about 
speed / wheel rpm. You don't have to plug in the actual hall effect sensor, but the arduino 
needs to be seen by the Raspberry Pi. This is a software bug that isn't currently worth fixing yet.

