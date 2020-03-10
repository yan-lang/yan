package yan.foundation.utils.formatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Align a list of lines that have several block.
 * e.g.
 *
 * @see ContentAligner#align
 */
public class ContentAligner {
    /**
     * Space between different block in a line.
     */
    public final int space;

    /**
     * Initialize a {@code ContentAligner} with {@code space = space}.
     *
     * @param space Space between different block in a line.
     */
    public ContentAligner(int space) {
        this.space = space;
    }

    /**
     * Initialize a {@code ContentAligner} with {@code space = 1}.
     *
     * @see ContentAligner#ContentAligner(int)
     */
    public ContentAligner() {
        this.space = 1;
    }

    /**
     * Align a list of lines that have several block.
     *
     * <pre>{@code
     *     Identifier - stdin:1:1;0:1 -> "a"
     *     Plus+ - stdin:1:2;1:1 -> "null"
     *     Identifier - stdin:1:3;2:3 -> "b"
     *     EOF - stdin:1:4;3:3 -> "null"
     * }</pre>
     * will be formatted as follow:
     * <pre>{@code
     *     Identifier - stdin:1:1;0:1 -> "a"
     *     Plus+      - stdin:1:2;1:1 -> "null"
     *     Identifier - stdin:1:3;2:3 -> "b"
     *     EOF        - stdin:1:4;3:3 -> "null"
     * }</pre>
     *
     * @param content A list of block with a certain format:
     *                [block_1,block_2,...,block_n],
     *                where strings in block_n have the same length.
     * @return aligned content
     * @throws RuntimeException when number of items in blocks not matched.
     */
    public String align(List<List<String>> content) {
        int num_blocks = content.size();
        int num_lines = content.get(0).size();
        StringBuilder contentBuilder = new StringBuilder();

        List<Integer> paddings = computePaddings(content);
        for (int l = 0; l < num_lines; l++) {
            StringBuilder lineBuilder = new StringBuilder();
            for (int b = 0; b < num_blocks; b++) {
                String item = content.get(b).get(l);
                lineBuilder.append(item)
                        .append(getSpaces(paddings.get(b) - item.length()))
                        .append(getSpaces(space));
            }
            contentBuilder.append(lineBuilder.toString()).append('\n');
        }
        return contentBuilder.toString();
    }

    private List<Integer> computePaddings(List<List<String>> content) {
        List<Integer> paddings = new ArrayList<>();
        int num_lines = content.get(0).size();
        for (var block : content) {
            if (num_lines != block.size())
                throw new RuntimeException("Each block must have the same size.");
            int maxn = 0;
            for (var item : block) {
                if (item.length() > maxn) maxn = item.length();
            }
            paddings.add(maxn);
        }
        return paddings;
    }

    private String getSpaces(int count) {
        return " ".repeat(count);
    }
}
