import java.io.*;
import java.net.*;

/**
 * DEMO 0: a web server with nothing but plain Java. No Tomcat. No servlets.
 *
 * Run this, then open http://localhost:8080 in your browser. It works.
 * That is the point: a web server is not magic and not special. It is a program
 * that waits on a port, reads some text, and writes some text back.
 *
 * Read the text it prints. That IS what your browser sent, character for
 * character. Nobody hid it from you.
 */
public class Demo00NoFrameworkAtAll {

    public static void main(String[] args) throws Exception {

        // A port is a numbered door on this machine. 8080 is just a habit.
        try (ServerSocket door = new ServerSocket(8080)) {
            System.out.println("waiting on http://localhost:8080  (open it in a browser, Ctrl+C to stop)");
            System.out.println();

            while (true) {
                // accept() blocks until somebody knocks. It hands back one conversation.
                try (Socket visitor = door.accept()) {

                    var in = new BufferedReader(new InputStreamReader(visitor.getInputStream()));

                    // ---- READ what the browser sent. It is just lines of text. ----
                    System.out.println("------ the browser sent me this ------");
                    String line = in.readLine();
                    String requestLine = line;
                    while (line != null && !line.isEmpty()) {   // headers end at a blank line
                        System.out.println("   " + line);
                        line = in.readLine();
                    }
                    System.out.println("------ end ------");

                    // The first line is:   GET /something HTTP/1.1
                    String path = requestLine == null ? "/" : requestLine.split(" ")[1];

                    // ---- WRITE a reply. Also just lines of text. ----
                    String body = "<h1>Hello</h1><p>You asked for: " + path + "</p>";
                    var out = new PrintWriter(visitor.getOutputStream());
                    out.print("HTTP/1.1 200 OK\r\n");                      // status line
                    out.print("Content-Type: text/html\r\n");              // headers
                    out.print("Content-Length: " + body.length() + "\r\n");
                    out.print("\r\n");                                      // blank line, then the body
                    out.print(body);
                    out.flush();

                    System.out.println("I replied with 200 and " + body.length() + " bytes.");
                    System.out.println();
                }
            }
        }
    }
}
