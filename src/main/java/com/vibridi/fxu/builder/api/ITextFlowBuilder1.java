package com.vibridi.fxu.builder.api;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.text.Font;

public interface ITextFlowBuilder1 {
	public ITextFlowBuilder1 addTitle(String text);
	public ITextFlowBuilder1 addSubTitle(String text);
	public ITextFlowBuilder1 addSection(String text);
	public ITextFlowBuilder1 addBody(String text);
	public ITextFlowBuilder1 addNewLine();
	public ITextFlowBuilder1 addText(String text, Font font);
	public List<Node> toList();
}
