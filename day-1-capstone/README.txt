DAY 1 CAPSTONE - object design, end to end
===========================================

One capstone in three parts. Part 1 exercises the four pillars of OOP; part 2
exercises the five SOLID principles; the finale ties them together. Read the
RunTests.java in each part first - the tests are the specification.

Requires Java 25 (JDK) - the same version used on all four days. Check:  java -version


SETUP - brand new? follow this exactly (Windows / macOS / Linux)
---------------------------------------------------------------
You need two tools: Git (downloads the code) and a Java 25 JDK (compiles and
runs it). Do these five steps in order.

  1) INSTALL GIT
       Windows         winget install Git.Git
       macOS           brew install git
       Linux           sudo apt install git
       no package mgr  download from  https://git-scm.com/downloads
     check it:         git --version          (should print a version)

  2) INSTALL JAVA 25 (JDK) - free, from Adoptium Temurin
       Windows         winget search Temurin
                       then install the id it prints for the 25 JDK row:
                       winget install <that id>
       macOS           brew install --cask temurin@25
       Linux (apt)     sudo apt install openjdk-25-jdk
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

  5) GO TO TODAY'S FOLDER AND RUN THE TESTS
       cd day-1-capstone
       macOS / Linux:            ./run.sh
       Windows (PowerShell):     .\run.cmd
     The ./ and .\ are part of the command - keep them, they mean "run it here".

You will see FAILING tests. That is expected - the failures are your to-do list.
Fill in the TODOs until every part reports All green. Each part also has its own
run.cmd / run.sh if you want to run one at a time, and each one exits non-zero
until it is green, so a script can tell whether you are done.


PART 1 - a tiny bank            (folder: part1-bank/, package: bank)
--------------------------------------------------------------------
A small program that needs all four pillars at once. Fill in the TODOs:

    bank/Account.java      a balance that can never go negative     (encapsulation)
    bank/Transaction.java  a sealed family (interface given)         (abstraction)
                           Deposit / Withdraw / Transfer apply()      (polymorphism)
    bank/Ledger.java       holds accounts, runs transactions         (composition)

One convention for the whole day: every rule Account enforces, a bad amount and an
overdraft alike, throws IllegalArgumentException. The finale's Account does the same.

Why each pillar:
  - The overdraw test passes only if Account guards its private balance.
  - runAll takes a mixed list and calls one method; each Transaction knows how to
    apply itself - that is polymorphism.
  - You can add Transfer without touching the loop: open for extension.
  - The Ledger HOLDS a map instead of extending one: composition, not inheritance.


PART 2 - a report exporter      (folder: part2-report/, package: report)
------------------------------------------------------------------------
The seams are already cut for you: one small interface, two formats behind it, and
a service that holds whichever format it was handed. Fill in the three bodies, then
look at what is NOT there. Nothing switches on a format type, and the service never
says `new CsvFormat()`. A third format is a new class and no edit here:

    report/Report.java        the data (given - a record)
    report/ReportFormat.java  the seam: one small interface (given)          (ISP)
    report/CsvFormat.java     format a report as CSV                         (OCP, LSP)
    report/JsonFormat.java    format a report as JSON                        (OCP, LSP)
    report/ReportService.java validate, then use the INJECTED format         (SRP, DIP)

Which principle each test proves:
  - A brand new format that just works: dependency inversion + open-closed.
  - Every format honouring the same one-method contract: ISP + Liskov.
  - The service rejecting a blank title while the formatter only formats: SRP.
  - One test hands the service a format that records what it was called with, so
    "delegates" means called once, with that report, and the answer passed straight
    back. The row tests use different text and different row counts, so a format
    that prints a fixed string cannot pass.


FINALE - export a bank statement   (folder: part3-finale/, package: finale)
---------------------------------------------------------------------------
The two halves, joined. Export a list of accounts through a pluggable format -
the pillars produce the data, SOLID decides how it leaves the building:

    finale/Account.java           a guarded balance (given)          (encapsulation)
    finale/StatementFormat.java   the seam: one interface (given)     (ISP, OCP, DIP)
    finale/PlainFormat.java       one "id: balance" line per account  (OCP, LSP)
    finale/CsvFormat.java         a CSV statement                     (OCP, LSP)
    finale/StatementExporter.java hold the INJECTED format; delegate   (DIP, composition)

Which concept each test proves:
  - The six format tests pin the exact output shape, over an empty list, one account
    and three accounts, so a format that ignores its input cannot pass.
  - The two exporter tests show the exporter producing the format's output unchanged,
    without ever naming a concrete format.
  - The new-format test is OCP and DIP in one line: a format the exporter has never
    seen, and not a character changes inside the exporter.
  - The last test overdraws, catches the refusal, then exports: the balance the
    statement prints is the one Account protected.

When your own new format works through the exporter with no change to it, you have
every idea from Day 1 in one small file.


WHAT COMES NEXT
---------------
This is day 1 of four. Tomorrow it's Collections, and that HashMap inside the
Ledger stops being a detail you can wave at: you'll learn why it's a HashMap and
not a TreeMap, and what breaks when Account forgets equals and hashCode. Day 3
is JDBC, so the accounts stop living in memory and go into a real database. Day 4
is Servlets, where a browser asks for the statement you just learned to format.
Then Spring starts where we stop. Every wire you connect by hand this week is a
wire its container connects for you, and you'll know what it's actually doing.


THE REFERENCE SOLUTION
----------------------
There is a full reference for all three parts, and your instructor has it. Ask for
it once you have had a real go at the tests, not before: reading the answer first
costs you the bit that actually sticks.

Teaching this day? The instructor copy of the repo carries a `solution/` folder in
every part, with its own run.sh and run.cmd next to the code, so you can run all
three references before class. The public repo has no solution/ anywhere, by design.
