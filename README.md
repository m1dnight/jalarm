JAlarm
======

This is a simple alarm clock built in Java. It is merely an exercise to keep busy.

Usage
-----

There is a simple interface which I abandoned due to not being a UX-designer. I have implemented a simple CLI interface which suits my needs better.

Compile the program in an executable .jar file (or download the jar from this repo). Help is available by executing

    java -jar alarm.jar -h

A normal execution would be something like:

    java -jar alarm.jar -t 13:37 -s 10 -f thatAnnoyingSong.mp3

The program can be snoozed by pressing enter. If you wish to stop the alarm you simply type `pleasestopimawake`. 

TODO
----
1. Snooze button
2. Repeat song N times
3. Remember previous songs
4. Pick random song out of list of songs
5. Listen to regular keypress instead of enter.
