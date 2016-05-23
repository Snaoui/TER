package org.qcmix.post_processing;

import static org.junit.Assert.*;

import org.junit.Test;
import org.qcmix.post_processing.PostProcessing;

public class PostProcessingTest {
	
	String simulatedRedundantTags = "<table:table-cell office:value-type=\"string\"><text:p>"
			+ "&lt;table:table-cell office:value-type=\"string\"&gt;&lt;text:p&gt;Hello&lt/text:p&gt;"
			+ "&lt;/table:table-cell&gt;</text:p>"
			+ "</table:table-cell>";
	
	String simulatedSpecialChars = "&lt;a href=\"\"&gt;hello&lt;/a&gt;";
	String simulatedStyleTagContent = "table:style-name=\"ce01\"table:style-name=\"ce390\"table:style-name=\"ce456\"table:style-name=\"ce712\"";
	String simulatedStyleColorContent = "<style:text-properties fo:color=\"#ffffff\"></style>" +
	"<style:text-properties fo:color=\"#f0f0ab\"></style>" +
	"<style:text-properties fo:color=\"#01cc99\">contenu</style>" +
	"<style:text-properties fo:color=\"#FFA09C\"><style:text-properties fo:color=\"#abAF01\">Hello</style></style>";
	
	String SimulatedContentFile = "<style:text-properties fo:color=\"#bbadc3\">"
			+ "<table:table-cell office:value-type=\"string\"><text:p>"
			+ "&lt;table:table-cell office:value-type=\"string\"&gt;&lt;text:p&gt;<B>Hey!</B>&lt;/text:p&gt;"
			+ "&lt;/table:table-cell&gt;</text:p>"
			+ "</table:table-cell></style>";
	
	private void testRemoveCellsStyle(){
		String r = PostProcessing.removeCellsStyle(simulatedStyleTagContent);
		if (!r.equals(""))
		{
			fail("removeCellsStyle failed");
		}
	}
	
	private void testRemoveAllColors(){
		String r = PostProcessing.removeAllColors(simulatedStyleColorContent);
		if (!r.equals("<style:text-properties fo:color=\"#000000\"></style><style:text-properties fo:color=\"#000000\"></style>"
				+ "<style:text-properties fo:color=\"#000000\">contenu</style>"
				+ "<style:text-properties fo:color=\"#000000\">"
				+ "<style:text-properties fo:color=\"#000000\">Hello</style></style>"))
				fail("removeAllColors failed");
	}
	
	private void testEscapeXmlSpecialChars(){
		String r = PostProcessing.escapeXmlSpecialChars(simulatedSpecialChars);
		if (!r.equals("<a href=\"\">hello</a>"))
			fail("escapeXmlSpecialChars failed");
	}
	
	private void testRemoveRedundantXMLTag(){
		String r = PostProcessing.removeRedundantXMLTag(simulatedRedundantTags);
		if (!r.equals("&lt;table:table-cell office:value-type=\"string\"&gt;&lt;text:p&gt;Hello&lt/text:p&gt;"
			+ "&lt;/table:table-cell&gt;"))
			fail("removeRedundantXMLTag failed");
	}
	
	private void testSimulatedContentFile(){
		String r = PostProcessing.removeRedundantXMLTag(SimulatedContentFile);
		r = PostProcessing.escapeXmlSpecialChars(r);
		r = PostProcessing.removeAllColors(r);
		if (!r.equals("<style:text-properties fo:color=\"#000000\">"
			+ "<table:table-cell office:value-type=\"string\"><text:p><B>Hey!</B></text:p>"
			+ "</table:table-cell></style>"))
				fail("simulatedContentFile failed");
	}
	
	@Test
	public void testPostProcessing() {
		testRemoveCellsStyle();
		testRemoveAllColors();
		testEscapeXmlSpecialChars();
		testRemoveRedundantXMLTag();
		testSimulatedContentFile();
	}

}
