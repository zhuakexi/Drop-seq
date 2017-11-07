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

import java.util.List;

import org.broadinstitute.dropseqrna.utils.FilteredIterator;
import org.broadinstitute.dropseqrna.utils.GroupingIterator;
import org.broadinstitute.dropseqrna.utils.MultiComparator;
import org.broadinstitute.dropseqrna.utils.StringTagComparator;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.samtools.util.CloserUtil;
import htsjdk.samtools.util.Log;
import htsjdk.samtools.util.ProgressLogger;

/**
 * Bundles together groups of reads that belong to a cell barcode / gene / UMI.
 * @author nemesh
 *
 */
public class UMIReadIterator implements CloseableIterator<List<SAMRecord>> {

	private static final Log log = Log.getInstance(UMIReadIterator.class);
    private static final ProgressLogger prog = new ProgressLogger(log);

	private final GroupingIterator<SAMRecord> atoi;

	/**
	 * Construct an object that generates UMI objects from a BAM file
	 * @param headerAndIterator The BAM records to extract UMIs from
	 * @param geneExonTag The geneExon tag on BAM records
	 * @param cellBarcodeTag The cell barcode tag on BAM records
	 * @param molecularBarcodeTag The molecular barcode tag on BAM records
	 * @param strandTag The strand tag on BAM records
	 * @param readMQ The minimum map quality of the reads
	 * @param useStrandInfo should the gene and read strand match for the read to be accepted
	 * @param cellBarcodes The list of cell barcode tag values that match the <cellBarcodeTag> tag on the BAM records.
     *                     Only reads with these values will be used.  If set to null, all cell barcodes are used.
	 */
	public UMIReadIterator(final SamHeaderAndIterator headerAndIterator,
                       final String geneExonTag,
                       final String cellBarcodeTag,
                       final String molecularBarcodeTag,
                       final String strandTag,
                       final boolean useStrandInfo,
                       final int readMQ,
                       final boolean rejectNonPrimaryReads,
                       final List<String> cellBarcodes
                       ) {



        final StringTagComparator cellBarcodeTagComparator = new StringTagComparator(cellBarcodeTag);
        final StringTagComparator geneExonTagComparator = new StringTagComparator(geneExonTag);
        final StringTagComparator molecularBarcodeTagComparator = new StringTagComparator(molecularBarcodeTag);
        final MultiComparator<SAMRecord> multiComparator;

		multiComparator = new MultiComparator<>(cellBarcodeTagComparator, geneExonTagComparator, molecularBarcodeTagComparator);
		// Filter records before sorting, to reduce I/O

		FilteredIterator<SAMRecord> filteringIterator = new MissingTagFilteringIterator(headerAndIterator.iterator, cellBarcodeTag, geneExonTag, molecularBarcodeTag, strandTag);

		filteringIterator = new MapQualityFilteredIterator(filteringIterator, readMQ, rejectNonPrimaryReads);
		// reference as the generic FilteredIterator

		if (useStrandInfo)
			filteringIterator = new GeneStrandFilteringIterator(filteringIterator, strandTag);

        CloseableIterator<SAMRecord> sortedAlignmentIterator = SamRecordSortingIteratorFactory.create(
                headerAndIterator.header, filteringIterator, multiComparator, prog);

		this.atoi = new GroupingIterator<>(sortedAlignmentIterator, multiComparator);
	}

	/**
	 * Gets the collection of reads for the next UMI - all the reads for the cell / gene / molecular barcode.
	 * @return Null if there are no reads left in the iterator.  Otherwise, returns a UMICollection.
	 */
	@Override
	public List<SAMRecord> next () {
		if (!this.atoi.hasNext())
			return null;
		/*
		List<SAMRecord> result = this.atoi.next();
		for (SAMRecord rec: result)
			if (rec.getReadName().equals("HN7TNBGXX:4:11609:2753:3252"))
				System.out.println("STOP");

		return result;
		*/
		return this.atoi.next();
	}

	@Override
	public boolean hasNext() {
		return this.atoi.hasNext();
	}

	@Override
	public void close() {
		CloserUtil.close(this.atoi);
	}

}
