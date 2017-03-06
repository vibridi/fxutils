package com.vibridi.fxmlutils.controller;

import com.vibridi.fxmlutils.functional.StackPageEventCallback;

public class BaseStackPageController extends BaseController {

	private StackPageEventCallback stackPageCallback;
	
	public void addStackPageCallback(StackPageEventCallback stackPageCallback) {
		this.stackPageCallback = stackPageCallback;
	}
		
	protected void fireStackPageEvent() {
		stackPageCallback.processEvent();
	}
	
	
}
