DAY 3 DEMO: JDBC you can run in about thirty seconds
====================================================

Five small programs. Each one runs on its own, prints what it is doing, and
explains itself as it goes. Read one, run it, change a line, run it again.

There is NOTHING TO INSTALL and NOTHING TO DOWNLOAD.
No database server. No internet. The database lives inside the program while it
runs and disappears when it stops, and the driver that talks to it is already in
this folder (jars/h2-2.3.232.jar, 2.6 MB, it came with the repo).

If your wifi is bad today, this still works. That is the point of it.


RUNNING IT, OPTION A: IntelliJ
------------------------------
  File > Open > pick this day-3-demo folder > Trust project.
  Open src/Demo01HelloDatabase.java and press the green arrow next to main.

  The driver is already set up as a library, so there is nothing to configure.
  If IntelliJ asks for a project SDK, pick any Java 17 or newer.


RUNNING IT, OPTION B: no IDE at all
-----------------------------------
  macOS / Linux      ./run.sh Demo01HelloDatabase
  Windows            run.cmd Demo01HelloDatabase

  Leave the name off and it runs the first demo.


THE FIVE PROGRAMS, IN THE ORDER THEY MAKE SENSE
-----------------------------------------------
  Demo01HelloDatabase   The whole of JDBC on one screen: connect, create a table,
                        insert three rows, read them back. If you only run one,
                        run this one. Every later idea hangs off these five steps.

  Demo02Injection       The same login written twice. One of them lets a stranger
                        in with the password  ' OR '1'='1  and the other refuses.
                        Run it and watch it happen, then read why.

  Demo03Transactions    Moving money between two accounts. A transfer is two
                        updates, and if the second fails you must undo the first.
                        Shows commit and rollback doing exactly that.

  Demo04RowsToObjects   Turning rows into ordinary Java objects at the edge, so
                        the rest of your program never touches a ResultSet.

  Playground            A table is already set up. Change the SQL on the marked
                        line, press run, see what comes back. You cannot break
                        anything: it is rebuilt from scratch every run.


WORDS YOU WILL SEE, IN PLAIN ENGLISH
------------------------------------
  Driver            The translator for one particular database. We use H2's.
  Connection        An open line to the database. Close it when you are done.
  Statement         A question or command you send down that line.
  PreparedStatement The same, but with blanks (?) you fill in safely afterwards.
  ResultSet         The answer, read one row at a time.
  Transaction       A group of changes that must all happen, or none of them.

  jdbc:h2:mem:demo  is a database address, read left to right:
      jdbc   we are speaking JDBC
      h2     to an H2 database
      mem    that lives in memory, not on disk
      demo   and is called demo


IF SOMETHING GOES WRONG
-----------------------
  "No suitable driver found"
      The jar is not on the classpath. In IntelliJ, reopen the folder itself
      (not a parent folder) so it picks up the module settings. Outside an IDE,
      use the run script rather than calling java by hand.

  "release version 17 not supported" or similar
      Your Java is older than 17. Check with:  java -version
      Any JDK 17 or newer is fine; the course uses 25.

  Nothing prints
      You ran the file without a main method, or ran the wrong class. Every demo
      here has a main; run one of the five listed above.
