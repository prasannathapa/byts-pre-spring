DAY 2 CAPSTONE - collections, and the contracts underneath them
================================================================

One capstone in three parts. Part 1 makes a type that is safe to use as a key;
part 2 replaces a broken subclass with a wrapper and builds an LRU cache; the
finale counts words with half the framework at once. Read the RunTests.java in
each part first - the tests are the specification.

Requires Java 25 (JDK) - the same version used on all four days. Check:  java -version


SETUP - brand new? follow this exactly (Windows / macOS / Linux)
---------------------------------------------------------------
You need two tools: Git (downloads the code) and a Java 25 JDK (compiles and
runs it). Do these five steps in order.

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

  5) GO TO TODAY'S FOLDER AND RUN THE TESTS
       cd day-2-capstone
       macOS / Linux:            ./run.sh
       Windows (PowerShell):     .\run.cmd
     The ./ and .\ are part of the command - keep them, they mean "run it here".

You will see FAILING tests. That is expected - the failures are your to-do list.
Fill in the TODOs until every part reports All green. Each part also has its own
run.cmd / run.sh if you want to run one at a time, and each one exits non-zero
until it is green, so a script can tell whether you are done.

One test is green before you start, in part 2. That is deliberate: it pins a bug
in a class you are not asked to fix, so you can see the bug rather than hear about it.


PART 1 - a Version that can be a key   (folder: part1-keys/, package: keys)
---------------------------------------------------------------------------
Most of the Collections framework is built on two methods you did not write. Write
them, and then watch four different collections behave because of it:

    keys/Version.java   parse, equals, hashCode, compareTo, toString   (Items 10, 11, 14)
    keys/Registry.java  a TreeSet behind an API that hands out views    (Items 15, 54, 55)

Version is deliberately NOT a record. A record would generate equals and hashCode
for you, and writing them by hand is the exercise. The fields are final, so a
Version cannot change after you build it: that is what a key has to promise, and
a key that mutates inside a HashSet is a key you have lost.

Which concept each test proves:
  - Two parsed copies of "1.2.3" are equal and share a hashCode, so a HashSet given
    both ends up with one element. Break either method and that test tells you which.
  - A separate test builds 200 different versions and counts how many different hash
    codes come back. `return 42;` is a legal hashCode and it passes the first test, so
    this one asks for spread as well as agreement (Item 11).
  - The versions that differ in ONE field are checked in all three positions, so an
    equals that returns true for anything of the right class cannot pass.
  - compareTo is checked on 1.9.0 against 1.10.0. Compare the strings instead and
    you get it backwards, because the character '9' is larger than '1'.
  - The TreeSet test wants compareTo consistent with equals (Item 14): a TreeSet
    dedupes by compareTo alone and never calls equals at all.
  - all() hands back an unmodifiable VIEW. The test holds the set it was given, adds
    a version to the registry afterwards, and looks through that same reference again:
    a defensive copy would still be showing three elements there. add() and remove()
    on it throw.
  - matching takes a Predicate and is probed with four different ones, including one
    that matches everything and one that matches nothing. Guessing what it "probably"
    means and hard-coding that fails on the second predicate.
  - latestOrElse is handed a Supplier that counts its own calls. orElse would call it
    every single time; only orElseGet leaves it alone (Item 55).
  - parse is given null, a blank string, a tab, an untrimmed "1.2.3 " and eight other
    bad shapes. A validation that checks only the one your eye lands on fails the rest.


PART 2 - wrap a Set, and build an LRU   (folder: part2-wrappers/, package: wrappers)
------------------------------------------------------------------------------------
The famous one. A set that counts how many elements were ever offered to it, written
the obvious way, reports six after you add three:

    wrappers/InstrumentedHashSet.java  the broken version (given, nothing to do)
    wrappers/ForwardingSet.java        the boilerplate, half done (finish it)
    wrappers/InstrumentedSet.java      the same counter, by wrapping        (Item 18)
    wrappers/LruCache.java             LinkedHashMap in access order        (Item 19)

