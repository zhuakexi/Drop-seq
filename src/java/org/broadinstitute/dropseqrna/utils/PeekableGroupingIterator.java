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

import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;

import htsjdk.samtools.util.IterableOnceIterator;
import htsjdk.samtools.util.PeekableIterator;

/**
 * Modeled after GroupingIterator, this class lets you group sets of objects
 * together by a comparator, but instead of returning all objects that have the
 * same value, this returns only the next object. You can peek to see if the
 * requested next object is in the same group you're currently in, or is in a
 * new group.
 *
 * @author nemesh
 *
 */
public class PeekableGroupingIterator<T> extends IterableOnceIterator<T> {

	private final PeekableIterator<T> underlyingIterator;
	private final Comparator<T> comparator;
	private T last; // the last object handed out.
	private boolean inGroup=false;

	public PeekableGroupingIterator(final Iterator<T> underlyingIterator, final Comparator<T> comparator) {
		this.underlyingIterator = new PeekableIterator<>(underlyingIterator);
		this.comparator = comparator;
		this.inGroup=false;
	}

	@Override
	public boolean hasNext() {
		return this.underlyingIterator.hasNext();
	}

	/**
	 * Is the next item in the iterator in the same group as the last one handed out?
	 * @return
	 */
	public boolean hasNextInGroup() {
		// if there's anything in the iterator and you haven't started, this is
		// the start of the group.
		if (last == null && this.underlyingIterator.hasNext()) return true;
		if (!this.underlyingIterator.hasNext()) return false;
		// otherwise, test if your last/next are in the same group.
		T next = underlyingIterator.peek();
		final int cmp = comparator.compare(last, next);
		if (cmp == 0)
			return true;
		if (cmp > 0)
			throw new IllegalStateException(
					String.format("Out of order iterator: %s > %s", last.toString(), next.toString()));
		return false;
	}

	@Override
	public T next() {
		this.last=this.underlyingIterator.next();
		return this.last;
	}

	@Override
    public void close() throws IOException {
        underlyingIterator.close();
        super.close();
    }

}
