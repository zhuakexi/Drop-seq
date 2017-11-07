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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import htsjdk.samtools.util.Interval;

public class GTFRecord implements Comparable<GTFRecord> {

	private final Interval interval;
	private final String geneID;
	private final String geneName;
	private final String transcriptName;
	private final String transcriptID;
	private final String transcriptType;
	private final String featureType;
    private final Integer geneVersion;


	//{chr, startPos, endPos, strand,geneName,geneID,transcriptName, transcriptID,transcriptType,featureType};
	public GTFRecord(final String chromsome, final int start, final int end, final boolean negativeStrand, final String geneID, final String geneName,
                     final String transcriptName, final String transcriptID, final String transcriptType, final String featureType,
                     final Integer geneVersion) {
		this.interval=new Interval(chromsome, start, end, negativeStrand, null);
		this.geneID=geneID;
		this.geneName=geneName;
		this.transcriptName=transcriptName;
		this.transcriptID=transcriptID;
		this.transcriptType=transcriptType;
		this.featureType=featureType;
        this.geneVersion=geneVersion;
	}

	public Interval getInterval () {
		return this.interval;
	}

	public String getStrandAsString() {
		if (interval.isNegativeStrand()) return ("-");
		return ("+");
	}

	public String getChromosome() {
		return this.interval.getContig();
	}

	public int getStart() {
		return this.interval.getStart();
	}

	public int getEnd() {
		return this.interval.getEnd();
	}

	public boolean isNegativeStrand() {
		return this.interval.isNegativeStrand();
	}

	public String getGeneID() {
		return geneID;
	}

	public String getGeneName() {
		return geneName;
	}

	public String getTranscriptName() {
		return transcriptName;
	}

	public String getTranscriptID() {
		return transcriptID;
	}

	public String getTranscriptType() {
		return transcriptType;
	}

	public String getFeatureType() {
		return featureType;
	}

    public Integer getGeneVersion() { return geneVersion; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GTFRecord that = (GTFRecord) o;
        if (!Objects.equals(this.interval, that.interval)) return false;
        if (!Objects.equals(this.geneID, that.geneID)) return false;
        if (!Objects.equals(this.geneName, that.geneName)) return false;
        if (!Objects.equals(this.transcriptName, that.transcriptName)) return false;
        if (!Objects.equals(this.transcriptID, that.transcriptID)) return false;
        if (!Objects.equals(this.transcriptType, that.transcriptType)) return false;
        if (!Objects.equals(this.featureType, that.featureType)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = interval.hashCode();
        result = 31 * result + geneID.hashCode();
        result = 31 * result + geneName.hashCode();
        result = 31 * result + hashCodeIfNotNull(transcriptName);
        result = 31 * result + hashCodeIfNotNull(transcriptID);
        result = 31 * result + hashCodeIfNotNull(transcriptType);
        result = 31 * result + featureType.hashCode();
        return result;
    }

    private int hashCodeIfNotNull(final String str) {
        if (str == null)
			return 0;
		else
			return str.hashCode();
    }

    public List<String> validate() {
        // Don't allocate unless there are errors
        List<String> ret = null;
        ret = addErrorIfNull(ret, "Missing sequence name", interval.getContig());
        ret = addErrorIfNull(ret, "Missing gene_id", geneID);
        ret = addErrorIfNull(ret, "Missing gene_name", geneName);
        ret = addErrorIfNull(ret, "Missing feature type", featureType);
        if (featureType != null && !featureType.equals("gene")) {
            ret = addErrorIfNull(ret, "Missing transcript_name", transcriptName);
            ret = addErrorIfNull(ret, "Missing transcript_id", transcriptID);
        }
        // check for comma in gene name
        if (geneName.contains(","))
        	ret = addError(ret, "Reserved character ',' in gene name ["+ geneName +"]");
        return ret;
    }

    private List<String> addErrorIfNull(final List<String> errorList, final String message, final Object value) {
        if (value == null)
			return addError(errorList, message);
		else
			return errorList;
    }
    private List<String> addError(List<String> errorList, final String message) {
        if (errorList == null)
			errorList = new ArrayList<>();
        errorList.add(message);
        return errorList;
    }

	@Override
	public int compareTo(final GTFRecord o) {
		return this.interval.compareTo(o.getInterval());
	}

	@Override
	public String toString () {
		return (this.interval.toString() +" [" + this.geneName + " " + this.featureType+ "]");
	}

}
