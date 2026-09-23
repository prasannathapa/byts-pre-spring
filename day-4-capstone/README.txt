DAY 4 CAPSTONE - a bank over HTTP
==================================

One capstone in three parts. Part 1 boots a container and answers requests; part 2 adds a
filter, a listener, a session and one counter that fifty threads fight over; the finale wires
the whole application together in a single class. Read the RunTests.java in each part first -
the tests are the specification.

Requires Java 25 (JDK) - the same version used on all four days. Check:  java -version

This day needs jars (embedded Tomcat, and H2 for the finale). One download, step 6 below.


SETUP - brand new? follow this exactly (Windows / macOS / Linux)
---------------------------------------------------------------
You need two tools: Git (downloads the code) and a Java 25 JDK (compiles and
runs it). Do these seven steps in order.

  1) INSTALL GIT
       Windows         winget install Git.Git
       macOS           brew install git
       Linux           sudo apt update && sudo apt install git
       no package mgr  download from  https://git-scm.com/downloads
     check it:         git --version          (should print a version)

  2) INSTALL JAVA 25 (JDK) - free, from Adoptium Temurin
       Windows         winget search Temurin
                       then install the id it prints for the 25 JDK row:
                       winget install <that id>
       macOS           brew install --cask temurin@25
       Linux (apt)     sudo apt update && sudo apt install openjdk-25-jdk
       no package mgr  download from  https://adoptium.net, unpack it, then set
                       JAVA_HOME to the unpacked folder and add its bin to PATH
     check it:         java -version          (must say 25 - not 8 or 17)
     If java -version still says 17 or 21, your PATH is finding an older JDK first.
     Fix PATH before you go on, or nothing below will compile.

  3) CLONE THE CODE  (same command on every OS)
       git clone https://github.com/prasannathapa/byts-pre-spring.git
       cd byts-pre-spring

  4) OPEN IT IN AN IDE
       IntelliJ IDEA (preferred): File > Open > pick the byts-pre-spring folder > Trust
       VS Code:                   run  code .   then install "Extension Pack for Java"

  5) GO TO TODAY'S FOLDER
       cd day-4-capstone

  6) GET THE JARS (once, about 7 MB, straight from Maven Central)
       macOS / Linux:            ./get-deps.sh
       Windows (PowerShell):     .\get-deps.cmd
     They land in lib/, which is deliberately not committed. Three of them:
       tomcat-embed-core-11.0.26.jar         the container itself
       tomcat-annotations-api-11.0.26.jar    what it compiles against
       h2-2.3.232.jar                        the finale's database, same one as Day 3
     Behind a proxy at work? Set https_proxy first, or download the three files by hand
     from https://repo1.maven.org/maven2 and drop them in lib/.

  7) RUN THE TESTS
       macOS / Linux:            ./run.sh
       Windows (PowerShell):     .\run.cmd
     The ./ and .\ are part of the command - keep them, they mean "run it here".
     run.sh and run.cmd fetch the jars for you if lib/ is empty, so step 6 is only there
     so you know what is being downloaded.

You will see FAILING tests. That is expected - the failures are your to-do list.
Fill in the TODOs until every part reports All green. Each part also has its own
run.cmd / run.sh if you want to run one at a time, and each one exits non-zero
until it is green, so a script can tell whether you are done.

One warning about the tests in parts 2 and 3: they start a real Tomcat on a port the OS
picks, fire real HTTP at it, and some of them use fifty threads. A run takes a few seconds.
If your firewall asks whether java may accept connections on localhost, say yes.


THE DEMO            (folder: demo-hello/, package: demo)
---------------------------------------------------------
Twenty lines, no tests, nothing to fill in: the servlet from the mid-morning session.
Run it with demo-hello/run.sh (or run.cmd), open http://localhost:8080/hello?name=you,
hit refresh a few times and watch the thread name change. Ctrl-C stops it.


PART 1 - hello over HTTP        (folder: part1-hello/, package: hello)
-----------------------------------------------------------------------
A container you start yourself, and two servlets. Fill in the TODOs:

    hello/Server.java         boot Tomcat on port 0, map two servlets, stop cleanly
    hello/HelloServlet.java   GET /hello?name=X -> "hello X", 400 when there is no name
    hello/SquareServlet.java  GET /square?n=7 -> {"n":7,"square":49}, 400 on nonsense
    hello/Json.java           given: escapes a JSON string literal

Which concept each test proves:
  - Port 0 plus getConnector().getLocalPort(): the OS picks the port, so your code has to
    ask what it got. A test that hard-codes 8080 is a test that fails on a busy laptop.
  - The 405 on POST and the 404 on /nothing are not yours. HttpServlet's service() and the
    container's mapper answer those, and knowing which layer answered is the whole point.
  - The charset test sends a name with an accent and compares raw bytes, so setting
    "text/plain" without ";charset=UTF-8" fails. Set it before you touch the writer.
  - The 400 tests read your body, so sendError (which replaces the body with an HTML page)
    fails them. They also send both a missing name and a blank one: a check for null alone
    is half a check.
  - The /square tests use four different values, including a negative one and zero, so a
    servlet that prints a fixed string cannot pass.
  - The last test stops the server and expects the connection to be refused. stop() without
    destroy() leaves the socket held, and the test notices.


