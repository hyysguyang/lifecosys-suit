package com.lifecosys.suit;

import javaslang.Function1;
import javaslang.control.Option;
import javaslang.control.Try;
import jodd.jerry.Jerry;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
 * @author <a href="mailto:guyang@lansent.com">Young Gu</a>
 */
public class JoddClasspathTemplate extends ClasspathTemplate<Jerry> {

    public static Consumer<Jerry> NOTHING = (doc) -> {};

    Logger logger() {
        return LoggerFactory.getLogger(this.getClass().getName());
    }

    public JoddClasspathTemplate(String templateName) {
        this(templateName, NOTHING);
    }

    public JoddClasspathTemplate(String templateName, Consumer<Jerry> transformer) {
        super(templateName, transformer);
    }

    public JoddClasspathTemplate(String prefix, String suffix, String templateName, Consumer<Jerry> transformer) {
        super(prefix, suffix, templateName, transformer);
    }

    public JoddClasspathTemplate(String templateName, Function1<Jerry, Jerry> transformer) {
        super(templateName, transformer);
    }

    public JoddClasspathTemplate(String prefix, String suffix, String templateName, Function1<Jerry, Jerry> transformer) {
        super(prefix, suffix, templateName, transformer);
    }

    @Override
    Option<Jerry> create(InputStream inputStream) {

        return Try.of(() -> new Jerry.JerryParser().parse(IOUtils.toString(inputStream))).toOption();
    }

    @Override
    public String toString(Jerry doc) {
        String html = doc.get(0).getHtml();
        logger().debug(html);
        return html;
    }

    public Function1<Jerry, Jerry> transformer() {
        Function1<Jerry, Jerry> prototypeTransformer = (doc) -> {
            doc.$("[suit ^='--']").remove();
            return doc;
        };
        return prototypeTransformer.andThen(transformer);
    }
}