Why each test is there:
  - The first test asserts the broken count of 6 and passes straight away. HashSet.addAll
    is written as a loop over add, and add is yours, so your count runs twice. The
    superclass called back into the subclass and nobody wrote that down anywhere.
  - Your InstrumentedSet is counted across batches of three, then two, then one. A
    version that adds a fixed number, or that quietly went back to extending HashSet,
    fails on the second batch.
  - The same wrapper is then put over a TreeSet and a LinkedHashSet and the iteration
    order is checked. That is the part a subclass of HashSet could never do.
  - retainAll and removeAll are run side by side with a plain HashSet doing the same
    thing, return value included, so "forwarded to the right method" is not a guess.
    Each is called twice with differently shaped collections, so passing on a remembered
    argument instead of the one you were handed shows up on the second call.
    containsAll is asked about a subset AND about an element that is missing.
  - equals is checked against a plain HashSet in both directions, and the hashCode
    against the number the JDK's own Set produces. Identity comparison fails that.
  - LruCache: put a, put b, read a, put c. If the third constructor argument is right,
    b is the one evicted, because reading a moved it to the back of the queue. The
    same sequence without the read evicts a instead, so both directions are pinned.
  - Capacity is checked at 3 and at 1 over fifty puts, and the surviving keys are named.
    A removeEldestEntry with a number typed into it passes one of those and not the other.

LinkedHashMap is the one place all day where extending a collection is correct, and the
reason is in the Javadoc: removeEldestEntry is documented as a hook the superclass will
call. Item 19 says design and document for inheritance or else prohibit it. HashSet did
not document its self-use. LinkedHashMap did.


FINALE - WordStats   (folder: part3-finale/, package: finale)
--------------------------------------------------------------
One class, and it uses most of what today covered:

    finale/WordStats.java   count, remove, rank, group, and hand the numbers out

Which concept each test proves:
  - Counting is merge(word, 1, Integer::sum). No containsKey, no null, one line.
  - remove uses merge too, and the remapping function returns null when the count
    hits zero. Returning null from merge DELETES the entry. The test checks that the
    word is absent from frequencies() rather than sitting there with a count of 0,
    while count() still answers 0 for it, because a missing word is a zero (Item 54).
  - Removing a word that was never there must not invent a negative entry. Two
    different unseen words are tried, in two different fixtures, and the size of the
    map is checked afterwards: a guard that rejects one particular string is not a guard.
  - Case folding goes through Locale.ROOT. One test sets a Turkish default locale, where
    'I' folds to a dotless i, and counts INDIA. Plain toLowerCase() fails it.
  - mostFrequent returns an Optional, empty when nothing has been counted, and on a
    tie it picks the alphabetically first word. Three different fixtures, one of them
    a tie that is not at the top of the alphabet.
  - topK is asked for with k of 1 through 6 and with k past the end, and a four-way tie
    sits exactly on the k boundary, so getting the tie direction backwards loses the
    wrong word. The bounded heap is the technique we want, and it keeps the WORST of the
    k at its head, so its comparator is the reverse of the output order, tie-break
    included. Be honest with yourself here: the tests pin the answer, not the complexity.
    A sort of the whole vocabulary would pass them and would still be the wrong answer
    when the vocabulary is a million words and k is ten.
  - byFirstLetter is computeIfAbsent into a TreeMap. The key order is asserted on two
    vocabularies: the first is one a HashMap iterates out of order, the second is one it
    would nearly get right by luck, with a single word added that breaks the luck. Sorted
    once is not sorted.
  - words() comes from stream().sorted().toList(), and adding to it throws.
    Collectors.toList() would not have thrown, and that difference is the test.
  - frequencies() is Map.copyOf: a snapshot that does not move when the stats do.
    index() is Collections.unmodifiableMap: a live view that does. The last test holds
    one of each at the same time and adds two words, so they cannot both be right
    unless you used the right call for each.


WHAT COMES NEXT
---------------
Tomorrow the data stops living in a HashMap and goes into a database. Day 3 is JDBC:
connections, PreparedStatement, transactions, and mapping a ResultSet row into an
object with a function you write once and pass around. That function is the same
shape as the Predicate and Supplier you passed into Registry today, and it is exactly
the shape Spring's JdbcTemplate expects. Day 4 puts an HTTP request in front of it.
Then Spring starts where we stop.


THE REFERENCE SOLUTION
----------------------
There is a full reference for all three parts, and your instructor has it. Ask for
it once you have had a real go at the tests, not before: reading the answer first
costs you the bit that actually sticks.

Teaching this day? The instructor copy of the repo carries a `solution/` folder in
every part, with its own run.sh and run.cmd next to the code, so you can run all
three references before class. The public repo has no solution/ anywhere, by design.
