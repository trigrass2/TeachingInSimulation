package com.cas.sim.tis.test.svg;

import com.cas.sim.tis.svg.SVGGlyph;
import com.cas.sim.tis.svg.SVGHelper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestSvg extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		SVGHelper.loadGlyphsFont(TestSvg.class.getResource("/svg/iconfont.svg"));
		SVGHelper.getAllGlyphsIDs().forEach(System.out::println);
//		
		SVGGlyph svgGlyph = null;

		svgGlyph = new SVGGlyph("iconfont.svg.excel", Color.RED, 64);
		svgGlyph.setSizeForWidth(64);
		Button btn1 = new Button("文字1", svgGlyph);
//		
		svgGlyph = new SVGGlyph("iconfont.svg.excelwenjian",Color.AQUA,  32);
		svgGlyph.setSizeForWidth(32);
		svgGlyph.setRotate(45);
		Button btn2 = new Button("文字2", svgGlyph);
//		
		svgGlyph = new SVGGlyph("iconfont.svg.wui-f-excel",Color.BLANCHEDALMOND,  16);
		svgGlyph.setSizeForWidth(16);
		Button btn3 = new Button("文字3", svgGlyph);

		VBox vb = new VBox(btn1, btn2, btn3);

		primaryStage.setScene(new Scene(vb));

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
