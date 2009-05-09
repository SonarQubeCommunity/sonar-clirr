package org.sonar.plugins.clirr;

/**
 * <pre>
 * clirr:clirr
 *   Generate a report from the Clirr output.
 * 
 *   Available parameters:
 * 
 *     classesDirectory (Default: ${project.build.outputDirectory})
 *       The classes of this project to compare the last release against.
 * 
 *     comparisonArtifacts
 *       List of artifacts to compare the current code against. This overrides
 *       comparisonVersion, if present. Each comparisonArtifact is made of a
 *       groupId, an artifactId, a version number. Optionally it may have a
 *       classifier (default null) and a type (default 'jar').
 * 
 *     comparisonVersion (Default: (,${project.version}))
 *       Version to compare the current code against.
 * 
 *     excludes
 *       A list of classes to exclude. These classes are excluded from the list of
 *       classes that are included. Values are specified in path pattern notation,
 *       e.g. org/codehaus/mojo/**.
 * 
 *     htmlReport (Default: true)
 *       Whether to render the HTML report or not.
 * 
 *     includes
 *       A list of classes to include. Anything not included is excluded. If
 *       omitted, all are assumed to be included. Values are specified in path
 *       pattern notation, e.g. org/codehaus/mojo/**.
 * 
 *     linkXRef (Default: true)
 *       Link the violation line numbers to the source xref. Defaults to true and
 *       will link automatically if jxr plugin is being used.
 * 
 *     logResults (Default: false)
 *       Whether to log the results to the console or not.
 * 
 *     minSeverity (Default: warning)
 *       Show only messages of this severity or higher. Valid values are info,
 *       warning and error.
 * 
 *     outputDirectory (Default: ${project.reporting.outputDirectory})
 *       Specifies the directory where the report will be generated.
 * 
 *     showSummary (Default: true)
 *       Whether to show the summary of the number of errors, warnings and
 *       informational messages.
 * 
 *     textOutputFile
 *       A text output file to render to. If omitted, no output is rendered to a
 *       text file.
 * 
 *     xmlOutputFile
 *       An XML file to render to. If omitted, no output is rendered to an XML
 *       file.
 * 
 *     xrefLocation (Default: ${project.build.directory}/site/xref)
 *       Location of the Xrefs to link to.
 * </pre>
 * 
 * @author mhaller
 */
public final class PluginParameters {

	private PluginParameters() {
	}

	public static final String GOAL = "clirr";

	public static final String CLASSES_DIRECTORY = "classesDirectory";
	public static final String COMPARISON_ARTIFACTS = "comparisonArtifacts";
	public static final String COMPARISON_VERSION = "comparisonVersion";
	public static final String EXCLUDES = "excludes";
	public static final String EXCLUDE = "exclude";
	public static final String HTMLREPORT = "htmlReport";
	public static final String INCLUDES = "includes";
	public static final String LINKXREF = "linkXRef";
	public static final String LOGRESULTS = "logResults";
	public static final String MINSEVERITY = "minSeverity";
	public static final String OUTPUTDIRECTORY = "outputDirectory";
	public static final String SHOWSUMMARY = "showSummary";
	public static final String TEXTOUTPUTFILE = "textOutputFile";
	public static final String XMLOUTPUTFILE = "xmlOutputFile";
	public static final String XREFLOCATION = "xrefLocation";

	public static final String SEVERITY_INFO = "info";
	public static final String SEVERITY_WARNING = "warning";
	public static final String SEVERITY_ERROR = "error";
}
