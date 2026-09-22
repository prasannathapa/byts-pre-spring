# Pre-Spring Java Bootcamp

Slides and hands-on capstones for a four day bootcamp on the Java you want before you touch Spring:
object design, collections, JDBC, and servlets.

**Get everything:**

```
git clone https://github.com/prasannathapa/byts-pre-spring
```

The slides are self-contained HTML. Open any `presentation.html` directly, or open `index.html` as a
launcher. Use the arrow keys to move through a deck.

## Days

| Day | Topic | Slides | Capstone |
| --- | --- | --- | --- |
| 1 | Object design: the pillars, then SOLID | `day-1/` | `day-1-capstone/` (bank, report, finale) |
| 2 | Java collections in depth | `day-2/` | `day-2-capstone/` |
| 3 | JDBC | `day-3/` | `day-3-capstone/` |
| 4 | Servlets and client-server architecture | `day-4/` | `day-4-capstone/` |

Day 4 stops at the point where a framework would take over, so Spring itself starts where this
course ends.

Each deck carries ten quiz questions spread through the day. Work them out before the answer goes
up; that is the point of them.

## Capstones

Each `*-capstone/` folder is a small Java exercise, no build tool. `RunTests.java` is the
specification: read it first, fill in the TODOs, and run until every test is green.

```
cd day-1-capstone
./run.sh            # Windows: run.cmd
```

You will see failing tests on the first run. That is expected, and it is your to-do list. The script
exits non-zero until every part is green.

Requires a JDK 17 or newer; Java 25 is what the course uses. Days 3 and 4 need a database driver and
a servlet container, so run `./get-deps.sh` (Windows: `get-deps.cmd`) in those capstone folders once
before the tests. It downloads a couple of jars into `lib/` and nothing else.

Your instructor has the reference solutions.
