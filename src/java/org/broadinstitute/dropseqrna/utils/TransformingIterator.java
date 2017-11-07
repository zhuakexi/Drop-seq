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

import htsjdk.samtools.util.CloserUtil;
import htsjdk.samtools.util.IterableOnceIterator;

import java.util.Iterator;

/**
 * Given an iterator of some type, transform each record to another domain object, possibly of the same or a different type.
 * These are 1 to 1 transforms.
 * @author nemesh
 *
 * @param <INPUT> The input iterator object type
 * @param <OUTPUT> The output iterator object type
 */
public abstract class TransformingIterator<INPUT,OUTPUT> extends IterableOnceIterator<OUTPUT> {

	protected final Iterator<INPUT> underlyingIterator;

	public TransformingIterator (final Iterator <INPUT> underlyingIterator) {
		this.underlyingIterator=underlyingIterator;
	}

	@Override
	public boolean hasNext() {
		return this.underlyingIterator.hasNext();
	}

	@Override
	public abstract OUTPUT next();

	@Override
	public void remove() {
		this.underlyingIterator.remove();
	}

	@Override
	public void close() {
		CloserUtil.close(this.underlyingIterator);
		// TODO Auto-generated method stub

	}

}