PART 2 - state and the chain    (folder: part2-state/, package: state)
-----------------------------------------------------------------------
The server is given this time; the pieces around it are yours:

    state/RequestLogFilter.java  log every request once, guard /admin, add a request id
    state/StartupListener.java   publish appName and startedAt into application scope
    state/VisitServlet.java      count visits per session, not per server
    state/HitServlet.java        GIVEN AND BROKEN: a plain int under fifty threads. Fix it.
    state/Server.java            given: the wiring, worth reading before part 3
    state/RequestLog.java        given: a CopyOnWriteArrayList of log lines
    state/InfoServlet.java       given: prints application scope, 500 if it is empty
    state/NewServlet.java        given: /new, /old (redirect) and /alias (forward)

Do StartupListener first. A listener that throws stops the context from starting at all, so
until it is written every test fails with a 404 and a stack trace at the top of the run.
That is worth seeing once: the application refuses to come up rather than half work.

Which concept each test proves:
  - The log test makes four different requests, one of them a POST and one of them a miss,
    and demands four exact lines, plus a sane elapsed time for each. A filter that logs only
    successes, or only GETs, fails it. The miss is logged because NotFoundServlet is mapped at
    "/": without that mapping the container answers before any filter chain exists.
  - The X-Request-Id test asks three times and demands three different ids, one of them from
    a servlet that flushed its response. Set the header after the chain returns and you are
    adding a header to a response that has already gone.
  - The /admin test checks 401 without a token, 401 on a second admin path, 200 with a token,
    and that the only log line is the filter's own. Short-circuiting means chain.doFilter was
    never called, and a guard that blocks everything under /admin fails just as loudly. This is
    also the test that needs the log line inside a finally: it is the one request that returns
    early, so a log written after the chain never runs for it. The four requests in the log test
    all finish normally, so they would not notice.
  - The session tests use two clients on purpose: one keeps its cookie and counts 1, 2, 3, the
    other drops cookies and sees 1 every time. An instance field passes neither.
  - The counter test fires 50 threads x 20 requests three times and demands the answers 1 to
    1000 with nothing repeated. That is what "one instance, many threads" costs you, and
    AtomicInteger.incrementAndGet is what it buys back. Under that much contention some requests
    really do take a millisecond or more, which is where the elapsed time you log gets checked.
  - The last two tests put a redirect and a forward side by side: two log lines and a
    Location header against one log line and a changed URI. Same destination, different story.


FINALE - the composition root   (folder: part3-finale/, package: finale)
-------------------------------------------------------------------------
Everything else is given, including Day 3's repository. Two files are yours:

    finale/App.java             the composition root: build the graph, own the lifecycle
    finale/AccountServlet.java  translate HTTP to the service and exceptions to statuses
    finale/JdbcAccountRepository.java, AccountService.java, Schema.java, Account.java,
    finale/ReportRenderer.java, LoggingFilter.java, RequestLog.java, Json.java   all given

Which concept each test proves:
  - The first two tests save an account through app.repository() and then read it back over
    HTTP. The second one also calls repository() twice and demands the same object both times,
    because "one graph" means the root wires one repository and hands it on. A repository()
    that returns a new one per call fails there, a servlet that builds its own fails on the
    read back, and so does anything static.
  - The 404 / 400 / 409 tests are the edge doing translation. The service throws
    NoSuchElementException, IllegalArgumentException and InsufficientFundsException, and it
    has never heard of HTTP. Only the servlet knows those are numbers.
  - The transfer tests move money twice and then try to overdraw twice, checking both
    balances each time. The rule is enforced inside the repository's transaction, so a check
    in the servlet would be a race, and a rollback that does not roll back shows up here.
  - The logging test proves a cross-cutting concern can be wired once at the root: no servlet
    mentions logging anywhere.
  - Three tests count constructions of ReportRenderer: zero after boot, one after the first
    report, and exactly one when twenty requests arrive at the same moment. That is why the
    servlet is handed a Supplier<ReportRenderer> and not a ReportRenderer, and why memoise
    uses a volatile field rather than a plain one. The second of those three saves an account
    between the two reports, so the numbers move and a remembered string cannot pass; the third
    sends the twenty requests to four different paths and checks every answer against the path
    that asked for it, which is where per request state parked in a servlet field shows up.
  - The last two tests start a second App, and then two at once. Each has its own database and
    its own port because configuration lives in the root and nothing is static. If a static
    field crept in, these fail.

When App.java reads like a list of what this application is made of, you are done.


WHAT COMES NEXT
---------------
Look at App.java again. You wrote a constructor that says: here is the database, here is the
repository that uses it, here is the service that uses that, here is the servlet, and here is
the filter in front of it all. Now imagine that class for forty classes instead of four, and
imagine it changing every time someone adds a feature.

That file is what Spring's container replaces. The wiring does not go away and it is not
magic: you annotate the pieces, Spring reads them, and it builds the same graph you just built
by hand, in the same order, for the same reason. Every time it surprises you, come back to
this file and ask what the equivalent line would have said.

Spring starts where we stop. You are ready for it now, and you know what it is doing.


THE REFERENCE SOLUTION
----------------------
There is a full reference for all three parts, and your instructor has it. Ask for
it once you have had a real go at the tests, not before: reading the answer first
costs you the bit that actually sticks.

Teaching this day? The instructor copy of the repo carries a `solution/` folder in
every part, with its own run.sh and run.cmd next to the code, so you can run all
three references before class. The public repo has no solution/ anywhere, by design.
