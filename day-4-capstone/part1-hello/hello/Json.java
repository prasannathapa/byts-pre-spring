package hello;

/**
 * Given to you. One method: turn a Java string into a JSON string literal, quotes and all.
 *
 * There is no JSON library in this capstone on purpose. Writing the escaping once, by hand,
 * is what makes the day's last slide land: a framework does this for you and you now know
 * exactly what "does this for you" means.
 */
public final class Json {

    private Json() { }

    public static String str(String s) {
        StringBuilder b = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"'  -> b.append("\\\"");
                case '\\' -> b.append("\\\\");
                case '\n' -> b.append("\\n");
                case '\r' -> b.append("\\r");
                case '\t' -> b.append("\\t");
                default   -> {
                    if (c < 0x20) b.append(String.format("\\u%04x", (int) c));
                    else b.append(c);
                }
            }
        }
        return b.append('"').toString();
    }
}
