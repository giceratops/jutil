package gh.giceratops.jutil;

import java.util.Objects;

public record Pair<L, R>(L left, R right) {

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
