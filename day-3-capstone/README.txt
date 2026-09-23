DAY 3 CAPSTONE - a bank on H2, end to end
==========================================

One capstone in three parts. Part 1 opens a database and moves rows in and out of
it; part 2 makes a transfer atomic and survives a hostile search box; the finale
puts one repository interface over two completely different implementations. Read
the RunTests.java in each part first - the tests are the specification.

Requires Java 25 (JDK) - the same version used on all four days. Check:  java -version

The database is H2, in memory. Nothing is installed and nothing is left on disk: the
whole database lives inside the JVM and disappears when the tests finish. One jar,
about 2.6 MB, fetched once by get-deps.


SETUP - brand new? follow this exactly (Windows / macOS / Linux)
---------------------------------------------------------------
You need two tools: Git (downloads the code) and a Java 25 JDK (compiles and
runs it). Do these six steps in order.

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

  5) GET THE H2 JAR (one time, needs the internet for about five seconds)
       cd day-3-capstone
       macOS / Linux:            ./get-deps.sh
       Windows (PowerShell):     .\get-deps.cmd
     It downloads h2-2.3.232.jar from Maven Central into lib/ and skips the download
     if the jar is already there. Behind a corporate proxy that blocks it, copy the
     jar from the shared folder into lib/ by hand and carry on. lib/ is deliberately
     not in git: jars do not belong in a source repository.

  6) RUN THE TESTS
       macOS / Linux:            ./run.sh
       Windows (PowerShell):     .\run.cmd
     The ./ and .\ are part of the command - keep them, they mean "run it here".
     run.sh and run.cmd call get-deps for you if lib/ is empty, so step 5 is really
     just so you know where the jar came from.

You will see FAILING tests. That is expected - the failures are your to-do list.
Fill in the TODOs until every part reports All green. Each part also has its own
run.cmd / run.sh if you want to run one at a time, and each one exits non-zero
until it is green, so a script can tell whether you are done.

Everything compiles against one classpath: -cp "lib/*:." on macOS and Linux,
-cp "lib/*;." on Windows. That is the entire build system for this capstone. No
Maven, no Gradle, nothing to configure.


PART 1 - connect, create, insert, query   (folder: part1-connect/, package: connect)
------------------------------------------------------------------------------------
The whole round trip, once: find a driver, open a connection, shape a table, put
rows in, get objects back. Fill in the TODOs:

    connect/Account.java     the row as a record (given)
    connect/Db.java          open() a connection to H2 in memory   (driver loading)
    connect/Schema.java      create() the table, exists() via metadata     (DDL, DatabaseMetaData)
    connect/AccountDao.java  insert / findById / findAll, one mapper  (PreparedStatement, ResultSet)

Which concept each test proves:
  - The driver test reads your source and fails if it finds Class.forName. The h2 jar
    carries META-INF/services/java.sql.Driver, so ServiceLoader has already done it.
    It finds your .java files from the class files as well as from the working
    directory, so it works the same from run.sh and from the run button in an IDE,
    and it FAILS rather than passing quietly if it cannot find them at all.
  - exists() is checked twice, once before create and once after, so a method that
    just returns true gets nowhere. Note the table name is ACCOUNT in capitals:
    unquoted identifiers are folded to upper case and the catalogue keeps them that way.
  - create() runs twice in one test. IF NOT EXISTS is why that is allowed.
  - Two accounts with different owners and different balances go in and come back as
    equal records, so a mapper that hard-codes a column cannot pass.
  - A missing row gives Optional.empty, not null and not an exception. Three different
    ids are asked for, including one that exists, so a blanket "return empty" fails too.
  - findAll is checked with three rows, then with none, then with one. Read the cursor
    with `if` instead of `while` and the three-row case collapses to one.
  - O'Brien and the string "x'); DROP TABLE account; --" both round-trip unchanged.
    Concatenate either of them into your SQL and you will find out why we bind.
  - A second test reads AccountDao.java, because escaping the quotes by hand passes the
    first one and is still wrong. It fails on createStatement, on SQL assembled with +,
    on a prepared statement that is not opened by a try-with-resources, and on a FIND_ALL
    that has lost its ORDER BY. Bind the values into the SQL you were given and it is quiet.
  - The CHECK constraint refuses a negative balance: the test wants a
    SQLIntegrityConstraintViolationException with an SQLState in class 23, and the row
    count unchanged afterwards. H2 says 23513 there and PostgreSQL says 23514, which is
    exactly why the test looks at the class and not at all five digits.


PART 2 - transfer money atomically      (folder: part2-transfer/, package: transfer)
------------------------------------------------------------------------------------
Two UPDATEs that have to happen together, and a search box that a hostile user is
typing into. Fill in the TODOs:

    transfer/Account.java                   the record (given)
    transfer/Db.java                        schema plus ana, bo and O'Brien (given)
    transfer/InsufficientFundsException.java unchecked, carries from/to/cents (given)
    transfer/TransferService.java           transfer() and deposit()   (transactions)
    transfer/AccountSearch.java             findByOwner, findByOwnerPrefix  (injection, LIKE)

