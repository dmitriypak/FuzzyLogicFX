package ru.bmstu.edu.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CvWebViewController {

  @FXML
  private WebView webView;

  void setURL(String url){
    System.out.println(url);
    WebView browser = new WebView();
    WebEngine webEngine = browser.getEngine();

// Load a page from remote url.
    webEngine.load(url);
  }


}
