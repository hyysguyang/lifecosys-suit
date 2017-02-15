package com.lifecosys.suit;

import javaslang.Function1;
import javaslang.control.Option;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:hyysguyang@gmail.com">Young Gu</a>
 * @author <a href="mailto:guyang@lansent.com">Young Gu</a>
 */
public class FragmentTransformer implements Function1<Jerry, Jerry> {

    private String layoutFragmentSelector = "[suit ^='::']";
    private String fragmentExtractor = "::\\s*(.*)\\s*:\\s*(.+)\\s*";

    private Function1<String, Option<Jerry>> loadFragment;

    public FragmentTransformer(Function1<String, Option<Jerry>> loadFragment) {
        this.loadFragment = loadFragment;
    }

    @Override
    public Jerry apply(Jerry jerry) {
        return mergeFragment(jerry, loadFragment);
    }

    Jerry mergeFragment(Jerry base, Function1<String, Option<Jerry>> loadFragment) {
        Jerry fragments = base.$(layoutFragmentSelector);
        if (fragments.size() == 0) return base;
        fragments.forEach(fragment -> {
            String suit = fragment.attr("suit");
            Pattern compile = Pattern.compile(fragmentExtractor);
            Matcher matcher = compile.matcher(suit);
            if (matcher.matches()) {
                String fragmentTemplateName = matcher.group(1);
                String fragmentSelector = matcher.group(2);
                Option<Jerry> innerFragment = loadFragment.apply(fragmentTemplateName);
                innerFragment.map(f -> {
                    removeAttributes(fragment);
                    copyAttributes(f.$(fragmentSelector), fragment);
                    return fragment.html(f.$(fragmentSelector).html());
                });
            }
        });

        return mergeFragment(base, loadFragment);

    }

    private void copyAttributes(Jerry source, Jerry dest) {
        Node fragmentNode = source.get(0);
        for (int i = 0; i < fragmentNode.getAttributesCount(); i++) {
            dest.get(0).setAttribute(fragmentNode.getAttribute(i).getName(), fragmentNode.getAttribute(i).getValue());
        }
    }

    private void removeAttributes(Jerry jerry) {
        Node node = jerry.get(0);
        for (int i = 0; i < node.getAttributesCount(); i++) {
            node.removeAttribute(node.getAttribute(i).getName());
        }
    }

}
