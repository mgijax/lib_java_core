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
* @is a set of static constants to use for fieldnames when building DTOs.
*	(no instances of this class are to be created)
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

    // alleles -- constants dealing with allele information

    public static String AlleleCounts = "alleleCounts";

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
    public static String GXDIndexCount = "gxdIndexCount";
    public static String ProbeCounts = "probeCounts";
    public static String cDNACount = "cDNACount";
    public static String Stage = "stage";
    public static String StageKey = "stageKey";
    public static String TheilerStages = "theilerStages";
    public static String TissueCount = "tissueCount";

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

    // Sequences -- constants dealing with sequence data

    public static String Sequences = "sequences";
    public static String SequenceKey = "sequenceKey";
    public static String SequenceLength = "length";
    public static String SequenceType = "seqType";
    public static String Strain = "strain";
    public static String RawStrain = "rawStrain";

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
}

/*
* $Log$
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
