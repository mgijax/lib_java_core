package org.jax.mgi.shr.dto;

/*
* $Header$
* $Name$
*/

/**
* @module DTOConstants.java
* @author jsb
*/

/** contains common constants for use in constructing <tt>DTO</tt>s.
*  a set of static constants to use for fieldnames when building DTOs.
*   (no instances of this class are to be created)
* @has a set of static constants.
* @does simply provides public access to these constants.
*/
public class DTOConstants
{
    ///////////////
    // constructors
    ///////////////

    /** hidden default constructor.  We do not want any instances of this
    *    class to be created.
    * @assumes nothing
    * @effects nothing
    * @throws nothing
    */
    private DTOConstants()
    {}

    /* -------------------------------------------------------------------- */

    ///////////////////
    // common constants
    ///////////////////

    // markers -- constants dealing with marker information

    public static String MarkerKey = "markerKey";
    public static String MarkerSymbol = "symbol";
    public static String MarkerName = "name";
    public static String MarkerType = "markerType";
    public static String MarkerIsWithdrawn = "isWithdrawn";
    public static String MarkerWithdrawnTo = "withdrawnTo";

    public static String Synonyms = "synonyms";
    public static String Aliases = "aliases";

    public static String NomenEvents = "nomenEvents";
    public static String OldName = "oldName";
    public static String NewName = "newName";
    public static String OldSymbol = "oldSymbol";
    public static String Event = "event";
    public static String EventDate = "eventDate";

    public static String Markers = "markers";
    public static String OrthologCount = "orthologCount";

    // alleles -- constants dealing with allele information

    public static String AlleleCounts = "alleleCounts";
    public static String AlleleTypeName = "alleleTypeName";
    public static String AlleleTypeKey = "alleleTypeKey";
    public static String AlleleTypeCount = "alleleTypeCount";

    // mapping -- constants dealing with mapping information

    public static String Chromosome = "chromosome";
    public static String CytogeneticOffset = "cytogeneticOffset";
    public static String MappingCount = "mappingCount";
    public static String MapPosition = "offset";

    // phenotypes -- constants dealing with phenotype information

    public static String HasMLC = "hasMLC";
    public static String PhenoCount = "phenoCount";

    // orthologies -- constants dealing with orthology information

    public static String HasHumanOrthology = "hasHumanOrthology";
    public static String OrthologousSpecies = "orthologousSpecies";

    // polymorphisms -- constants dealing with polymorphism

    public static String PcrCount = "pcrCount";
    public static String RflpCount = "rflpCount";

    // references -- constants dealing with references

    public static String Authors = "authors";
    public static String Citation = "citation";
    public static String FirstReference = "firstRef";
    public static String Jnum = "jnum";
    public static String LastReference = "lastRef";
    public static String ReferenceCount = "refCount";
    public static String RefsKey = "refsKey";
    public static String RefsKeys = "refsKeys";
    public static String RefsTitle = "refsTitle";
    public static String References = "references";
    public static String RefID = "referenceID";

    // gene ontology -- constants dealing with GO data

    public static String GOAnnotationCount = "goCount";
    public static String GOAnnotations = "goAnnotations";
    public static String GOID = "goID";
    public static String GOTerm = "goTerm";

    // gene expression -- constants dealing with expression data

    public static String AntibodyCount = "antibodyCount";
    public static String AssayTypeKey = "assayTypeKey";
    public static String ExpressionAssayCount = "expressionAssayCount";
    public static String ExpressionAssayCounts = "expressionAssayCounts";
    public static String ExpressionResultCount = "expressionResultCount";
    public static String ExpressionResultCounts = "expressionResultCounts";
    public static String ExpressionTissues = "expressionTissues";
    public static String GXDIndexCount = "gxdIndexCount";
    public static String NegativeExpressionCount = "minusExpressionCount";
    public static String PositiveExpressionCount = "plusExpressionCount";
    public static String ProbeCounts = "probeCounts";
    public static String cDNACount = "cDNACount";
    public static String Stage = "stage";
    public static String StageKey = "stageKey";
    public static String Structure = "structure";
    public static String StructureKey = "structureKey";
    public static String TheilerStages = "theilerStages";
    public static String TissueCount = "tissueCount";
    public static String Tissue = "tissue";

    // probes -- constants dealing with probes and clones

