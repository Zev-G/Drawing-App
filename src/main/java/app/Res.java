package app;

public final class Res {

    public static String css(String css) {
        return css.endsWith(".css") ? getResource(css) : getResource(css + ".css");
    }

    public static String getResource(String res) {
        return Res.class.getClassLoader().getResource(res).toExternalForm();
    }

}
