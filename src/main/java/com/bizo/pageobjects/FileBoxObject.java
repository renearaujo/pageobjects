package com.bizo.pageobjects;

import java.io.File;

import org.openqa.selenium.JavascriptExecutor;

public class FileBoxObject extends AbstractElementObject {

  private final String id;

  /** Only take an id for our firefox change hack. */
  public FileBoxObject(final PageObject p, final String id) {
    super(p, id);
    this.id = id;
  }

  public void set(File file) {
    type(file.getAbsolutePath());
  }

  public void type(final String value) {
    getElement().sendKeys(value);
    // Firefox does not fire onchange even if we send tab (it gets mangled) or
    // focus on another element, so do this the very explicit way
    if (getWebDriver() instanceof JavascriptExecutor) {
      final String script = "var element = document.getElementById('" + id + "');" //
        + "if (element.dispatchEvent) {"
        + "var e = document.createEvent('HTMLEvents');"
        + "e.initEvent('change', true, true);"
        + "element.dispatchEvent(e);"
        + "}"
        + "return true;";
      ((JavascriptExecutor) getWebDriver()).executeScript(script);
    }
    // go ahead and click dummy-click-div in case its not FF
    if (DummyClickDiv.isEnabled()) {
      DummyClickDiv.click(getWebDriver());
    } else {
      // not sure what to do here--the tab char got corrupted by FF
    }
  }

}
