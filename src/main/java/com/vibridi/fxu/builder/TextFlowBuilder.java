package com.vibridi.fxu.builder;

import java.util.ArrayList;
import java.util.List;

import com.vibridi.fxu.builder.api.ITextFlowBuilder1;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class TextFlowBuilder {

	public static TextFlowBuilder newText() {
		return new TextFlowBuilder();
	}
	
	private List<Node> nodes;	
	private Font TITLE;
	private Font SUBTITLE;
	private Font SECTION_TITLE;
	private Font SECTION_BODY;
	
	private TextFlowBuilder() {
		nodes = new ArrayList<>();
	}
	
	public TextFlowBuilder1 setFont(String family, int referenceFontSize) {
		TITLE = Font.font(family, FontWeight.BOLD, referenceFontSize);
		SUBTITLE = Font.font(family, FontWeight.NORMAL, referenceFontSize-2);
		SECTION_TITLE = Font.font(family, FontWeight.BOLD, referenceFontSize-6);
		SECTION_BODY = Font.font(family, FontWeight.NORMAL, referenceFontSize-6);
		return new TextFlowBuilder1();
	}

	
	public class TextFlowBuilder1 implements ITextFlowBuilder1 {

		@Override
		public ITextFlowBuilder1 addTitle(String text) {
			Text t = new Text(text + "\n");
			t.setFont(TITLE);
			nodes.add(t);
			return this;
		}

		@Override
		public ITextFlowBuilder1 addSubTitle(String text) {
			Text t = new Text(text + "\n");
			t.setFont(SUBTITLE);
			nodes.add(t);
			return this;
		}

		@Override
		public ITextFlowBuilder1 addSection(String text) {
			Text t = new Text(text + "\n");
			t.setFont(SECTION_TITLE);
			nodes.add(t);
			return this;
		}

		@Override
		public ITextFlowBuilder1 addBody(String text) {
			Text t = new Text(text + "\n");
			t.setFont(SECTION_BODY);
			nodes.add(t);
			return this;
		}

		@Override
		public ITextFlowBuilder1 addNewLine() {
			nodes.add(new Text("\n"));
			return this;
		}

		@Override
		public ITextFlowBuilder1 addText(String text, Font font) {
			Text t = new Text(text + "\n");
			t.setFont(font);
			nodes.add(t);
			return this;
		}

		@Override
		public List<Node> toList() {
			return nodes;
		}

	}
	
}
