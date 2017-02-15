package com.lifecosys.suit;

import javaslang.Function1;
import javaslang.control.Option;
import javaslang.control.Try;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
 * @author <a href="mailto:guyang@lansent.com">Young Gu</a>
 */
public abstract class ClasspathTemplate<D> implements Template<D> {

    protected final String prefix;
    protected final String suffix;
    protected final String templateName;
    protected final Function1<D, D> transformer;

    public ClasspathTemplate(String templateName, Consumer<D> transformer) {
        this(templateName, (doc) -> {
            transformer.accept(doc);
            return doc;
        });
    }

    public ClasspathTemplate(String prefix, String suffix, String templateName, Consumer<D> transformer) {
        this(prefix, suffix, templateName, (doc) -> {
            transformer.accept(doc);
            return doc;
        });
    }

    public ClasspathTemplate(String templateName, Function1<D, D> transformer) {
        this("/webapp/templates", "html", templateName, transformer);
    }

    public ClasspathTemplate(String prefix, String suffix, String templateName, Function1<D, D> transformer) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.templateName = templateName;
        this.transformer = transformer;
    }

    abstract Option<D> create(InputStream inputStream);

    public Option<D> template() {
        return loadTemplate(templateName);
    }

    protected Option<D> loadTemplate(String templateName) {
        String templateFile = String.format("%s/%s.%s", prefix, templateName, suffix);
        return Try.of(() -> getClass().getResourceAsStream(templateFile)).toOption().flatMap(this::create);
    }

    public Function1<D, D> transformer() {
        return transformer;
    }

}