    public static String ProbeName = "probeName";
    public static String ProbeKey = "probeKey";
    public static String Probes = "probes";
    public static String CloneCollection = "cloneCollection";
    public static String CloneID = "cloneID";
    public static String SegmentType = "segmentType";

    // accession IDs -- constants dealing with various types of accession IDs

    public static String AccID = "accID";
    public static String AccIDs = "accIDs";
    public static String NucleotideSeqIDs = "nucleotideSequences";
    public static String OtherIDs = "otherIDs";
    public static String OtherMGIIDs = "otherMgiIDs";
    public static String PrimaryAccID = "primaryID";
    public static String ProteinSeqIDs = "proteinSequences";
    public static String SeqID = "seqID";
    public static String SeqIDs = "seqIDs";
    public static String LogicalDbKey = "logicalDbKey";
    public static String InterProTerms = "interProTerms";
    public static String ActualDB = "actualDB";
    public static String ActualDBs = "actualDBs";

    // Sequences -- constants dealing with sequence data

    public static String Sequences = "sequences";
    public static String SequenceKey = "sequenceKey";
    public static String SequenceLength = "length";
    public static String SequenceType = "seqType";
    public static String SequenceRecordDate = "sequenceRecordDate";
    public static String SequenceDate = "sequenceDate";
    public static String Strain = "strain";
    public static String RawStrain = "rawStrain";
    public static String SequenceProvider = "sequenceProvider";
    public static String SequenceStatus = "sequenceStatus";
    public static String SequenceVersion = "sequenceVersion";
    public static String SequenceDescription = "sequenceDescription";
    public static String Age = "age";
    public static String CellLine = "cellLine";
    public static String Gender = "gender";
    public static String Organism = "organism";
    public static String Library = "library";
    public static String LastAnnotationUpdate = "lastAnnotationUpdate";
    public static String LastSequenceUpdate = "lastSequenceUpdate";
    public static String SequenceCount = "sequenceCount";
    public static String RepDNASeq = "repDNASeq";
    public static String RepRNASeq = "repRNASeq";
    public static String RepProteinSeq = "repProteinSeq";


	// Assembly Coordinates -- constants for dealing with the assembly
	public static String IsAssembly = "isAssembly";
	public static String Strand = "strand";
	public static String StartCoord = "startCoord";
	public static String StopCoord = "stopCoord";

    // URLs -- constants dealing with URLs

    public static String GeneFamilyURL = "geneFamilyUrl";
    public static String MinimapURL = "minimapUrl";
    public static String URL = "url";

    // vocab -- constants dealing with standard vocabularies and annotations

    public static String IsNot = "isNot";
    public static String EvidenceCode = "evidenceCode";
    public static String InferredFrom = "inferredFrom";
    public static String TermKey = "_Term_key";
    public static String Term = "term";

    // miscellaneous -- constants which fit none of the other sections

    public static String DatabaseVersion = "databaseVersion";
    public static String DatabaseDate = "databaseDate";
    public static String Notes = "notes";
    public static String SearchToolResults = "searchToolResults";
}

/*
* $Log$
* Revision 1.10  2004/07/21 20:25:52  mbw
* javadocs edits only
*
* Revision 1.9  2004/06/25 11:10:27  jsb
* added three constants for AlleleType*, for TR5750
*
* Revision 1.8  2004/03/12 18:59:06  jsb
* Added new expression-related definitions
*
* Revision 1.7  2004/03/10 19:00:14  jw
* Added support for sequenceDate and sequenceRecordDate
*
* Revision 1.6  2004/03/08 16:02:06  jw
* Added refID to dto constants
*
* Revision 1.5  2004/02/17 17:18:50  jw
* Added search tool support
*
* Revision 1.4  2004/02/12 23:37:51  jw
* Added Support for the sequence factory
*
* Revision 1.3  2004/02/11 16:28:16  jsb
* Added ActualDB and ActualDBs
*
* Revision 1.2  2004/02/10 16:10:23  jsb
* Added many more definitions
*
* Revision 1.1  2003/12/30 16:56:28  mbw
* imported into this product
*
* Revision 1.2  2003/12/01 13:10:21  jsb
* Code review changes, added some new terms
*
* Revision 1.1  2003/07/03 17:29:55  jsb
* initial addition for use by JSAM WI
*
* $Copyright$
*/
