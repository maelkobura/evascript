package dev.kobura.evascript.lexer;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EvaSyntax {

    @ToString
    public static class ParseResult {
        public String root;
        public List<String> embeds;
        private final String originalInput;
        private final String nonRootContent;  // Contenu sans le root block

        public ParseResult(String root, List<String> embeds, String originalInput, String nonRootContent) {
            this.root = root;
            this.embeds = embeds;
            this.originalInput = originalInput;
            this.nonRootContent = nonRootContent;
        }

        /**
         * Reassemble the input string WITHOUT the root block,
         * by replacing each embed block {…} with the corresponding new embed.
         *
         * @param newEmbeds list of replacements, must be same size as embeds
         * @return reassembled string with replaced embeds (without root)
         */
        public String reassemble(List<String> newEmbeds) {
            if (newEmbeds.size() != embeds.size()) {
                throw new IllegalArgumentException("Number of new embeds must match the number of original embeds.");
            }

            Pattern embedPattern = Pattern.compile("\\{(.*?)}");
            Matcher matcher = embedPattern.matcher(nonRootContent);

            StringBuffer result = new StringBuffer();
            int index = 0;

            while (matcher.find()) {
                String replacement = newEmbeds.get(index++);
                replacement = Matcher.quoteReplacement(replacement);
                matcher.appendReplacement(result, replacement);
            }
            matcher.appendTail(result);

            return result.toString().trim();
        }
    }

    public static ParseResult preprocess(String input) {
        // Match <? ... >
        Pattern rootPattern = Pattern.compile("<\\?(.*?)>", Pattern.DOTALL);
        Matcher rootMatcher = rootPattern.matcher(input);

        List<String> roots = new ArrayList<>();
        int rootStart = -1;
        int rootEnd = -1;
        while (rootMatcher.find()) {
            roots.add(rootMatcher.group(1).trim());
            rootStart = rootMatcher.start();
            rootEnd = rootMatcher.end();
        }

        if (roots.size() > 1) {
            throw new IllegalArgumentException("Multiple root blocks found in input.");
        }

        String root = roots.isEmpty() ? null : roots.get(0);

        // Construire la chaîne sans le root block
        String nonRootContent;
        if (rootStart >= 0 && rootEnd >= 0) {
            nonRootContent = input.substring(0, rootStart) + input.substring(rootEnd);
        } else {
            // Pas de root, tout le contenu est nonRootContent
            nonRootContent = input;
        }

        // Match { ... } dans nonRootContent
        Pattern embedPattern = Pattern.compile("\\{(.*?)}");
        Matcher embedMatcher = embedPattern.matcher(nonRootContent);

        List<String> embeds = new ArrayList<>();
        while (embedMatcher.find()) {
            embeds.add(embedMatcher.group(1).trim());
        }

        return new ParseResult(root, embeds, input, nonRootContent);
    }
}
