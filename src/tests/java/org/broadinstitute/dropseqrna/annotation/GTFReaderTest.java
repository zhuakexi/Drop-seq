/*
 * The Broad Institute
 * SOFTWARE COPYRIGHT NOTICE AGREEMENT
 * This software and its documentation are copyright 2017 by the
 * Broad Institute/Massachusetts Institute of Technology. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever.
 * Neither the Broad Institute nor MIT can be responsible for its use, misuse,
 * or functionality.
 */
package org.broadinstitute.dropseqrna.annotation;

import htsjdk.samtools.util.OverlapDetector;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import picard.annotation.Gene;
import picard.annotation.Gene.Transcript;



public class GTFReaderTest {
	private final File GTF_FILE1 = new File("testdata/org/broadinstitute/transcriptome/annotation/human_ISG15.gtf.gz");
	private final File GTF_FILE2 = new File("testdata/org/broadinstitute/transcriptome/annotation/human_ISG15_FAM41C.gtf.gz");
	private final File GTF_FILE3 = new File("testdata/org/broadinstitute/transcriptome/annotation/human_SNORD18.gtf.gz");
	private final File SD = new File("testdata/org/broadinstitute/transcriptome/annotation/human_g1k_v37_decoy_50.dict");


	@Test(enabled=true, groups={"dropseq", "transcriptome"})
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void test1() {
		GTFReader r = new GTFReader(GTF_FILE1, SD);
		OverlapDetector<Gene> od = (OverlapDetector) r.load();
		Assert.assertNotNull(od);
		Collection<Gene> genes = od.getAll();
		Assert.assertEquals(genes.size(), 1);
		Gene g = genes.iterator().next();
		Assert.assertEquals(g.getStart(), 948803);
		Assert.assertEquals(g.getEnd(), 949920);
		Assert.assertTrue(g.isPositiveStrand());
		Gene.Transcript t = g.iterator().next();
		Assert.assertEquals(t.transcriptionStart, 948803);
		Assert.assertEquals(t.transcriptionEnd, 949920);
	}



	@Test(enabled=true, groups={"dropseq", "transcriptome"})
	// I like the negative strand genes da best, so I put one in this set.
	public void test2() {
		GTFReader r = new GTFReader(GTF_FILE2, SD);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		OverlapDetector<Gene> od = (OverlapDetector) r.load();
		Assert.assertNotNull(od);
		Collection<Gene> genes = od.getAll();
		Map<String, Gene> geneMap = new HashMap<>(genes.size());
		for (Gene g: genes)
			geneMap.put(g.getName(), g);

		Gene g = geneMap.get("FAM41C");

		Assert.assertEquals(genes.size(), 2);

		Assert.assertEquals(g.getStart(), 803451);
		Assert.assertEquals(g.getEnd(), 812283);
		Assert.assertTrue(g.isNegativeStrand());
        for (Transcript t : g)
			if (t.name.equals("FAM41C-001")) {
                Assert.assertEquals(t.name, "FAM41C-001");
                Assert.assertEquals(t.transcriptionStart, 803451);
                Assert.assertEquals(t.transcriptionEnd, 812283);
            }
	}

	@Test(enabled=true, groups={"dropseq", "transcriptome"})
	// I like the negative strand genes da best, so I put one in this set.
	public void testMixedChromosomeGene() {
		GTFReader r = new GTFReader(GTF_FILE3, SD);

		@SuppressWarnings({ "rawtypes", "unchecked" })
		OverlapDetector<Gene> od = (OverlapDetector) r.load();
		Assert.assertNotNull(od);
		Collection<Gene> genes = od.getAll();
		Map<String, Gene> geneMap = new HashMap<>(genes.size());
		for (Gene g: genes)
			geneMap.put(g.getName(), g);

		Gene g = geneMap.get("SNORD18");

		/*
		Assert.assertEquals(genes.size(), 2);

		Assert.assertEquals(g.getStart(), 803451);
		Assert.assertEquals(g.getEnd(), 812283);
		Assert.assertTrue(g.isNegativeStrand());
		Iterator<Transcript> iter = g.iterator();
		while (iter.hasNext()) {
			Transcript t = iter.next();
			if (t.name.equals("FAM41C-001")) {
				Assert.assertEquals(t.name, "FAM41C-001");
				Assert.assertEquals(t.transcriptionStart, 803451);
				Assert.assertEquals(t.transcriptionEnd, 812283);
			}
		}
		*/
	}



	@Test(enabled=false, groups={"dropseq", "transcriptome"})
	// this tests loading in a full GTF, which I'm not going to store in the code base itself, so I'll leave the test disabled.
	// this is more to see what errors are thrown in a full parse, rather than to debug individual elements.
	public void fullLoad () {
		File fullD = new File ("/humgen/cnp04/sandbox/data/Evan/common/individual_references/Human/Homo_sapiens.GRCh37.74.gtf");
		GTFReader r = new GTFReader(fullD, SD);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		OverlapDetector<Gene> od = (OverlapDetector) r.load();
		Assert.assertNotNull(od);
	}



}
