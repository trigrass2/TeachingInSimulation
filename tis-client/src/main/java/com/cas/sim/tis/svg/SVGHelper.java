package com.cas.sim.tis.svg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * will load icomoon svg font file, it will create a map of the available svg glyphs. the user can retrieve the svg glyph using its name.
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2016-03-09
 */
public class SVGHelper {

	private static final HashMap<String, SVGBuilder> glyphsMap = new HashMap<>();

	public static SVGBuilder getSVG(String name) {
		return glyphsMap.get(name);
	}

//	/**
//	 * will retrieve icons from the glyphs map for a certain glyphName
//	 * @param glyphName the glyph name
//	 * @return SVGGlyph node
//	 */
//	public static SVG getIcoMoonGlyph(String glyphName) throws Exception {
//		SVGBuilder builder = glyphsMap.get(glyphName);
//		if (builder == null) throw new Exception("Glyph '" + glyphName + "' not found!");
//		SVG svg = builder.build();
		// we need to apply transformation to correct the icon since
		// its being inverted after importing from icomoon
//		svg.getTransforms().add(new Scale(1, -1));
//		Translate height = new Translate();
//		height.yProperty().bind(Bindings.createDoubleBinding(() -> -glyph.getHeight(), svg.heightProperty()));
//		svg.getTransforms().add(height);
//		return svg;
//	}

	/**
	 * @return a set of all loaded svg IDs (names)
	 */
	public static Set<String> getAllGlyphsIDs() {
		return glyphsMap.keySet();
	}

	/**
	 * will load SVG icons from icomoon font file (e.g font.svg)
	 * @param url of the svg font file
	 * @throws IOException
	 */
	public static void loadGlyphsFont(URL url) throws IOException {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			docBuilder.setEntityResolver((publicId, systemId) -> {
				// disable dtd entites at runtime
				return new InputSource(new StringReader(""));
			});

//			File svgFontFile = new File(url.toURI());
			Document doc = docBuilder.parse(url.openStream());
			doc.getDocumentElement().normalize();

			NodeList glyphsList = doc.getElementsByTagName("glyph");
			for (int i = 0; i < glyphsList.getLength(); i++) {
				Node glyph = glyphsList.item(i);
				Node glyphName = glyph.getAttributes().getNamedItem("glyph-name");
				if (glyphName == null) {
					continue;
				}

				String glyphId = glyphName.getNodeValue();
				SVGBuilder builder = new SVGBuilder(i, glyphId, glyph.getAttributes().getNamedItem("d").getNodeValue());
				glyphsMap.put("iconfont.svg." + glyphId, builder);
			}
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

	/**
	 * will load SVG icons from input stream
	 * @param stream input stream of svg font file
	 * @param keyPrefix will be used as a prefix when storing SVG icons in the map
	 * @throws IOException
	 */
	public static void loadGlyphsFont(InputStream stream, String keyPrefix) throws IOException {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			docBuilder.setEntityResolver((publicId, systemId) -> {
				// disable dtd entites at runtime
				return new InputSource(new StringReader(""));
			});

			Document doc = docBuilder.parse(stream);
			doc.getDocumentElement().normalize();

			NodeList glyphsList = doc.getElementsByTagName("glyph");
			for (int i = 0; i < glyphsList.getLength(); i++) {
				Node glyph = glyphsList.item(i);
				Node glyphName = glyph.getAttributes().getNamedItem("glyph-name");
				if (glyphName == null) {
					continue;
				}

				String glyphId = glyphName.getNodeValue();
				SVGBuilder builder = new SVGBuilder(i, glyphId, glyph.getAttributes().getNamedItem("d").getNodeValue());
				glyphsMap.put(keyPrefix + "." + glyphId, builder);
			}
			stream.close();
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	/**
//	 * load a single svg icon from a file
//	 * @param url of the svg icon
//	 * @return SVGGLyph node
//	 * @throws IOException
//	 */
//	public static SVGGlyph loadGlyph(URL url) throws IOException {
//		String urlString = url.toString();
//		String filename = urlString.substring(urlString.lastIndexOf('/') + 1);
//
//		int startPos = 0;
//		int endPos = 0;
//		while (endPos < filename.length() && filename.charAt(endPos) != '-') {
//			endPos++;
//		}
//		int id = Integer.parseInt(filename.substring(startPos, endPos));
//		startPos = endPos + 1;
//
//		while (endPos < filename.length() && filename.charAt(endPos) != '.') {
//			endPos++;
//		}
//		String name = filename.substring(startPos, endPos);
//
//		return new SVGGlyph(id, name, extractSvgPath(getStringFromInputStream(url.openStream())), Color.BLACK);
//	}

	/**
	 * clear all loaded svg icons
	 */
	public static void clear() {
		glyphsMap.clear();
	}

//	private static String extractSvgPath(String svgString) {
//		return svgString.replaceFirst(".*d=\"", "").replaceFirst("\".*", "");
//	}
//
//	private static String getStringFromInputStream(InputStream is) {
//		BufferedReader br = null;
//		StringBuilder sb = new StringBuilder();
//
//		String line;
//		try {
//			br = new BufferedReader(new InputStreamReader(is));
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return sb.toString();
//	}

	public static final class SVGBuilder {
		private final int glyphId;
		private final String name;
		private final String svgPathContent;

		SVGBuilder(int glyphId, String name, String svgPathContent) {
			this.glyphId = glyphId;
			this.name = name;
			this.svgPathContent = svgPathContent;
		}

		public int getGlyphId() {
			return glyphId;
		}



		public String getName() {
			return name;
		}

		public String getSvgPathContent() {
			return svgPathContent;
		}
	}
}
