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
package org.broadinstitute.dropseqrna.barnyard;

import java.io.File;

import picard.cmdline.CommandLineProgram;

import picard.cmdline.Option;
import picard.cmdline.StandardOptionDefinitions;

public abstract class DGECommandLineBase extends CommandLineProgram {
	
	@Option(shortName = StandardOptionDefinitions.INPUT_SHORT_NAME, doc = "The input SAM or BAM file to analyze.")
	public File INPUT;
	
	@Option(doc="The cell barcode tag.  If there are no reads with this tag, the program will assume that all reads belong to the same cell and process in single sample mode.")
	public String CELL_BARCODE_TAG="XC";
	
	@Option(doc="The molecular barcode tag.")
	public String MOLECULAR_BARCODE_TAG="XM";
	
	@Option(doc="The Gene/Exon tag")
	public String GENE_EXON_TAG="GE";
	
	@Option(doc="The strand of the gene(s) the read overlaps.  When there are multiple genes, they will be comma seperated.")
	public String STRAND_TAG="GS";
	
	@Option(doc="The edit distance that molecular barcodes should be combined at within a gene.")
	public Integer EDIT_DISTANCE=1;
	
	@Option(doc="The map quality of the read to be included.")
	public Integer READ_MQ=10;
	
	@Option(doc="The minimum number of reads a molecular barcode should have to be considered.  This is done AFTER edit distance collapse of barcodes.")
	public Integer MIN_BC_READ_THRESHOLD=0;
	
	@Option(doc="Gather up all cell barcodes that have more than some number of reads.", optional=true)
	public Integer MIN_NUM_READS_PER_CELL=null;
	
	@Option(doc="The minumum number of genes for a cell barcode to be reported.", optional=true)
	public Integer MIN_NUM_GENES_PER_CELL=null;
	
	@Option(doc="The minumum number of transcripts for a cell barcode to be reported.", optional=true)
	public Integer MIN_NUM_TRANSCRIPTS_PER_CELL=null;
	
	@Option(doc="Number of cells that you think are in the library.  This accomplishes the same goals as the MIN_NUM_READS_CORE argument, but instead of defining barcodes as important based on the number of reads, it picks the top <X> barcodes as core.", optional=true)
	public Integer NUM_CORE_BARCODES=null;
	
	@Option(doc="Override CELL_BARCODE and MIN_NUM_READS_PER_CELL, and process reads that have the cell barcodes in this file instead.  The file has 1 column with no header.", optional=true)
	public File CELL_BC_FILE=null;
	
	@Option(doc="Is the library stranded?  If so, use the strand info to more precisely place reads on the correct gene, and ignore reads that are on the wrong strand.")
	public boolean USE_STRAND_INFO=true;
	
	@Option (doc="Drop UMIs within a cell/gene pair that occur less than the average number of reads*<FILTER_FREQ> for all UMIs in the cell/gene pair.  " +
			"For example, if you had on average 1000 reads per UMI and a UMI with 1-10 reads, those small UMIs would be removed when this frequency was set to 0.01." +
			"This is off by default.  A sensible value might be 0.01.")
	public double RARE_UMI_FILTER_THRESHOLD=0;
	
	
}
