package finale;

/** Given. The same hand-rolled JSON as part 1, plus one account shape. */
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

    public static String account(Account a) {
        return "{\"id\":" + a.id() + ",\"owner\":" + str(a.owner()) + ",\"balancePaise\":" + a.balancePaise() + "}";
    }

    public static String error(String message) {
        return "{\"error\":" + str(message) + "}";
    }
}
