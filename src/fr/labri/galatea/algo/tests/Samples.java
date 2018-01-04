package fr.labri.galatea.algo.tests;

import fr.labri.galatea.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Samples {

    private Samples() {}

    public static Context sampleContext1() {
        Context c = new Context();
        Attribute aa = new BinaryAttribute("a");
        Attribute ab = new BinaryAttribute("b");
        Attribute ac = new BinaryAttribute("c");
        Attribute ad = new BinaryAttribute("d");
        Attribute ae = new BinaryAttribute("e");
        Attribute af = new BinaryAttribute("f");
        Attribute ag = new BinaryAttribute("g");
        Attribute ah = new BinaryAttribute("h");
        Entity e1 = new Entity("1");
        Entity e2 = new Entity("2");
        Entity e3 = new Entity("3");
        Entity e4 = new Entity("4");
        Entity e5 = new Entity("5");
        Entity e6 = new Entity("6");
        c.addAttribute(aa);
        c.addAttribute(ab);
        c.addAttribute(ac);
        c.addAttribute(ad);
        c.addAttribute(ae);
        c.addAttribute(af);
        c.addAttribute(ag);
        c.addAttribute(ah);
        c.addEntity(e1);
        c.addEntity(e2);
        c.addEntity(e3);
        c.addEntity(e4);
        c.addEntity(e5);
        c.addEntity(e6);
        c.addPair(e1, ab);
        c.addPair(e1, ac);
        c.addPair(e1, ad);
        c.addPair(e1, ae);
        c.addPair(e2, aa);
        c.addPair(e2, ab);
        c.addPair(e2, ac);
        c.addPair(e2, ag);
        c.addPair(e2, ah);
        c.addPair(e3, aa);
        c.addPair(e3, ab);
        c.addPair(e3, af);
        c.addPair(e3, ag);
        c.addPair(e3, ah);
        c.addPair(e4, ad);
        c.addPair(e4, ae);
        c.addPair(e5, ac);
        c.addPair(e5, ad);
        c.addPair(e6, aa);
        c.addPair(e6, ah);
        return c;
    }

    public static Set<String> sampleGsh1() {
        return new HashSet<>(Arrays.asList(
                new String[] {
                        "([a, h],[2, 3, 6]) 0 1",
                        "([b],[1, 2, 3]) 0 2",
                        "([c],[1, 2, 5]) 0 2",
                        "([d],[1, 4, 5]) 0 2",
                        "([a, b, g, h],[2, 3]) 2 2",
                        "([c, d],[1, 5]) 2 1",
                        "([d, e],[1, 4]) 1 1",
                        "([a, b, f, g, h],[3]) 1 0",
                        "([a, b, c, g, h],[2]) 2 0",
                        "([b, c, d, e],[1]) 3 0",
                }
        ));
    }

    public static Set<String> sampleLattice1() {
        return new HashSet<>(Arrays.asList(
                new String[] {
                        "([],[1, 2, 3, 4, 5, 6]) 0 4",

                        "([a, h],[2, 3, 6]) 1 1",
                        "([b],[1, 2, 3]) 1 2",
                        "([c],[1, 2, 5]) 1 2",
                        "([d],[1, 4, 5]) 1 2",

                        "([a, b, g, h],[2, 3]) 2 2",
                        "([b, c],[1, 2]) 2 2",
                        "([c, d],[1, 5]) 2 1",
                        "([d, e],[1, 4]) 1 1",

                        "([a, b, f, g, h],[3]) 1 1",
                        "([a, b, c, g, h],[2]) 2 1",
                        "([b, c, d, e],[1]) 3 1",

                        "([a, b, c, d, e, f, g, h],[]) 3 0",
                }
        ));
    }

    public static Set<String> conceptOrderHash(ConceptOrder o) {
        Set<String> hash = new HashSet<>();
        for (Concept c : o)
            hash.add(c.toString() + " " + c.getParents().size() + " " + c.getChildren().size());
        return hash;
    }

}
