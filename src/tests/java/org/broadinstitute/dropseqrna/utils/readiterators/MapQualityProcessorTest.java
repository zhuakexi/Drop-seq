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
package org.broadinstitute.dropseqrna.utils.readiterators;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

import java.io.File;
import java.util.Collections;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class MapQualityProcessorTest {



	@Test(enabled = true)
	public void processReadTest() {
		String unmappedReadName="NS500217:67:H14GMBGXX:3:23408:5941:1275";
		String mappedReadName="NS500217:67:H14GMBGXX:1:22207:3769:12483";
		SAMRecord unmapped = getRecordFromBAM(unmappedReadName);
		SAMRecord mapped = getRecordFromBAM(mappedReadName);

		MapQualityFilteredIterator r1 = new MapQualityFilteredIterator(Collections.singletonList(unmapped).iterator(), 10, true);
		Assert.assertFalse(r1.hasNext());

        MapQualityFilteredIterator r2 = new MapQualityFilteredIterator(Collections.singletonList(mapped).iterator(), 10, true);
		Assert.assertTrue(r2.hasNext());
		Assert.assertEquals(mapped, r2.next());
        Assert.assertFalse(r2.hasNext());
	}


	// A slow way to get reads for testing.
	private SAMRecord getRecordFromBAM (final String readName) {
		File inFile = new File("testdata/org/broadinstitute/transcriptome/barnyard/5cell3gene.bam");
		SamReader reader = SamReaderFactory.makeDefault().enable(SamReaderFactory.Option.EAGERLY_DECODE).open(inFile);
		for (SAMRecord r: reader)
			if (r.getReadName().equals(readName)) return (r);
		throw new IllegalArgumentException("Asked for a read not in the BAM!");
	}
}
