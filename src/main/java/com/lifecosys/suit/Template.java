package com.lifecosys.suit;

import javaslang.Function1;
import javaslang.control.Option;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
 * @author <a href="mailto:guyang@lansent.com">Young Gu</a>
 */
public interface Template<D> {

    Option<D> template();

    Function1<D, D> transformer();

    default Option<D> render() {
        return template().map(transformer());
    }

    String toString(D doc);

    default String renderToString() {
        return render().map(this::toString).getOrElse("");
    }

}
