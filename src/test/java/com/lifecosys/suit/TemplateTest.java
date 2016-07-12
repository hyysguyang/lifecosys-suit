package com.lifecosys.suit;

import jodd.jerry.Jerry;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
 * @author <a href="mailto:guyang@lansent.com">Young Gu</a>
 */
public class TemplateTest {


    @Test
    public void testUpdateAttribute() throws Exception {
        Template<Jerry> template = createTemplate((match) -> match.$("body").attr("class", "updated class"));
        assertEquals("updated class", parse(template.renderToString()).$("body").attr("class"));

        template = createTemplate((match) -> match.$("body [role='tab']").text("Google"));
        assertEquals("Google", parse(template.renderToString()).$("body [data-target=\"[suit='tab-create-bank']\"]").text());
    }

    @Test
    public void testUpdateAttributeOfChildElement() throws Exception {
        Template<Jerry> template = createTemplate((match) -> match.$(".nav-tabs").attr("data-id", "1000000"));
        assertEquals("1000000", parse(template.renderToString()).$(".nav.nav-tabs").attr("data-id"));
    }

    @Test
    public void testMultipleUpdate() throws Exception {
        Consumer<Jerry> transformer = (match) -> {
            match.$(".nav-tabs").attr("data-id", "1000000");
            match.$(".nav-tabs").attr("data-role", "dialog");
            match.$(".nav-tabs [role='tab']").attr("data-name", "main-tab");
        };
        Template<Jerry> template = createTemplate(transformer);
        Jerry jerry = parse(template.renderToString());
        assertEquals("1000000", jerry.$(".nav.nav-tabs").attr("data-id"));
        assertEquals("dialog", jerry.$(".nav.nav-tabs").attr("data-role"));
        assertEquals("main-tab", jerry.$(".nav.nav-tabs a").attr("data-name"));
    }

    @Test
    public void testReferToUpdatedNode() throws Exception {
        Consumer<Jerry> transformer = (match) -> {
            match.$(".nav-tabs").attr("data-id", "1000000");
            match.$("[data-id='1000000']").attr("data-user", "jack");
            match.$("[data-id='1000000'] a").text("Test Link");
            match.$(".nav-tabs [role='tab']").attr("data-name", "main-tab");
        };
        Template<Jerry> template = createTemplate(transformer);
        Jerry jerry = parse(template.renderToString());
        assertEquals("1000000", jerry.$(".nav.nav-tabs").attr("data-id"));
        assertEquals("jack", jerry.$(".nav.nav-tabs").attr("data-user"));
        assertEquals("Test Link", jerry.$(".nav.nav-tabs a").text());
    }

    @Test
    public void testReferToInsertedNode() throws Exception {
        Consumer<Jerry> transformer = (match) -> {
            match.$(".nav-tabs").attr("data-id", "1000000");
            match.$("[data-id='1000000']").append("<div class='inserted'>Inserted Node</div>");
            match.$(".inserted").attr("name", "inserted-name");
        };
        Template<Jerry> template = createTemplate(transformer);
        Jerry jerry = parse(template.renderToString());
        assertEquals("1000000", jerry.$(".nav.nav-tabs").attr("data-id"));
        assertEquals("Inserted Node", jerry.$(".nav.nav-tabs .inserted").text());
        assertEquals("inserted-name", jerry.$(".nav.nav-tabs .inserted").attr("name"));
    }


    @Test
    public void testComposedTransformer() throws Exception {

        Consumer<Jerry> transformer1 = (match)-> match.$(".nav-tabs").attr("data-id", "1000000");
        Consumer<Jerry> transformer2 = (match)-> match.$(".nav-tabs").attr("data-role", "dialog");
        Consumer<Jerry> transformer3 = (match)-> match.$(".nav-tabs [role='tab']").attr("data-name", "main-tab");
        Consumer<Jerry> transformer = transformer1.andThen(transformer2).andThen(transformer3);

        Template<Jerry> template = createTemplate(transformer);
        Jerry jerry = parse(template.renderToString());
        assertEquals("1000000", jerry.$(".nav.nav-tabs").attr("data-id"));
        assertEquals("dialog", jerry.$(".nav.nav-tabs").attr("data-role"));
        assertEquals("main-tab", jerry.$(".nav.nav-tabs a").attr("data-name"));
    }

    @Test
    public void testPrototypeTagSupport() throws Exception {

        Template<Jerry> template = createTemplate(JoddClasspathTemplate.NOTHING);
        Jerry jerry = parse(template.renderToString());
        assertEquals(0, jerry.$(".prototype").size());
    }



    private Template<Jerry> createTemplate(Consumer<Jerry> transformer) {
        return new JoddClasspathTemplate("/com/lifecosys/suit", "html", "simple", transformer);
    }


    private Jerry parse(String content) throws SAXException, IOException {
        return new Jerry.JerryParser().parse(content);
    }

}