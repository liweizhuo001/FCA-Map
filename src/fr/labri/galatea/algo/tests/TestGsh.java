package fr.labri.galatea.algo.tests;

import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.algo.Algorithm;
import fr.labri.galatea.algo.Ceres;
import fr.labri.galatea.algo.SimpleGSH;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestGsh {

    @Ignore
    @Test
    public void testCeres() {
        Context c = Samples.sampleContext1();
        Algorithm a = new Ceres(c);
        a.compute();
        ConceptOrder o = a.getConceptOrder();
        assertEquals(Samples.sampleGsh1(), Samples.conceptOrderHash(o));
    }

    @Test
    public void testSimpleGSH() {
        Context c = Samples.sampleContext1();
        Algorithm a = new SimpleGSH(c);
        a.compute();
        ConceptOrder o = a.getConceptOrder();
        assertEquals(Samples.sampleGsh1(), Samples.conceptOrderHash(o));
    }

}