Which concept each test proves:
  - Two different transfers in one test, then a third that empties an account exactly
    to zero. The CHECK allows zero and refuses minus one.
  - An overdraft throws InsufficientFundsException and BOTH balances are exactly what
    they were. The database refused the debit; your rollback undid the rest.
  - A transfer to an id nobody has is the sharp one. The debit worked, the credit
    changed 0 rows, you noticed the count and threw. If your catch only catches
    SQLException, that unchecked throw sails past the rollback, reaches the finally,
    and setAutoCommit(true) COMMITS the debit. Money vanishes. Catch wider.
  - After every failure, getAutoCommit() has to be true again. A pooled connection goes
    back to the pool carrying whatever you left on it, and the next borrower inherits it.
  - The silly transfers (zero, negative, an account paying itself) are tried on a CLOSED
    connection. Anything that touches the database there throws a SQLException, so the
    only way to see an IllegalArgumentException is to have validated first.
  - findByOwner("x' OR '1'='1") matches nobody while O'Brien still finds himself. A ?
    binds a value; it never becomes syntax.
  - And, as in part 1, a test reads AccountSearch.java and TransferService.java. Doubling
    the quotes yourself defuses both attack strings and still fails this one, on purpose:
    the lesson is the ?, not the escaping. It also checks the two SQL constants still carry
    their ? and their ESCAPE '!', and that nothing here is left open after an exception.
  - findByOwnerPrefix("%") has to return NOTHING. Binding stopped the injection, but %
    and _ are special to LIKE itself, so you escape them yourself and say ESCAPE '!'.
    The test also searches for "a_" and for "!b", so escaping only the % is not enough.
  - 20 threads deposit 10 cents each on their own connections and the balance has to be
    up by exactly 200. SELECT then UPDATE loses most of them. Let the database do the
    arithmetic: balance_cents = balance_cents + ?.


FINALE - one interface, two implementations  (folder: part3-finale/, package: finale)
-------------------------------------------------------------------------------------
Day 1 said depend on an interface. Here is what that buys you. You write ONE class:

    finale/Account.java                    record with of() and withId() (given)
    finale/AccountRepository.java          the interface (given)
    finale/RepositoryException.java        unchecked, keeps the cause (given)
    finale/AccountService.java             totals and the richest account (given, never edited)
    finale/InMemoryAccountRepository.java  complete, the worked example (given)
    finale/JdbcAccountRepository.java      YOURS. About ninety lines.

RunTests runs the same four contract tests against both repositories from one loop.
The in-memory one is already green, so every red line you see belongs to yours.

Which concept each test proves:
  - save fills in an id and returns a COPY: the account you passed in still has a null
    id afterwards, because a record cannot be changed.
  - findById returns what was saved and Optional.empty for an id nobody has, on both
    implementations. Same contract, different machinery: that is Liskov with a test.
  - saveAll of 1000 accounts gives 1000 distinct ids and a count of 1000, then a batch
    of 3 and a batch of 0 do the right thing too. On the JDBC side that means one
    addBatch loop, one executeBatch, and getGeneratedKeys read once afterwards.
  - The service computes the same total and the same richest account over each repository
    in turn. The dispatch itself is given to you, so that test cannot catch a polymorphism
    mistake; what it establishes is that your repository answers the service correctly, and
    the fact that the same loop runs unchanged over a HashMap and over H2 is the argument
    of the day rather than a thing you can get wrong.
  - Ids climb in insertion order across two saveAll calls, and each id still finds its
    own row. An IDENTITY column, not a counter you invented.
  - A batch with one negative-balance row in the middle leaves the table exactly as it
    was. Only a transaction around the WHOLE batch does that, which is why saveAll is
    not a for loop of save() calls.
  - The RepositoryException from that batch has a BatchUpdateException as its cause,
    and getNextException() on it is the integrity violation. Translation at the
    boundary is fine; throwing the cause away is not.
  - A repository over a DataSource pointing at nothing must still CONSTRUCT. Open a
    connection in the constructor and your app cannot start before the database does.
    The first test hands you a DataSource that counts the connections it is asked for and
    wants that count to be zero when the constructor returns, then one after createSchema.
  - count() is watched too: that same counting DataSource records the SQL you send, and the
    test fails if count() fetches rows instead of asking the database for a number. Pulling
    three hundred rows home to call size() on them is the N plus one habit in miniature.
  - After 50 borrow, query and close cycles, H2 reports exactly one open session: the
    test's own. Every connection you borrowed, you gave back.


THE DEMO FOLDER
---------------
demo/LostUpdate.java is the lost-update demonstration from the afternoon, ready to
run on its own:  ./demo/run.sh  (or .\demo\run.cmd). Two tellers, one account, fifty
cents each onto a balance of a hundred, and a latch that holds both of them until both
have read. It prints 150 every single time, with no luck involved.

Then run it again with an argument to watch each fix work:

    ./demo/run.sh atomic      balance_cents = balance_cents + ?, one statement
    ./demo/run.sh lock        SELECT ... FOR UPDATE, so the second read waits
    ./demo/run.sh version     write WHERE version = ?, retry when nobody matched

All three print 200. The version run also prints a retry count of exactly one, which
is worth staring at: only the teller who lost the race pays for the retry.


WHAT COMES NEXT
---------------
Tomorrow the repository you just wrote gets a servlet in front of it, and a single
composition root that builds the DataSource, hands it to the repository, hands the
repository to the service, and starts the server. That class is the thing Spring's
container replaces. When somebody shows you @Repository and JdbcTemplate.query with
a RowMapper, you will recognise every piece of it, because you wrote them by hand
this afternoon.


THE REFERENCE SOLUTION
----------------------
There is a full reference for all three parts, and your instructor has it. Ask for
it once you have had a real go at the tests, not before: reading the answer first
costs you the bit that actually sticks.

Teaching this day? The instructor copy of the repo carries a `solution/` folder in
every part, with its own run.sh and run.cmd next to the code, so you can run all
three references before class. The public repo has no solution/ anywhere, by design.
