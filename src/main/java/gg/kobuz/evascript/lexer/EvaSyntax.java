package gg.kobuz.evascript.lexer;


import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvaSyntax {

    public static String preprocessing(String text, Function<String, String> replacer) {

        Pattern regexPattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher matcher = regexPattern.matcher(text);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String replacement = replacer.apply(matcher.group(1));
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result); // Ajouter le reste du texte

        return result.toString();
    }

}
