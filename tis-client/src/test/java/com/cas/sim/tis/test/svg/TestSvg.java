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
		SVGGlyph svgGlyph = SVGHelper.getGlyph("iconfont.svg.excel");
		svgGlyph.setSizeForWidth(64);
		svgGlyph.setFill(Color.RED);
		Button btn1 = new Button("文字1", svgGlyph);
//		
		svgGlyph = SVGHelper.getGlyph("iconfont.svg.excelwenjian");
		svgGlyph.setSizeForWidth(32);
		svgGlyph.setRotate(45);
		Button btn2 = new Button("文字2", svgGlyph);
//		
		svgGlyph = SVGHelper.getGlyph("iconfont.svg.wui-f-excel");
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
