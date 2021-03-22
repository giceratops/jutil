package gh.giceratops.jutil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

@Jacksonized @Builder
@Data
@AllArgsConstructor
public class Pair<L, R> {

    private L left;
    private R right;

    public static <L, R> Pair<L, R> of(final L left, final R right) {
        return new Pair<>(left, right);
    }

    public static <L, R> Pair<L, R> nonNull(final L left, final R right) {
        return new Pair<>(Objects.requireNonNull(left), Objects.requireNonNull(right));
    }

    public L leftOrDefault(final L def) {
        return Objects.requireNonNullElse(this.left, def);
    }

    public R rightOrDefault(final R def) {
        return Objects.requireNonNullElse(this.right, def);
    }

    public boolean equals(final L left, final R right) {
        return Objects.equals(this.left, left)
                && Objects.equals(this.right, right);
    }
}
