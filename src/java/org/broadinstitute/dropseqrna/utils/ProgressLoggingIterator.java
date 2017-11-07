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
package org.broadinstitute.dropseqrna.utils;

import java.util.Iterator;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.ProgressLogger;

/**
 * A simple iterator that logs each read that passes through it.
 * @author nemesh
 *
 */
public class ProgressLoggingIterator extends FilteredIterator<SAMRecord> {

	private final ProgressLogger progressLogger;

	public ProgressLoggingIterator (final Iterator<SAMRecord> underlyingIterator, final ProgressLogger progressLogger) {
		super(underlyingIterator);
		this.progressLogger=progressLogger;
	}

	@Override
	public boolean filterOut(final SAMRecord rec) {
		this.progressLogger.record(rec);
		return false;
	}






}
