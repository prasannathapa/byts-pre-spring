DAY 4 DEMO: a web server you can actually watch working
=======================================================

Four small programs. Nothing to install, nothing to download, no internet
needed. Tomcat is just a jar sitting in this folder, and we start it from
main() like any other Java object.

Start at Demo00. It is a web server written in plain Java with no Tomcat and
no servlets at all, so you can see there is nothing magic underneath.


RUNNING IT, OPTION A: IntelliJ
------------------------------
  File > Open > pick this day-4-demo folder > Trust project.
  Open a file in src/ and press the green arrow next to main.


RUNNING IT, OPTION B: no IDE
----------------------------
  macOS / Linux      ./run.sh Demo02Lifecycle
  Windows            run.cmd Demo02Lifecycle

  Leave the name off and it runs Demo00.


THE FOUR PROGRAMS
-----------------
  Demo00NoFrameworkAtAll
      A web server in about forty lines of plain Java. No Tomcat. No servlets.
      Run it, then open http://localhost:8080 in a browser. It prints the exact
      text your browser sent it, and writes the reply back by hand.
      The lesson: a web server is a program that holds a port open, reads text
      and writes text back. Everything else is convenience on top of that.
      Press Ctrl+C to stop it. This is the only demo that keeps running.

  Demo01WhatIsAServer
      The same idea, now with Tomcat doing the socket work. Watch the five
      steps print: build, register, start, serve one request, stop. Notice we
      hand our object to the container and never call it ourselves.

  Demo02Lifecycle
      Who creates your servlet, and how often? Prints the constructor, init(),
      three doGet() calls and destroy(), in the order they really happen.
      Two surprises worth watching for: init() does not run at startup, it runs
      on the first request, and the three requests arrive on three threads.

  Demo03OneObjectManyThreads
      There is only ONE servlet object, shared by every request at once. This
      fires 500 requests at two counters. The one using a plain int field loses
      hundreds of counts. The one using AtomicInteger is always right.
      This is the bug that never appears on your laptop and always appears in
      production.


WORDS YOU WILL SEE, IN PLAIN ENGLISH
------------------------------------
  Port          A numbered door on a machine. A server waits at one.
  Socket        One open conversation through that door.
  HTTP          The text format browsers and servers use. Just lines of text.
  Web server    A program that waits on a port and answers HTTP.
  Servlet       Your class, which the server calls when a request arrives.
  Container     The program that holds your servlets, creates them, and decides
                which one handles which URL. Tomcat is a container.
  Thread pool   A set of ready workers. One picks up each request.


IF SOMETHING GOES WRONG
-----------------------
  "Address already in use" on Demo00
      Something else is on port 8080. Stop it, or change 8080 in the code.

  Demo00 does not stop
      It is a server, so it waits forever on purpose. Ctrl+C in the terminal,
      or the red square in IntelliJ.

  A wall of Tomcat logging
      The demos turn it down, but the first line or two can still appear.
      Our own output is the indented text.
